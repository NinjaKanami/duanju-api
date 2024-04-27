package com.sqx.modules.pay.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.pay.service.PayClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang
 * @date 2022/4/16
 */
@Slf4j
@RestController
@Api(value = "充值分类", tags = {"充值分类"})
@RequestMapping(value = "/app/payClassify")
public class AppPayClassifyController {

    @Autowired
    private PayClassifyService payClassifyService;


    @GetMapping("/selectPayClassifyList")
    @ApiOperation("查询充值分类")
    public Result selectPayClassifyList(){
        return Result.success().put("data",payClassifyService.list());
    }




}