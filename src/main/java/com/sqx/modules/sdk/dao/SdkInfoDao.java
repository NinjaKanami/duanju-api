package com.sqx.modules.sdk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.sdk.entity.SdkInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-20
 */
@Mapper
public interface SdkInfoDao extends BaseMapper<SdkInfo> {

    IPage<SdkInfo> getSdkPage(@Param("pages") Page<SdkInfo> pages, @Param("sdkInfo") SdkInfo sdkInfo);


    List<SdkInfo> getSdkList(@Param("sdkInfo") SdkInfo sdkInfo);

}
