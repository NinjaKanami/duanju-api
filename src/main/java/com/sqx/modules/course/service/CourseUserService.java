package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseUser;


public interface CourseUserService extends IService<CourseUser> {

    void updateTime(Long courseId);

    Result selectCourseUser(Integer page, Integer limit, Long userId);

    Result selectLatelyCourse(Integer page, Integer limit, Long userId);

    Result insertCourseUser(CourseUser courseUser);
}
