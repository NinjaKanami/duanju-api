package com.sqx.modules.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.platform.entity.CourseScore;
import com.sqx.modules.platform.service.CourseScoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseScore)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:41
 */
@RestController
@RequestMapping("courseScore")
public class CourseScoreController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseScoreService courseScoreService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param courseScore 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<CourseScore> page, CourseScore courseScore) {
        return success(this.courseScoreService.page(page, new QueryWrapper<>(courseScore)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.courseScoreService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param courseScore 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody CourseScore courseScore) {
        return success(this.courseScoreService.save(courseScore));
    }

    /**
     * 修改数据
     *
     * @param courseScore 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody CourseScore courseScore) {
        return success(this.courseScoreService.updateById(courseScore));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.courseScoreService.removeByIds(idList));
    }
}
