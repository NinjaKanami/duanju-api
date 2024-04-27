package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseComment;

public interface CourseCommentService extends IService<CourseComment> {
    Result insertCourseComment(CourseComment courseComment);

    Result updateGoodsNum(Long courseCommentId, Long userId);

    Result selectCourseComment(Integer page, Integer limit, Long courseId,Long userId);

    Result deleteCourseComment(Long courseCommentId);

    /**
     * 查询用户的评论
     * @param userId
     * @return
     */
    Result selectCourseCommentUser(Integer page, Integer limit,Long userId);
}
