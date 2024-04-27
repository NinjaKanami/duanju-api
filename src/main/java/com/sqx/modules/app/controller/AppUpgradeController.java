package com.sqx.modules.app.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.App;
import com.sqx.modules.app.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/appinfo")
@Api(value = "APP升级管理", tags = {"APP升级管理"})
public class AppUpgradeController {

    @Autowired
    private AppService iAppService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("管理平台升级详情")
    @ResponseBody
    public Result list(Integer page,Integer limit) {
        IPage<App> pages =new Page<>(page,limit);
        return Result.success().put("data",iAppService.page(pages));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台升级详情")
    @ResponseBody
    public Result getBanner(@PathVariable Long id) {
        return Result.success().put("data",iAppService.selectAppById(id));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation("管理平台添加升级信息")
    @ResponseBody
    public Result addBanner(@RequestBody App app) {
        if(app.getId()!=null){
            iAppService.updateAppById(app);
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            app.setCreateAt(sdf.format(new Date()));
            iAppService.insertApp(app);
        }
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除升级信息")
    public Result deleteBanner(@PathVariable Long id) {
        iAppService.deleteAppById(id);
        return Result.success();
    }





}
