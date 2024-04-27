package com.sqx.modules.course.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.dao.CourseClassificationDao;
import com.sqx.modules.course.dao.CourseCollectDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.service.CourseCollectService;
import com.sqx.modules.course.service.CourseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectDao, CourseCollect> implements CourseCollectService {

    @Autowired
    private CourseClassificationDao courseClassificationDao;
    @Autowired
    private CourseDetailsService courseDetailsService;
    @Autowired
    private CourseDetailsDao courseDetailsDao;
    @Autowired
    private RedisUtils redisUtils;

    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result insertCourseCollect(CourseCollect courseCollect) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            CourseCollect courseCollect1 = selectCourseCollectUserIdAnd(courseCollect.getUserId(), courseCollect.getCourseId(),courseCollect.getClassify(),courseCollect.getCourseDetailsId());
            if (courseCollect.getType() == 1) {
                if(courseCollect1==null){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    courseCollect.setCreateTime(df.format(new Date()));
                    courseCollect.setUpdateTime(courseCollect.getCreateTime());
                    baseMapper.insert(courseCollect);
                    if(courseCollect.getClassify()==2){
                        CourseDetails courseDetails = courseDetailsService.getById(courseCollect.getCourseDetailsId());
                        courseDetails.setGoodNum(courseDetails.getGoodNum()+1);
                        courseDetailsService.updateById(courseDetails);
                    }
                }else{
                    courseCollect1.setCourseDetailsSec(courseCollect.getCourseDetailsSec());
                    courseCollect1.setUpdateTime(DateUtils.format(new Date()));
                    baseMapper.updateById(courseCollect1);
                }
            } else {
                if(courseCollect1!=null){
                    baseMapper.deleteById(courseCollect1.getCourseCollectId());
                    if(courseCollect.getClassify()==2){
                        CourseDetails courseDetails = courseDetailsService.getById(courseCollect.getCourseDetailsId());
                        courseDetails.setGoodNum(courseDetails.getGoodNum()-1);
                        courseDetailsService.updateById(courseDetails);
                    }
                }
            }

            if(courseCollect.getClassify()==2){
                String redisCourseDetailsListName="selectCourseDetailsList_"+courseCollect.getCourseId()+"user_id"+courseCollect.getUserId();
                List<CourseDetails> courseDetailsList = null;
                courseDetailsList = courseDetailsDao.findByCourseId(courseCollect.getCourseId(), courseCollect.getUserId());
                redisUtils.set(redisCourseDetailsListName, JSONObject.toJSONString(courseDetailsList));

                String redisCourseDetailsNoUrlListName="selectCourseDetailsNoUrlList_"+courseCollect.getCourseId()+"user_id"+courseCollect.getUserId();
                List<CourseDetails> courseDetailsNotList = null;
                courseDetailsNotList = courseDetailsDao.findByCourseIdNotUrl(courseCollect.getCourseId(), courseCollect.getUserId());
                redisUtils.set(redisCourseDetailsNoUrlListName,JSONObject.toJSONString(courseDetailsNotList));
            }

            return Result.success("操作成功！");
        }catch (Exception e){
            e.printStackTrace();
            log.error("收藏短剧出错了！"+e.getMessage());
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙,请稍后再试！");
    }

    @Override
    public Result selectByUserId(Integer page, Integer limit, Long userId,Integer classify) {
        Page<Course> pages=new Page<>(page,limit);
        IPage<Course> courseIPage = baseMapper.selectCourseByCollect(pages, userId,classify);
        List<Course> courses = courseIPage.getRecords();
        if (courses != null && courses.size() > 0) {
            for (Course course : courses) {
                course.setCourseClassification(courseClassificationDao.selectById(course.getClassifyId()));
            }
        }
        return Result.success().put("data",courseIPage);
    }


    @Override
    public CourseCollect selectCourseCollectUserIdAnd(Long userId,Long courseId,Integer classify,Long courseDetailsId){
        return baseMapper.selectOne(new QueryWrapper<CourseCollect>().eq(courseDetailsId!=null,"course_details_id",courseDetailsId).eq("user_id",userId).eq("classify",classify).eq("course_id",courseId));
    }


}
