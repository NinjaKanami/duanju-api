package com.sqx.modules.course.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.service.CourseCollectService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "app短剧收藏", tags = {"app短剧收藏"})
@RequestMapping(value = "/app/courseCollect")
public class AppCourseCollectController extends AbstractController {

    @Autowired
    private CourseCollectService courseCollectService;

    @Login
    @PostMapping("/insertCourseCollect")
    @ApiOperation("app收藏短剧信息")
    public Result insertCourseCollect(@RequestBody CourseCollect courseCollect,@RequestAttribute("userId") Long userId){
        courseCollect.setUserId(userId);
        return courseCollectService.insertCourseCollect(courseCollect);
    }

    @Login
    @GetMapping("/selectByUserId")
    @ApiOperation("app查询收藏短剧信息")
    public Result selectByUserId(Integer page, Integer limit,@RequestAttribute("userId") Long userId,Integer classify){
        return courseCollectService.selectByUserId(page,limit,userId,classify);
    }

    @Login
    @GetMapping("/selectPayCourse")
    @ApiOperation("app查询收藏短剧信息")
    public Result selectPayCourse(Long courseId,@RequestAttribute Long userId){
        return Result.success().put("data",courseCollectService.count(new QueryWrapper<CourseCollect>()
                .eq("course_id",courseId).eq("user_id",userId).eq("classify",4)));
    }


}
