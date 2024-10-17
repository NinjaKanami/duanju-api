package com.sqx.modules.box.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.box.entity.BoxItem;
import com.sqx.modules.box.service.BoxItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (BoxItem)表控制层
 *
 * @author makejava
 * @since 2024-10-17 15:49:35
 */
@RestController
@RequestMapping("boxItem")
public class BoxItemController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private BoxItemService boxItemService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param boxItem 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<BoxItem> page, BoxItem boxItem) {
        return success(this.boxItemService.page(page, new QueryWrapper<>(boxItem)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.boxItemService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param boxItem 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody BoxItem boxItem) {
        return success(this.boxItemService.save(boxItem));
    }

    /**
     * 修改数据
     *
     * @param boxItem 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody BoxItem boxItem) {
        return success(this.boxItemService.updateById(boxItem));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.boxItemService.removeByIds(idList));
    }
}
