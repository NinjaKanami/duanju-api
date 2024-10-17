package com.sqx.modules.box.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.box.dao.BoxDao;
import com.sqx.modules.box.entity.Box;
import com.sqx.modules.box.service.BoxService;
import org.springframework.stereotype.Service;

/**
 * (Box)表服务实现类
 *
 * @author makejava
 * @since 2024-10-17 15:49:36
 */
@Service("boxService")
public class BoxServiceImpl extends ServiceImpl<BoxDao, Box> implements BoxService {

}
