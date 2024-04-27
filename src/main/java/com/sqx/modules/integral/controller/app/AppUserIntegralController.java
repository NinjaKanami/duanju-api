package com.sqx.modules.integral.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户积分", tags = {"用户积分"})
@RequestMapping(value = "/app/integral")
public class AppUserIntegralController {

    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;

    @Login
    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    @ApiOperation("查看用户积分")
    @ResponseBody
    public Result selectByUserId(@RequestAttribute Long userId) {
        return Result.success().put("data", userIntegralService.selectById(userId));
    }

    @Login
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ApiOperation("查看用户积分详细列表信息")
    @ResponseBody
    public Result selectUserIntegralDetailsByUserId(int page, int limit, @RequestAttribute Long userId) {
        return Result.success().put("data", userIntegralDetailsService.selectUserIntegralDetailsByUserId(page, limit, userId));
    }

    @Login
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    @ApiOperation("签到")
    @ResponseBody
    public Result signIn(@RequestAttribute Long userId) {
        return userIntegralDetailsService.signIn(userId);
    }

    @Login
    @RequestMapping(value = "/selectIntegralDay", method = RequestMethod.GET)
    @ApiOperation("获取签到记录")
    @ResponseBody
    public Result selectIntegralDay(@RequestAttribute Long userId) {
        return userIntegralDetailsService.selectIntegralDay(userId);
    }

    @Login
    @RequestMapping(value = "/creditsExchange", method = RequestMethod.POST)
    @ApiOperation("积分兑换")
    @ResponseBody
    public Result creditsExchange(@RequestAttribute Long userId,Integer integral) {
        return userIntegralDetailsService.creditsExchange(userId,integral);
    }

}
