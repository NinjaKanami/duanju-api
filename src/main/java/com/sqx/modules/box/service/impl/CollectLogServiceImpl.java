package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.CollectLogDao;
import com.sqx.modules.box.entity.CollectLog;
import com.sqx.modules.box.service.CollectLogService;
import org.springframework.stereotype.Service;

/**
 * (CollectLog)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Service("collectLogService")
public class CollectLogServiceImpl extends ServiceImpl<CollectLogDao, CollectLog> implements CollectLogService {

}
