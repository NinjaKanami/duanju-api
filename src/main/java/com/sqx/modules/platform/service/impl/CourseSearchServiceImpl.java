package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.performer.entity.Performer;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.platform.dao.SeriesDao;
import com.sqx.modules.platform.entity.CoursePerformer;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.entity.SeriesCourse;
import com.sqx.modules.platform.service.CoursePerformerService;
import com.sqx.modules.platform.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 短剧榜/搜索
 *
 * @author Zbc
 */
@Service("courseSearchService")
public class CourseSearchServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseSearchService {

    @Resource
    private CourseDao courseDao;
    @Resource
    private PerformerService performerService;
    @Resource
    private CoursePerformerService coursePerformerService;

    @Override
    public Result searchCourse(Page<Course> page, String keyword, Long areaId, Long classifyId, Long platformId, Long isPrice, String sortField, Integer sort) {

        Page<Course> coursePage = courseDao.searchCourse(page, keyword, areaId, classifyId, platformId, isPrice, sortField, sort);
        if (coursePage != null && coursePage.getRecords() != null) {
            for (Course course : coursePage.getRecords()) {
                // 查询演员
                List<CoursePerformer> coursePerformerList = coursePerformerService.list(new QueryWrapper<CoursePerformer>().eq("course_id", course.getCourseId()));
                if (!coursePerformerList.isEmpty()) {
                    List<Performer> performerList = performerService.list(new QueryWrapper<Performer>().in("id", coursePerformerList.stream().map(CoursePerformer::getPerformerId).toArray()));
                    course.setPerformerList(performerList);
                }
            }
        }

        return Result.success().put("data", coursePage);
    }


}
