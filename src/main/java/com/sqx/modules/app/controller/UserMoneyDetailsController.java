package com.sqx.modules.app.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/moneyDetails")
@AllArgsConstructor
@Api("钱包明细")
public class UserMoneyDetailsController {
    private UserMoneyDetailsService userMoneyDetailsService;
    private UserMoneyService userMoneyService;
    private UserService userService;


    @ApiOperation("收益明细")
    @GetMapping("/selectUserMoneyDetails")
    public Result selectUserMoneyDetails(Integer page, Integer limit, Long userId) {
        return userMoneyDetailsService.selectUserMoneyDetails(page, limit,userId);
    }

    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit,Long sysUserId, Long userId,Integer classify,Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, sysUserId, userId,classify,type);
    }

    @GetMapping("/selectUserMoney")
    @ApiOperation("我的钱包")
    public Result selectUserMoney(Long userId){
        return Result.success().put("data",userMoneyService.selectUserMoneyByUserId(userId));
    }

    @GetMapping("/selectSysUserMoney")
    @ApiOperation("代理钱包")
    public Result selectSysUserMoney(Long userId){
        return Result.success().put("data",userMoneyService.selectSysUserMoneyByUserId(userId));
    }


    @GetMapping("/selectMoneyDetailsByTime")
    @ApiOperation("根据时间查询收益明细")
    public Result selectMoneyDetailsByTime(Integer page, Integer limit, String time,Long userId, Integer classify){
        UserEntity userEntity = userService.getById(userId);
        return userMoneyDetailsService.selectMoneyDetailsByTime(page, limit, time, userEntity, classify);
    }

    @GetMapping("/selectEarningsMoneyDetailsByTime")
    @ApiOperation("根据时间查询收益明细")
    public Result selectEarningsMoneyDetailsByTime(Integer page,Integer limit, Integer type,String time,Integer classify){
        return userMoneyDetailsService.selectEarningsMoneyDetailsByTime(page, limit, type, time, classify);
    }


    @GetMapping("/selectEarningsMoneyDetailsByTimeAndUserId")
    @ApiOperation("根据时间查询收益明细")
    public Result selectEarningsMoneyDetailsByTimeAndUserId(Integer page,Integer limit, Integer type,String time,Integer classify,Long userId,Integer timeType){
        return userMoneyDetailsService.selectEarningsMoneyDetailsByTimeAndUserId(page, limit, type, time, classify,userId,timeType);
    }



    @GetMapping("/selectChannelMoney")
    @ApiOperation("获取剧荐管收益")
    public Result selectChannelMoney(Long userId){
        UserEntity userEntity = userService.getById(userId);
        return userMoneyDetailsService.selectChannelMoney(userEntity);
    }

    @RequestMapping(value = "/selectMoneyCount", method = RequestMethod.GET)
    @ApiOperation("获取用户收入统计")
    @ResponseBody
    public Result selectMoneyCount(Long userId) {
        Calendar calendar=Calendar.getInstance();
        //今日
        Double nowDayMoney = userMoneyDetailsService.selectSumMoneyByUserIdAndTime(userId, 1, DateUtils.format(calendar.getTime()));
        //本月
        Double nowMonthMoney = userMoneyDetailsService.selectSumMoneyByUserIdAndTime(userId, 2, DateUtils.format(calendar.getTime()));
        //上月
        calendar.add(Calendar.DAY_OF_MONTH,-1);
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
