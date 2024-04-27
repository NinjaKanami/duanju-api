package com.sqx.modules.pay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.service.PayClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fang
 * @date 2022/4/16
 */
@Slf4j
@RestController
@Api(value = "充值分类", tags = {"充值分类"})
@RequestMapping(value = "/payClassify")
public class PayClassifyController {

    @Autowired
    private PayClassifyService payClassifyService;


    @PostMapping("/insertPayClassify")
    @ApiOperation("添加充值分类")
    public Result insertPayClassify(@RequestBody PayClassify payClassify){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payClassify.setCreateTime(sdf.format(new Date()));
        payClassifyService.save(payClassify);
        return Result.success();
    }

    @PostMapping("/updatePayClassify")
    @ApiOperation("修改充值分类")
    public Result updatePayClassify(@RequestBody PayClassify payClassify){
        payClassifyService.updateById(payClassify);
        return Result.success();
    }

    @PostMapping("/deletePayClassify")
    @ApiOperation("删除充值分类")
    public Result deletePayClassify(Long payClassifyId){
        payClassifyService.removeById(payClassifyId);
        return Result.success();
    }

    @GetMapping("/selectPayClassifyList")
    @ApiOperation("查询充值分类")
    public Result selectPayClassifyList(Integer page,Integer limit){
        Page<PayClassify> pages=new Page<>(page,limit);
        return Result.success().put("data",new PageUtils(payClassifyService.page(pages)));
    }




}