package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseClassificationDao;
import com.sqx.modules.course.entity.CourseClassification;
import com.sqx.modules.course.response.ClassificationResponse;
import com.sqx.modules.course.service.CourseClassificationService;
import com.sqx.modules.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseClassificationServiceImpl extends ServiceImpl<CourseClassificationDao, CourseClassification> implements CourseClassificationService {

    @Autowired
    private CourseService courseService;

    @Override
    public Result insertCourseClassification(CourseClassification course) {
        course.setIsDelete(0);
        baseMapper.insert(course);
        return Result.success("操作成功！");
    }

    @Override
    public Result updateCourseClassification(CourseClassification course) {
        baseMapper.updateById(course);
        return Result.success("操作成功！");
    }

    @Override
    public Result updateDelete(Long id) {
        baseMapper.updateDelete(id);
        return Result.success("操作成功！");
    }

    @Override
    public Result selectCourseClassification(Integer page, Integer limit, String classificationName) {
        if(page==null || limit==null){
            return Result.success().put("data", new PageUtils(baseMapper.selectCourseClassificationList( classificationName)));
        }
        Page<CourseClassification> pages = new Page<>(page, limit);
        IPage<CourseClassification> mapIPage = baseMapper.selectCourseClassificationPage(pages, classificationName);
        List<CourseClassification> classificationList = mapIPage.getRecords();
        for (CourseClassification courseClassification:classificationList){
            if(courseClassification.getCourseId()!=null){
                courseClassification.setCourse(courseService.selectCourseByCourseId(null,courseClassification.getCourseId()));
            }
        }
        return Result.success().put("data", new PageUtils(mapIPage));
    }

    @Override
    public CourseClassification selectCourseClassificationById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Map<String, Object>> selectClassification() {
        return baseMapper.selectMaps(new QueryWrapper<CourseClassification>().eq("is_delete",0));
    }

    /**
     * 查询短剧的分类信息
     *
     * @return
     */
    @Override
    public List<ClassificationResponse> queryClassification() {
        return baseMapper.queryClassification();
    }


}
