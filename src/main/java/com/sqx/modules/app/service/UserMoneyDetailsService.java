package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;

import java.util.List;
import java.util.Map;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {

    Result queryUserMoneyDetails(Integer page, Integer limit,Long sysUserId,Long userId,Integer classify,Integer type);

    Result selectUserMoneyDetails(Integer page, Integer limit,Long userId);

    Double monthIncome(String date,Long userId);

    Double selectSumMoneyByClassifyAndUserId(String time,Integer classify,Long userId);

    Integer selectCountByClassifyAndUserId(String time,Integer classify,Long userId);

    List<Map<String,String>> selectSumMoneyByClassifyAndUserIdTime(String startTime, String endTime, Integer classify, Long userId);

    Double selectSumMoneyByClassifyAndUserIdTimes(String startTime, String endTime, Integer classify, Long userId);

    Integer selectCountByClassifyAndUserIdTime(String startTime,String endTime,Integer classify,Long userId);

    Result selectMoneyDetailsByTime(Integer page, Integer limit, String time, @LoginUser UserEntity userEntity, Integer classify);

    Result selectChannelMoney(UserEntity userEntity);

    Double selectSumMoneyByUserIdAndTime(Long userId,Integer type,String time);

    Double selectSumMoneyByTime(Integer type,String time);

    Result selectEarningsMoneyDetailsByTime(Integer page,Integer limit, Integer type,String time,Integer classify);

    Result selectEarningsMoneyDetailsByTimeAndUserId(Integer page,Integer limit, Integer type,String time,Integer classify,Long userId,Integer timeType);

}
