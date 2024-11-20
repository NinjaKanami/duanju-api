package com.sqx.modules.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
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
 * (Series)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
@Api(value = "series", tags = {"后台剧单"})
@RestController
@RequestMapping("series")
public class SeriesController extends ApiController {
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
    @GetMapping("{id}")
    public Result selectOne(@PathVariable Serializable id) {
        return seriesService.selectSeriesById(id);
    }

    /**
     * 新增数据
     *
     * @param series 实体对象
     * @return 新增结果
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Series", name = "series", value = "实体对象", required = true)
    })
    @ApiOperation(value = "新增数据", notes = "新增数据", httpMethod = "POST")
    @PostMapping
    public Result insert(@RequestBody Series series) {
        return seriesService.insertSeries(series);
    }

    /**
     * 修改数据
     *
     * @param series 实体对象
     * @return 修改结果
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Series", name = "series", value = "实体对象", required = true)
    })
    @ApiOperation(value = "修改数据", notes = "修改数据", httpMethod = "PUT")
    @PutMapping
    public Result update(@RequestBody Series series) {
        return seriesService.updateSeries(series);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "List<Long>", name = "idList", value = "主键结合", required = true)
    })
    @ApiOperation(value = "删除数据", notes = "删除数据", httpMethod = "DELETE")
    @DeleteMapping
    public Result delete(@RequestParam("idList") List<Long> idList) {
        return seriesService.deleteSeries(idList);
    }
}
