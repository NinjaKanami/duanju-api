package com.sqx.modules.box.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.box.entity.Collect;
import com.sqx.modules.box.service.CollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (Collect)表控制层
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Api(value = "App用户藏品", tags = {"App用户藏品"})
@RestController
@RequestMapping("app/collect")
public class AppCollectController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private CollectService collectService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param collect 查询实体
     * @return 所有数据
     */
    @Profile("!prod")
    @Login
    @GetMapping
    public R selectAll(Page<Collect> page, Collect collect) {
        return success(this.collectService.page(page, new QueryWrapper<>(collect)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @Profile("!prod")
    @Login
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.collectService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param collect 实体对象
     * @return 新增结果
     */
    @Profile("!prod")
    @Login
    @PostMapping
    public R insert(@RequestBody Collect collect) {
        return success(this.collectService.save(collect));
    }

    /**
     * 修改数据
     *
     * @param collect 实体对象
     * @return 修改结果
     */
    @Profile("!prod")
    @Login
    @PutMapping
    public R update(@RequestBody Collect collect) {
        return success(this.collectService.updateById(collect));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Profile("!prod")
    @Login
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.collectService.removeByIds(idList));
    }


    @Login
    @PostMapping("/synthesise")
    @ApiOperation("合成")
    public Result synthesise(@RequestAttribute Long userId, Integer count) {
        return this.collectService.synthesise(userId, count);
    }
}
