package com.sqx.modules.box.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.box.entity.BoxPoint;

/**
 * (BoxPoint)表服务接口
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
public interface BoxPointService extends IService<BoxPoint> {

    /**
     * 获得积分
     *
     * @param userId   用户
     * @param courseId 短剧
     * @param size     集数
     * @return Result
     */
    Result getPoints(Long userId, Long courseId, int size);

}
