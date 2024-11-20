package com.sqx.modules.platform.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.platform.entity.CourseScore;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.service.CourseScoreService;
import com.sqx.modules.platform.service.CourseSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * App短剧榜/搜索
 *
 * @author makejava
 * @since 2024-11-07 18:57:41
 */

@Api(value = "app/courseSearch", tags = {"App短剧榜/搜索"})
@RestController
@RequestMapping("app/courseSearch")
public class AppCourseSearchController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseSearchService courseSearchService;


    /**
     * 搜索课程接口
     * <p>
     * 该接口用于用户根据多种条件搜索课程信息可以通过关键词、地区、分类、平台等多个维度进行筛选
     * 同时支持排序功能，以满足用户不同的搜索需求
     *
     * @param page       分页对象，用于封装分页信息
     * @param keyword    关键词，用于搜索课程名称、描述等内容
     * @param areaId     地区ID，用于筛选属于特定地区的课程
     * @param classifyId 分类ID，用于筛选属于特定分类的课程
     * @param platformId 平台ID，用于筛选属于特定平台的课程
     * @param isPrice    价格状态，用于筛选免费或付费课程
     * @param sortField  排序字段，指定按照哪个字段进行排序
     * @param sort       排序方式，升序或降序
     * @return 返回搜索结果，包含课程列表和分页信息
     */
    @ApiOperation(value = "搜索课程接口", notes = "搜索课程接口", httpMethod = "GET")
    @GetMapping
    public Result searchCourse(Page<Course> page, String keyword, Long areaId, Long classifyId, Long platformId, Long isPrice, String sortField, Integer sort) {
        return courseSearchService.searchCourse(page, keyword, areaId, classifyId, platformId, isPrice, sortField, sort);
    }
}
