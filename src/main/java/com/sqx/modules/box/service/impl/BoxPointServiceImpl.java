package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.BoxPointDao;
import com.sqx.modules.box.entity.BoxPoint;
import com.sqx.modules.box.service.BoxPointService;
import org.springframework.stereotype.Service;

/**
 * (BoxPoint)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Service("boxPointService")
public class BoxPointServiceImpl extends ServiceImpl<BoxPointDao, BoxPoint> implements BoxPointService {

}
