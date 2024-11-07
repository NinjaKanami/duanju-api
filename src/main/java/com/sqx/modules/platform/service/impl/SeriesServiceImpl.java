package com.sqx.modules.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.platform.dao.SeriesDao;
import com.sqx.modules.platform.entity.Series;
import com.sqx.modules.platform.service.SeriesService;
import org.springframework.stereotype.Service;

/**
 * (Series)表服务实现类
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
@Service("seriesService")
public class SeriesServiceImpl extends ServiceImpl<SeriesDao, Series> implements SeriesService {

}
