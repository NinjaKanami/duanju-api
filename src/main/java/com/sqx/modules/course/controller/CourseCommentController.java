package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseComment;
import com.sqx.modules.course.service.CourseCommentService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "短剧信息", tags = {"短剧评论信息"})
@RequestMapping(value = "/courseComment")
public class CourseCommentController extends AbstractController {
    @Autowired
    private CourseCommentService courseCommentService;

    @GetMapping("/selectCourseComment")
    @ApiOperation("查看评论")
    public Result selectCourseComment(Integer page, Integer limit, Long courseId) {
        return courseCommentService.selectCourseComment(page, limit, courseId,1L);
    }

    @PostMapping("/insertCourseComment")
    @ApiOperation("添加评论")
    public Result insertCourseComment(@RequestBody CourseComment courseComment){
        return courseCommentService.insertCourseComment(courseComment);
    }
    @PostMapping("/deleteCourseComment")
    @ApiOperation("删除评论")
    public Result deleteCourseComment(Long courseCommentId) {
        return courseCommentService.deleteCourseComment(courseCommentId);
    }
    @GetMapping("/selectCourseCommentUser")
    @ApiOperation("我的评论")
    public Result selectCourseCommentUser(Integer page, Integer limit,Long userId) {
        return courseCommentService.selectCourseCommentUser(page,limit,userId);
    }


}
