package com.sqx.modules.urlAddress.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.urlAddress.entity.UrlAddress;
import com.sqx.modules.urlAddress.service.UrlAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "域名池", tags = {"域名池"})
@RequestMapping(value = "/urlAddress")
public class UrlAddressController extends AbstractController {

    @Autowired
    private UrlAddressService urlAddressService;

    @PostMapping("/insertUrlAddress")
    @ApiOperation("创建域名")
    public Result insertUrlAddress(@RequestBody UrlAddress urlAddress){
        urlAddress.setCreateTime(DateUtils.format(new Date()));
        urlAddressService.save(urlAddress);
        return Result.success();
    }

    @PostMapping("/updateUrlAddress")
    @ApiOperation("修改域名")
    public Result updateUrlAddress(@RequestBody UrlAddress urlAddress){
        urlAddressService.updateById(urlAddress);
        return Result.success();
    }

    @PostMapping("/deleteUrlAddress")
    @ApiOperation("删除域名")
    public Result deleteUrlAddress(Long addressId){
        urlAddressService.removeById(addressId);
        return Result.success();
    }

    @GetMapping("/selectUrlAddressList")
    @ApiOperation("查询域名池列表")
    public Result selectUrlAddressList(Integer page,Integer limit,String urlAddress,Integer status){
        return Result.success().put("data",new PageUtils(urlAddressService.page(new Page<>(page,limit),
                new QueryWrapper<UrlAddress>().like(StringUtils.isNotEmpty(urlAddress),"url_address",urlAddress)
                        .eq(status!=null && status!=0,"status",status))));
    }

    @GetMapping("/selectUrlAddress")
    @ApiOperation("获取分享链接")
    public Result selectUrlAddress(){
        return Result.success().put("data",urlAddressService.selectUrlAddressOne());
    }


}
