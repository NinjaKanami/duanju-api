package com.sqx.modules.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.service.SeriesService;
import io.swagger.annotations.Api;
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
@Api(value = "剧单后台管理", tags = {"剧单后台管理"})
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
    @ApiOperation("分页查询所有数据")
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
    @ApiOperation("通过主键查询单条数据")
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
    @ApiOperation("新增数据")
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
    @ApiOperation("修改数据")
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
    @ApiOperation("删除数据")
    @DeleteMapping
    public Result delete(@RequestParam("idList") List<Long> idList) {
        return seriesService.deleteSeries(idList);
    }
}
