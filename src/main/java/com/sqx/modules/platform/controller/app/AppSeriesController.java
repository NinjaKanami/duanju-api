package com.sqx.modules.platform.controller.app;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.OptionalLogin;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.service.SeriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * App剧单
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
@Api(value = "app/series", tags = {"App剧单"})
@RestController
@RequestMapping("app/series")
public class AppSeriesController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private SeriesService seriesService;

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param series 查询实体
     * @return 所有数据
     */
    @ApiOperation(value = "分页查询所有数据", notes = "分页查询所有数据", httpMethod = "GET")
    //@Login
    @GetMapping
    public Result selectAll(Page<Series> page, Series series) {
        return seriesService.selectSeriesPage(page, series);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Serializable", name = "id", value = "主键", required = true)
    })
    @ApiOperation(value = "通过主键查询单条数据", notes = "通过主键查询单条数据", httpMethod = "GET")
    @OptionalLogin
    @GetMapping("{id}")
    public Result selectOne(@RequestAttribute(required = false) Long userId, @PathVariable Serializable id) {
        return seriesService.selectSeriesById(userId, id);
    }

}
