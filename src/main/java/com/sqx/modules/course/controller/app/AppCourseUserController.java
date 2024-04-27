package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "我的短剧", tags = {"我的短剧"})
@RequestMapping(value = "/app/CourseUser")
public class AppCourseUserController extends AbstractController {
    @Autowired
    private CourseUserService courseUserService;

    @Login
    @GetMapping("/selectCourseUser")
    @ApiOperation("App我的短剧")
    public Result selectCourseUser(Integer page, Integer limit,@RequestAttribute Long userId) {
        return courseUserService.selectCourseUser(page, limit, userId);
    }

    @Login
    @GetMapping("/updateTime")
    @ApiOperation("修改时间")
    public void updateTime(Long courseId) {
        courseUserService.updateTime(courseId);
    }

    @Login
    @GetMapping("/selectLatelyCourse")
    @ApiOperation("最近学习")
    public Result selectLatelyCourse(Integer page, Integer limit, Long userId) {
        return courseUserService.selectLatelyCourse(page, limit, userId);
    }




}
