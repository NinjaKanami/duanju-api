package com.sqx.modules.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.platform.entity.CourseArea;
import com.sqx.modules.platform.service.CourseAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseArea)表控制层
 *
 * @author makejava
 * @since 2024-11-07 18:57:40
 */
@Api(value = "短剧地区管理", tags = {"短剧地区管理"})
@RestController
@RequestMapping("courseArea")
public class CourseAreaController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseAreaService courseAreaService;
    @Resource
    private CourseService courseService;

    /**
     * 分页查询所有数据
     *
     * @param page       分页对象
     * @param courseArea 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping
    public R selectAll(Page<CourseArea> page, CourseArea courseArea) {
        return success(this.courseAreaService.page(page, new QueryWrapper<>(courseArea).orderByAsc("sort")));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.courseAreaService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param courseArea 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R insert(@RequestBody CourseArea courseArea) {
        return success(this.courseAreaService.save(courseArea));
    }

    /**
     * 修改数据
     *
     * @param courseArea 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R update(@RequestBody CourseArea courseArea) {
        return success(this.courseAreaService.updateById(courseArea));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        int count = courseService.count(new QueryWrapper<Course>().in("area_id", idList));
        if (count > 0) {
            return failed("该地区下有短剧，无法删除");
        }
        return success(this.courseAreaService.removeByIds(idList));
    }
}
