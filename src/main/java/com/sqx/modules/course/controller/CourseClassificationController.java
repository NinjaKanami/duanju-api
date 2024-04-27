package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseClassification;
import com.sqx.modules.course.service.CourseClassificationService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "短剧分类信息", tags = {"短剧分类信息"})
@RequestMapping(value = "/courseClassification")
public class CourseClassificationController extends AbstractController {
    @Autowired
    private CourseClassificationService courseClassificationService;

    @GetMapping("/selectCourseClassification")
    @ApiOperation("查询短剧分类信息")
    public Result selectCourseClassification(Integer page, Integer limit, String classificationName) {
        return courseClassificationService.selectCourseClassification(page, limit, classificationName);
    }

    @PostMapping("/insertCourseClassification")
    @ApiOperation("添加短剧分类信息")
    public Result insertCourseClassification(@RequestBody CourseClassification courseClassification) {
        return courseClassificationService.insertCourseClassification(courseClassification);
    }

    @PostMapping("/updateCourseClassification")
    @ApiOperation("修改短剧分类信息")
    public Result updateCourseClassification(@RequestBody CourseClassification courseClassification) {
        return courseClassificationService.updateCourseClassification(courseClassification);
    }

    @GetMapping("/updateDelete")
    @ApiOperation("假删除")
    public Result updateDelete(Long id) {
        return courseClassificationService.updateDelete(id);
    }

    @GetMapping("/selectCourseClassificationById")
    @ApiOperation("根据id查询短剧分类信息")
    public CourseClassification selectCourseClassificationById(Long id) {
        return courseClassificationService.selectCourseClassificationById(id);
    }
}
