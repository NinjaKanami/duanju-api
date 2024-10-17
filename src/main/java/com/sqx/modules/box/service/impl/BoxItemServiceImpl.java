package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.BoxItemDao;
import com.sqx.modules.box.entity.BoxItem;
import com.sqx.modules.box.service.BoxItemService;
import org.springframework.stereotype.Service;

/**
 * (BoxItem)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:35
 */
@Service("boxItemService")
public class BoxItemServiceImpl extends ServiceImpl<BoxItemDao, BoxItem> implements BoxItemService {

}
