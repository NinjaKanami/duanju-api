package com.sqx.modules.platform.controller.app;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.performer.service.PerformerService;
import com.sqx.modules.performer.vo.AppPerformerVO;
import com.sqx.modules.platform.service.CourseSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private PerformerService performerService;


    /**
     * 搜索课程接口
     * <p>
     * 该接口用于用户根据多种条件搜索课程信息可以通过关键词、地区、分类、平台等多个维度进行筛选
     * 同时支持排序功能，以满足用户不同的搜索需求
     *
     * @param page       分页对象，用于封装分页信息
     * @param keyword    关键词，用于搜索短剧名称、描述等内容
     * @param areaId     地区ID，用于筛选属于特定地区的课程
     * @param classifyId 分类ID，用于筛选属于特定分类的课程
     * @param platformId 平台ID，用于筛选属于特定平台的课程
     * @param isPrice    价格状态，用于筛选免费或付费课程
     * @param sortField  排序字段，指定按照哪个字段进行排序
     * @param sort       排序方式，升序或降序
     * @return 返回搜索结果，包含课程列表和分页信息
     */
    @ApiOperation(value = "搜索短剧接口", notes = "搜索短剧接口", httpMethod = "GET")
    @GetMapping("/searchCourse")
    public Result searchCourse(Page<Course> page, String keyword, Long areaId, Long classifyId, Long platformId, Long isPrice, String sortField, Integer sort) {
        return courseSearchService.searchCourse(page, keyword, areaId, classifyId, platformId, isPrice, sortField, sort);
    }

    /**
     * 搜索课程&接口
     * <p>
     * 该接口用于用户根据多种条件搜索课程信息可以通过关键词、地区、分类、平台等多个维度进行筛选
     * 同时支持排序功能，以满足用户不同的搜索需求
     *
     * @param page       分页对象，用于封装分页信息
     * @param keyword    关键词，用于搜索短剧&演员名称、描述等内容
     * @param areaId     地区ID，用于筛选属于特定地区的课程
     * @param classifyId 分类ID，用于筛选属于特定分类的课程
     * @param platformId 平台ID，用于筛选属于特定平台的课程
     * @param isPrice    价格状态，用于筛选免费或付费课程
     * @param sortField  排序字段，指定按照哪个字段进行排序
     * @param sort       排序方式，升序或降序
     * @return 返回搜索结果，包含短句列表（data）和分页信息 以及演员信息（performerList）
     */
    @ApiOperation(value = "搜索短剧&演员接口", notes = "搜索短剧&演员接口", httpMethod = "GET")
    @GetMapping("/searchCourseAndPerformer")
    public Result searchCourseAndPerformer(Page<Course> page, String keyword, Long areaId, Long classifyId, Long platformId, Long isPrice, String sortField, Integer sort) {
        Result result = courseSearchService.searchCourse(page, keyword, areaId, classifyId, platformId, isPrice, sortField, sort);
        List<AppPerformerVO> appPerformerVOList = performerService.userSearchPerformer(keyword);
        result.put("performerList", appPerformerVOList);
        return result;
    }
}
