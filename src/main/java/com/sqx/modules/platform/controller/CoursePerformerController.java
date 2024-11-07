package com.sqx.modules.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.platform.entity.CoursePerformer;
import com.sqx.modules.platform.service.CoursePerformerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CoursePerformer)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:43
 */
@RestController
@RequestMapping("coursePerformer")
public class CoursePerformerController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CoursePerformerService coursePerformerService;

    /**
     * 分页查询所有数据
     *
     * @param page            分页对象
     * @param coursePerformer 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<CoursePerformer> page, CoursePerformer coursePerformer) {
        return success(this.coursePerformerService.page(page, new QueryWrapper<>(coursePerformer)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.coursePerformerService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param coursePerformer 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody CoursePerformer coursePerformer) {
        return success(this.coursePerformerService.save(coursePerformer));
    }

    /**
     * 修改数据
     *
     * @param coursePerformer 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody CoursePerformer coursePerformer) {
        return success(this.coursePerformerService.updateById(coursePerformer));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.coursePerformerService.removeByIds(idList));
    }
}
