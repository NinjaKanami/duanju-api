package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import com.sqx.modules.orders.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/VipDetails")
@AllArgsConstructor
@Api("会员详情信息")
public class AppVipDetailsController {
    private VipDetailsService appVipDetailsService;
    private OrdersService ordersService;

    /**
     * 查询会员的详情信息
     *
     * @return
     */

    @ApiParam("查询会员的详情信息")
    @GetMapping("/selectVipDetails")
    public Result selectVipDetails() {
        return appVipDetailsService.selectVipDetails();
    }

    /**
     * 添加会员的详情信息
     *
     * @return
     */
    @Login
    @ApiParam("添加会员的详情信息")
    @GetMapping("/insertVipDetails")
    public Result insertVipDetails(VipDetails vipDetails) {
        return appVipDetailsService.insertVipDetails(vipDetails);

    }
}


