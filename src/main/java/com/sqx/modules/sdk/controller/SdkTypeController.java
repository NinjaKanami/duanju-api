package com.sqx.modules.sdk.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.sdk.entity.SdkType;
import com.sqx.modules.sdk.service.SdkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 卡密类型
 * </p>
 *
 * @author wuchang
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/admin/sdkType/")
public class SdkTypeController {
    @Autowired
    private SdkTypeService sdkTypeService;

    /**
     * 添加或修改卡密类型
     */
    @PostMapping("saveSdkType")
    public Result saveSdkType(SdkType sdkType) {
        return sdkTypeService.saveSdkType(sdkType);
    }

    /**
     * 获取卡密类型列表
     */
    @GetMapping("getSdkTypeList")
    public Result getSdkTypeList(Integer page, Integer limit, SdkType sdkType) {
        return Result.success().put("data", sdkTypeService.getSdkTypeList(page, limit, sdkType));
    }

    /**
     * 删除卡密类型
     */
    @GetMapping("deleteSdkType")
    public Result deleteSdkType(Long typeId) {
        return sdkTypeService.removeById(typeId) ? Result.success() : Result.error();
    }
}

