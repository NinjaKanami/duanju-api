package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.service.CourseDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@Api(value = "短剧视频信息", tags = {"短剧视频信息"})
@RequestMapping(value = "/courseDetails")
public class CourseDetailsController {
    @Autowired
    private CourseDetailsService courseDetailsService;

    @PostMapping("/insertCourseDetails")
    @ApiOperation("添加短剧视频信息")
    public Result insertCourseDetails(@RequestBody CourseDetails courseDetails) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        courseDetails.setCreateTime(sdf.format(new Date()));
        courseDetailsService.insert(courseDetails);
        return Result.success();
    }

    @PostMapping("/updateCourseDetails")
    @ApiOperation("修改短剧视频信息")
    public Result updateCourseDetails(@RequestBody CourseDetails courseDetails) {
        courseDetailsService.updateCourseDetails(courseDetails);
        return Result.success();
    }

    @PostMapping("/deleteCourseDetails")
    @ApiOperation("删除短剧视频信息")
    public Result deleteCourseDetails(String ids) {
        courseDetailsService.deleteCourseDetails(ids);
        return Result.success();
    }

    /**
     * 集导入列表--导入
     * @param file 上传文件
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "集导入列表--导入")
    @PostMapping(value = "/courseDetailsListExcelIn")
    public Result courseDetailsListExcelIn(@ApiParam(name = "file", value = "excel文件") @RequestPart MultipartFile file, Long courseId) throws Exception {
        try {
            if (file == null) {
                return Result.error("文件不能为空！");
            }
            return courseDetailsService.courseDetailsListExcelIn(file,courseId);
        } catch (Exception e) {
            log.error("集导入列表--导入异常：", e);
        }

        // 返回结果
        return Result.error("导入失败");
    }


}
