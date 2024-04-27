package com.sqx.modules.sdk.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.sdk.entity.SdkInfo;
import com.sqx.modules.sdk.service.SdkInfoService;
import com.sqx.modules.utils.excel.ExcelData;
import com.sqx.modules.utils.excel.ExportExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wucahng
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/admin/sdkInfo/")
public class SdkInfoController {
    @Autowired
    private SdkInfoService infoService;

    /**
     * 生成卡密
     *
     * @return
     */
    @PostMapping("saveSdkInfo")
    public Result saveSdkInfo(Long typeId, Integer num,Long sysUserId) {
        return infoService.saveSdkInfo(typeId, num,sysUserId);
    }

    /**
     * 获取卡密列表
     */
    @GetMapping("getSdkList")
    public Result getSdkList(Integer page, Integer limit, SdkInfo sdkInfo) {
        return Result.success().put("data", infoService.getSdkList(page, limit, sdkInfo));
    }

    /**
     * 批量删除卡密
     */
    @GetMapping("deleteSdk")
    public Result deleteSdk(String sdkIds) {
        return infoService.removeByIds(Arrays.asList(sdkIds.split(","))) ? Result.success() : Result.error();
    }

    @GetMapping("excelCouponCardList")
    @ApiOperation("导出卡密列表")
    public void excelCouponCardList(SdkInfo sdkInfo, HttpServletResponse response) throws Exception{
        ExcelData excelData = infoService.excelSdkList(sdkInfo);
        ExportExcelUtils.exportExcel(response,"卡密列表.xlsx",excelData);
    }

    @GetMapping("/selectSdkCount")
    @ApiOperation("卡密数据统计")
    public Result selectSdkCount(Integer type,String date){
        if(type==3){
            date = date.substring(0,4);
        }else if(type==2){
            date = date.substring(0,7);
        }
        int sdkCount = infoService.count(new QueryWrapper<SdkInfo>().like("create_time", date));
        int wsySdkCount = infoService.count(new QueryWrapper<SdkInfo>().like("create_time", date).eq("status",0));
        int ysySdkCount = infoService.count(new QueryWrapper<SdkInfo>().like("create_time", date).eq("status",1));
        int ygqSdkCount = infoService.count(new QueryWrapper<SdkInfo>().like("create_time", date).eq("status",2));
        Map<String,Integer> result=new HashMap<>();
        result.put("sdkCount",sdkCount);
        result.put("wsySdkCount",wsySdkCount);
        result.put("ysySdkCount",ysySdkCount);
        result.put("ygqSdkCount",ygqSdkCount);
        return Result.success().put("data",result);
    }


}

