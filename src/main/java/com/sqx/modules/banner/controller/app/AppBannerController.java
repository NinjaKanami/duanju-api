package com.sqx.modules.banner.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.banner.entity.Banner;
import com.sqx.modules.banner.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "app banner图", tags = {"app banner图"})
@RequestMapping(value = "/app/banner")
public class AppBannerController {


    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerList(Integer classify) {
        return Result.success().put("data", bannerService.selectBannerList(classify));
    }

    @RequestMapping(value = "/selectBannerPage", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerPage(Integer page,Integer limit,Integer classify) {
        return Result.success().put("data", new PageUtils(bannerService.page(new Page<>(page,limit),new QueryWrapper<Banner>().eq("classify",classify))));
    }

    @RequestMapping(value = "/clickBanner", method = RequestMethod.GET)
    @ApiOperation("点击金刚图")
    @ResponseBody
    public Result clickBanner(Integer bannerId,int page,int limit) {
        return bannerService.clickBanner(bannerId,page,limit);
    }


}