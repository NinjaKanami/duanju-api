package com.sqx.modules.course.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseCut;
import com.sqx.modules.course.service.CourseCutService;
import com.sqx.modules.course.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * (CourseCut)表控制层
 *
 * @author makejava
 * @since 2024-09-26 19:54:35
 */
@Api(value = "App短剧砍剧", tags = {"App短剧砍剧"})
@RestController
@RequestMapping("app/courseCut")
public class AppCourseCutController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseCutService courseCutService;

    @Login
    @GetMapping("/selectCourseCut")
    @ApiOperation("查询用户砍剧短剧信息")
    public Result selectCourseCollect(@Validated @RequestParam("cutId") Long cutId,
                                      @Validated @RequestAttribute("userId") Long userId) {
        return courseCutService.selectCourseCut(cutId, userId);
    }

    @Login
    @GetMapping("/selectCourseCutList")
    @ApiOperation("查询用户砍剧短剧信息列表")
    public Result selectCourseCutList(@Validated @RequestAttribute("userId") Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Result.success().put("data", courseCutService.list(new QueryWrapper<CourseCut>()
                .eq("user_id", userId).eq("cut_status", 1)
                .or()
                .eq("user_id", userId).eq("cut_status", 0).ge("deadline", sdf.format(System.currentTimeMillis()))
        ));
    }

    @Login
    @PostMapping("/insertCourseCut")
    @ApiOperation("新增砍剧短剧信息")
    public Result insertCourseCollect(@Validated @RequestParam("courseId") Long courseId,
                                      @Validated @RequestAttribute("userId") Long userId) {
        return courseCutService.insertCourseCut(courseId, userId);
    }

}
