package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseCollect;

public interface CourseCollectService extends IService<CourseCollect> {

    Result insertCourseCollect(CourseCollect courseCollect);

    Result selectByUserId(Integer page, Integer limit, Long userId,Integer classify);

    CourseCollect selectCourseCollectUserIdAnd(Long userId,Long courseId,Integer classify,Long courseDetailsId);
}
