package com.sqx.modules.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.platform.entity.SeriesCourse;
import com.sqx.modules.platform.service.SeriesCourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (SeriesCourse)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
@RestController
@RequestMapping("seriesCourse")
public class SeriesCourseController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private SeriesCourseService seriesCourseService;

    /**
     * 分页查询所有数据
     *
     * @param page         分页对象
     * @param seriesCourse 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<SeriesCourse> page, SeriesCourse seriesCourse) {
        return success(this.seriesCourseService.page(page, new QueryWrapper<>(seriesCourse)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.seriesCourseService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param seriesCourse 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody SeriesCourse seriesCourse) {
        return success(this.seriesCourseService.save(seriesCourse));
    }

    /**
     * 修改数据
     *
     * @param seriesCourse 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody SeriesCourse seriesCourse) {
        return success(this.seriesCourseService.updateById(seriesCourse));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.seriesCourseService.removeByIds(idList));
    }
}
