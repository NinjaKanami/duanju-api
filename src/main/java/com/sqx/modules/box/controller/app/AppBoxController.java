package com.sqx.modules.box.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.box.entity.Box;
import com.sqx.modules.box.service.BoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Box)表控制层
 *
 * @author makejava
 * @since 2024-10-17 15:49:35
 */
@Api(value = "App用户盲盒", tags = {"App用户盲盒"})
@RestController
@RequestMapping("app/box")
public class AppBoxController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private BoxService boxService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param box  查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<Box> page, Box box) {
        return success(this.boxService.page(page, new QueryWrapper<>(box)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @Login
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.boxService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param box 实体对象
     * @return 新增结果
     */
    @Login
    @PostMapping
    public R insert(@RequestBody Box box) {
        return success(this.boxService.save(box));
    }

    /**
     * 修改数据
     *
     * @param box 实体对象
     * @return 修改结果
     */
    @Login
    @PutMapping
    public R update(@RequestBody Box box) {
        return success(this.boxService.updateById(box));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Login
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.boxService.removeByIds(idList));
    }

    @Login
    @GetMapping("/selectBoxCollection")
    @ApiOperation("App查询用户盲盒信息")
    public Result selectBoxCollection(@RequestAttribute Long userId) {
        return this.boxService.selectBoxCollection(userId);
    }


    @Login
    @PostMapping("/openBox")
    @ApiOperation("开盒")
    public Result openBox(@RequestAttribute Long userId, @RequestParam int count) {
        return this.boxService.openBox(userId, count);
    }
}
