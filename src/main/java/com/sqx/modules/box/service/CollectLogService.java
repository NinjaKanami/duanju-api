package com.sqx.modules.box.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.box.entity.CollectLog;

import java.util.List;

/**
 * (CollectLog)表服务接口
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
public interface CollectLogService extends IService<CollectLog> {
    /**
     * 查询待同步记录with phone
     *
     * @return List<CollectLog>
     */
    List<CollectLog> selectSyncCollectLog();
}
