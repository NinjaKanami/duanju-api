package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.BoxLogDao;
import com.sqx.modules.box.entity.BoxLog;
import com.sqx.modules.box.service.BoxLogService;
import org.springframework.stereotype.Service;

/**
 * (BoxLog)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:33
 */
@Service("boxLogService")
public class BoxLogServiceImpl extends ServiceImpl<BoxLogDao, BoxLog> implements BoxLogService {

}
