package com.sqx.modules.sdk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.sdk.entity.SdkInfo;
import com.sqx.modules.utils.excel.ExcelData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-20
 */
public interface SdkInfoService extends IService<SdkInfo> {

    Result saveSdkInfo(Long typeId, Integer num,Long sysUserId);

    IPage<SdkInfo> getSdkList(Integer page, Integer limit, SdkInfo sdkInfo);

    Result sdkExchange(Long userId, String sdkContent);

    ExcelData excelSdkList(SdkInfo sdkInfo);
}
