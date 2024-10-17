package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.CollectPointDao;
import com.sqx.modules.box.entity.CollectPoint;
import com.sqx.modules.box.service.CollectPointService;
import org.springframework.stereotype.Service;

/**
 * (CollectPoint)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:35
 */
@Service("collectPointService")
public class CollectPointServiceImpl extends ServiceImpl<CollectPointDao, CollectPoint> implements CollectPointService {

}
