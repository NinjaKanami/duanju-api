package com.sqx.modules.box.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.entity.Collect;

/**
 * (Collect)表服务接口
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
public interface CollectService extends IService<Collect> {

    /**
     * 合成
     *
     * @param userId 用户Id
     * @param count  数量
     * @return 结果
     */
    Result synthesise(Long userId, int count);
}
