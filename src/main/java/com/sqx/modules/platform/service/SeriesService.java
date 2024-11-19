package com.sqx.modules.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.platform.entity.Series;

import java.io.Serializable;
import java.util.List;

/**
 * (Series)表服务接口
 *
 * @author makejava
 * @since 2024-11-07 18:57:42
 */
public interface SeriesService extends IService<Series> {

    Result selectSeriesPage(Page<Series> page, Series series);
    Result selectSeriesById(Serializable id);
    Result insertSeries(Series series);
    Result updateSeries(Series series);
    Result deleteSeries(List<Long> idList);

}
