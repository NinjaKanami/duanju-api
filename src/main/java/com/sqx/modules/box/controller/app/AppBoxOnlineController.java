package com.sqx.modules.box.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.box.entity.BoxOnline;
import com.sqx.modules.box.service.BoxOnlineService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (BoxOnline)表控制层
 *
 * @author makejava
 * @since 2024-10-23 13:08:05
 */
@RestController
@RequestMapping("app/boxOnline")
public class AppBoxOnlineController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private BoxOnlineService boxOnlineService;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param boxOnline 查询实体
     * @return 所有数据
     */
    @Profile("!prod")
    @Login
    @GetMapping
    public R selectAll(Page<BoxOnline> page, BoxOnline boxOnline) {
        return success(this.boxOnlineService.page(page, new QueryWrapper<>(boxOnline)));
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
        return success(this.boxOnlineService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param boxOnline 实体对象
     * @return 新增结果
     */
    @Profile("!prod")
    @Login
    @PostMapping
    public R insert(@RequestBody BoxOnline boxOnline) {
        return success(this.boxOnlineService.save(boxOnline));
    }

    /**
     * 修改数据
     *
     * @param boxOnline 实体对象
     * @return 修改结果
     */
    @Profile("!prod")
    @Login
    @PutMapping
    public R update(@RequestBody BoxOnline boxOnline) {
        return success(this.boxOnlineService.updateById(boxOnline));
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
        return success(this.boxOnlineService.removeByIds(idList));
    }

    /**
     * 更新在线时间
     *
     * @param userId 用户
     * @return 单条数据
     */
    @Login
    @PostMapping("/updateOnline")
    public Result updateOnline(@RequestAttribute Long userId, Integer minute) {

        return this.boxOnlineService.updateOnline(userId, minute);

    }
}
