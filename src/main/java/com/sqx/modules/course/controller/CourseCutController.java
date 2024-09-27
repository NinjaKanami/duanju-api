package com.sqx.modules.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.entity.CourseCut;
import com.sqx.modules.course.service.CourseCutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseCut)表控制层
 *
 * @author makejava
 * @since 2024-09-26 19:54:35
 */
@Api(value = "短剧砍剧", tags = {"短剧砍剧"})
@RestController
@RequestMapping("courseCut")
public class CourseCutController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CourseCutService courseCutService;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param courseCut 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<CourseCut> page, CourseCut courseCut) {
        return success(this.courseCutService.page(page, new QueryWrapper<>(courseCut)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.courseCutService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param courseCut 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody CourseCut courseCut) {
        return success(this.courseCutService.save(courseCut));
    }

    @Login
    @PostMapping("/insertCourseCut")
    @ApiOperation("砍剧短剧信息")
    public Result insertCourseCollect(@RequestParam("courseId") Long courseId,@RequestParam("userId") Long userId){
        CourseCut courseCut = new CourseCut();
        courseCut.setCourseId(courseId);
        courseCut.setUserId(userId);
        courseCut.setCutStatus(0);
        courseCut.setInviteCount(0);
        courseCut.setInvitedCount(0);
        courseCut.setDeadline("2099-12-31 23:59:59");
        courseCut.setCreateTime(System.currentTimeMillis()+"");
        courseCut.setUpdateTime(System.currentTimeMillis()+"");
        return Result.success().put("data",courseCutService.save(courseCut));
    }

    /**
     * 修改数据
     *
     * @param courseCut 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody CourseCut courseCut) {
        return success(this.courseCutService.updateById(courseCut));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.courseCutService.removeByIds(idList));
    }
}
