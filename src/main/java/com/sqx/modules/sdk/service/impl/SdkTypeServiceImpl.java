package com.sqx.modules.sdk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sdk.dao.SdkTypeDao;
import com.sqx.modules.sdk.entity.SdkType;
import com.sqx.modules.sdk.service.SdkTypeService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @autho wuchang
 * @since 2023-02-20
 */
@Service
public class SdkTypeServiceImpl extends ServiceImpl<SdkTypeDao, SdkType> implements SdkTypeService {

    @Override
    public Result saveSdkType(SdkType sdkType) {
        if (sdkType.getTypeId() != null) {
            baseMapper.updateById(sdkType);
            return Result.success();
        } else {
            sdkType.setCreateTime(DateUtils.format(new Date()));
            baseMapper.insert(sdkType);
            return Result.success();
        }


    }

    @Override
    public IPage<SdkType> getSdkTypeList(Integer page, Integer limit, SdkType sdkType) {
        Page<SdkType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(sdkType).orderByDesc("create_time"));


    }
}
