package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/moneyDetails")
@AllArgsConstructor
@Api("钱包明细")
public class AppUserMoneyDetailsController {

    private UserMoneyDetailsService userMoneyDetailsService;
    private UserMoneyService userMoneyService;


    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit, @RequestAttribute Long userId,Integer classify,Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit,null, userId,1,type);
    }

    @Login
    @ApiOperation("收益明细")
    @GetMapping("/selectUserMoneyDetails")
    public Result selectUserMoneyDetails(Integer page, Integer limit, @RequestAttribute Long userId) {
        return userMoneyDetailsService.selectUserMoneyDetails(page, limit,userId);
    }

    @Login
    @GetMapping("/selectUserMoney")
    @ApiOperation("我的钱包")
    public Result selectUserMoney(@RequestAttribute Long userId){
        return Result.success().put("data",userMoneyService.selectUserMoneyByUserId(userId));
    }

    @Login
    @GetMapping("/selectMoneyDetailsByTime")
    @ApiOperation("根据时间查询收益明细")
    public Result selectMoneyDetailsByTime(Integer page, Integer limit, String time, @LoginUser UserEntity userEntity, Integer classify){
        return userMoneyDetailsService.selectMoneyDetailsByTime(page, limit, time, userEntity, classify);
    }

    @Login
    @GetMapping("/selectChannelMoney")
    @ApiOperation("获取剧荐管收益")
    public Result selectChannelMoney(@LoginUser UserEntity userEntity){
        return userMoneyDetailsService.selectChannelMoney(userEntity);
    }

    @Login
    @RequestMapping(value = "/selectMoneyCount", method = RequestMethod.GET)
    @ApiOperation("获取用户收入统计")
    @ResponseBody
    public Result selectMoneyCount(@RequestAttribute Long userId) {
        Calendar calendar=Calendar.getInstance();
        //今日
        Double nowDayMoney = userMoneyDetailsService.selectSumMoneyByUserIdAndTime(userId, 1, DateUtils.format(calendar.getTime()));
        //本月
        Double nowMonthMoney = userMoneyDetailsService.selectSumMoneyByUserIdAndTime(userId, 2, DateUtils.format(calendar.getTime()));
        //上月
        calendar.add(Calendar.MONTH,-1);
        Double monthMoney = userMoneyDetailsService.selectSumMoneyByUserIdAndTime(userId, 2, DateUtils.format(calendar.getTime()));
        //累计
        Double sumMoney = userMoneyDetailsService.selectSumMoneyByUserIdAndTime(userId, null, null);
        Map<String,Double> result=new HashMap<>();
        result.put("nowDayMoney",nowDayMoney);
        result.put("nowMonthMoney",nowMonthMoney);
        result.put("monthMoney",monthMoney);
        result.put("sumMoney",sumMoney);
        return Result.success().put("data",result);
    }

}
