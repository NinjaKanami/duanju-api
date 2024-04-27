package com.sqx.modules.banner.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.banner.entity.Banner;
import com.sqx.modules.banner.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "banner图", tags = {"banner图"})
@RequestMapping(value = "/banner")
public class BannerController {


    @Autowired
    private BannerService bannerService;


    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerList(Integer classify){
        return Result.success().put("data",bannerService.selectBannerLists(classify));
    }

    @RequestMapping(value = "/selectBannerPage", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerPage(Integer page,Integer limit,Integer classify){
        return Result.success().put("data",bannerService.selectBannerPage(page,limit,classify));
    }

    @RequestMapping(value = "/selectBannerById", method = RequestMethod.GET)
    @ApiOperation("根据id查看详细信息")
    @ResponseBody
    public Result selectBannerById(Long id){
        return Result.success().put("data",bannerService.selectBannerById(id));
    }

    @RequestMapping(value = "/updateBannerStateById", method = RequestMethod.GET)
    @ApiOperation("隐藏banner图")
    @ResponseBody
    public Result updateBannerStateById(Long id){

        return bannerService.updateBannerStateById(id);
    }

    @RequestMapping(value = "/updateBannerById", method = RequestMethod.POST)
    @ApiOperation("修改banner图")
    @ResponseBody
    public Result updateBannerById(@RequestBody Banner banner){
        bannerService.updateBannerById(banner);
        return Result.success();
    }

    @RequestMapping(value = "/deleteBannerById", method = RequestMethod.GET)
    @ApiOperation("删除banner图")
    @ResponseBody
    public Result deleteBannerById(String ids){
        bannerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

    @RequestMapping(value = "/insertBanner", method = RequestMethod.POST)
    @ApiOperation("添加banner图")
    @ResponseBody
    public Result insertBanner(@RequestBody Banner banner){
        bannerService.insertBanner(banner);
        return Result.success();
    }



}