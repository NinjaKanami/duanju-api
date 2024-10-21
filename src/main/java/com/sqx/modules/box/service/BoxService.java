package com.sqx.modules.box.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.entity.Box;

/**
 * (Box)表服务接口
 *
 * @author makejava
 * @since 2024-10-17 15:49:36
 */
public interface BoxService extends IService<Box> {

    /**
     * 查询 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
     *
     * @param userId 用户ID
     * @return 盲盒 青龙 龙鳞 记录 最大发行 剩余数量
     */
    Result selectBoxCollection(Long userId);

    /**
     * 开盒
     * @param userId 用户Id
     * @param count 数量
     * @return 开盒结果
     */
    Result openBox(Long userId, int count);
}
