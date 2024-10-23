package com.sqx.modules.box.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.entity.BoxOnline;
import org.springframework.stereotype.Service;

/**
 * (BoxOnline)表服务接口
 *
 * @author makejava
 * @since 2024-10-23 13:08:05
 */
public interface BoxOnlineService extends IService<BoxOnline> {

    /**
     * 更新在线时间
     *
     * @param userId 用户
     * @param minute 时间
     * @return 结果
     */
    Result updateOnline(Long userId, int minute);
}
