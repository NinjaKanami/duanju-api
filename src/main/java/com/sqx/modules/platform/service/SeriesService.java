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

    /**
     * 根据条件分页查询Series信息
     *
     * @param page   分页对象，包含分页参数如当前页码和每页记录数
     * @param series 查询条件对象，可包含需要匹配的Series属性值
     * @return 查询结果，包括Series列表和总记录数等信息
     */
    Result selectSeriesPage(Page<Series> page, Series series);

    /**
     * 根据ID查询Series信息
     *
     * @param id Series的唯一标识符
     * @return 查询结果，包括匹配的Series详细信息
     */
    Result selectSeriesById(Serializable id);

    /**
     * 根据系列ID选择系列信息
     * 此方法用于通过特定ID检索系列信息，常用于数据查询操作
     *
     * @param userId 用户ID，用于标识请求的用户，以便于权限验证或个性化处理
     * @param id     系列ID，可以是任何实现了Serializable接口的类型，用于指定需要查询的系列
     * @return 返回一个Result对象，包含查询结果和相关状态信息
     */
    Result selectSeriesById(Long userId, Serializable id);

    /**
     * 新增Series信息
     *
     * @param series 需要插入的Series对象，包括所有需要记录的属性值
     * @return 插入结果，包括操作状态和受影响的记录数等信息
     */
    Result insertSeries(Series series);

    /**
     * 更新Series信息
     *
     * @param series 需要更新的Series对象，包括所有需要修改的属性值
     * @return 更新结果，包括操作状态和受影响的记录数等信息
     */
    Result updateSeries(Series series);

    /**
     * 根据ID列表删除Series信息
     *
     * @param idList 需要删除的Series的ID列表
     * @return 删除结果，包括操作状态和受影响的记录数等信息
     */
    Result deleteSeries(List<Long> idList);
}
