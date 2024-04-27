package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.pay.service.DyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fang
 * @date 2023/12/11
 */
@RestController
@Api(value = "抖音支付", tags = {"抖音支付"})
@RequestMapping("/app/dyPay")
@Slf4j
public class DyPayController {

    @Autowired
    private DyService dyService;

    @Login
    @ApiOperation("抖音支付订单")
    @PostMapping("/payAppOrder")
    public Result payAppOrder(Long orderId) throws Exception {
        return dyService.payOrder(orderId);
    }

    @Login
    @ApiOperation("充值点券")
    @PostMapping("/payMoney")
    public Result payMoney(Long payClassifyId,@RequestAttribute Long userId)  throws Exception {
        return dyService.payMoney(payClassifyId,userId);
    }

    @Login
    @ApiOperation("抖音虚拟支付订单")
    @PostMapping("/payVirtualAppOrder")
    public Result payVirtualAppOrder(Long orderId) throws Exception {
        return dyService.payVirtualAppOrder(orderId);
    }

    @Login
    @ApiOperation("抖音虚拟支付订单")
    @PostMapping("/payVirtualMoney")
    public Result payVirtualMoney(Long payClassifyId,@RequestAttribute Long userId) throws Exception {
        return dyService.payVirtualAppOrder(payClassifyId,userId);
    }

    @PostMapping("/notify")
    @ApiOperation("抖音回调")
    public String notify(HttpServletRequest request, HttpServletResponse response) {
        return dyService.payBack(request,response);
    }

    @PostMapping("/virtualNotify")
    @ApiOperation("抖音虚拟回调")
    public String virtualNotify(@RequestBody JSONObject jsonObject) {
        return dyService.virtualNotify(jsonObject);
    }




}
