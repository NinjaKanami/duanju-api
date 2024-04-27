package com.sqx.modules.sdk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.sdk.entity.SdkType;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-20
 */
public interface SdkTypeService extends IService<SdkType> {

    Result saveSdkType(SdkType sdkType);

    IPage<SdkType> getSdkTypeList(Integer page, Integer limit, SdkType sdkType);
}
