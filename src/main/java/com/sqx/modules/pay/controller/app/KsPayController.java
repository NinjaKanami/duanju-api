package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.pay.service.KsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "快手支付", tags = {"快手支付"})
@RequestMapping("/app/ksPay")
@Slf4j
public class KsPayController {

    @Autowired
    private KsService ksService;

    @Login
    @ApiOperation("快手支付订单")
    @PostMapping("/payAppOrder")
    public Result payAppOrder(Long orderId) throws Exception {
        return ksService.payOrder(orderId);
    }

    @Login
    @ApiOperation("充值点券")
    @PostMapping("/payMoney")
    public Result payMoney(Long payClassifyId, @RequestAttribute Long userId)  throws Exception {
        return ksService.payMoney(payClassifyId,userId);
    }

    @PostMapping("/notify")
    @ApiOperation("快手回调")
    public String notify(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        String kwaisign = request.getHeader("kwaisign");
        return ksService.payBack(kwaisign,jsonObject);
    }

}
