package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.CollectDao;
import com.sqx.modules.box.entity.Collect;
import com.sqx.modules.box.service.CollectService;
import org.springframework.stereotype.Service;

/**
 * (Collect)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:34
 */
@Service("collectService")
public class CollectServiceImpl extends ServiceImpl<CollectDao, Collect> implements CollectService {

}
