package com.sqx.modules.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.CourseCutInvite;
import com.sqx.modules.course.service.CourseCutInviteService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseCutInvite)表控制层
 *
 * @author makejava
 * @since 2024-09-26 19:54:53
 */
@Api(value = "短剧砍剧邀请", tags = {"短剧砍剧邀请"})
@RestController
@RequestMapping("courseCutInvite")
public class CourseCutInviteController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseCutInviteService courseCutInviteService;

    /**
     * 分页查询所有数据
     *
     * @param page            分页对象
     * @param courseCutInvite 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<CourseCutInvite> page, CourseCutInvite courseCutInvite) {
        return success(this.courseCutInviteService.page(page, new QueryWrapper<>(courseCutInvite)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.courseCutInviteService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param courseCutInvite 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody CourseCutInvite courseCutInvite) {
        return success(this.courseCutInviteService.save(courseCutInvite));
    }

    /**
     * 修改数据
     *
     * @param courseCutInvite 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody CourseCutInvite courseCutInvite) {
        return success(this.courseCutInviteService.updateById(courseCutInvite));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.courseCutInviteService.removeByIds(idList));
    }
}
