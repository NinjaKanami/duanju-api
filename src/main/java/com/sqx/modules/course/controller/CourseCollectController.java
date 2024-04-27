package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.service.CourseCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "短剧收藏", tags = {"短剧收藏"})
@RequestMapping(value = "/courseCollect")
@AllArgsConstructor
public class CourseCollectController {

    private CourseCollectService courseCollectService;

    @GetMapping("/selectByUserId")
    @ApiOperation("查询收藏短剧信息")
    public Result selectByUserId(Integer page, Integer limit, Long userId,Integer classify){
        return courseCollectService.selectByUserId(page,limit,userId,classify);
    }
}
