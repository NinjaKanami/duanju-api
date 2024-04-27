package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {


    Double monthIncome(@Param("date") String date,@Param("userId") Long userId);

    Double selectSumMoneyByClassifyAndUserId(String time,Integer classify,Long userId);

    Integer selectCountByClassifyAndUserId(String time,Integer classify,Long userId);

    List<Map<String,String>> selectSumMoneyByClassifyAndUserIdTime(String startTime, String endTime, Integer classify, Long userId);

    Double selectSumMoneyByClassifyAndUserIdTimes(String startTime, String endTime, Integer classify, Long userId);


    Integer selectCountByClassifyAndUserIdTime(String startTime,String endTime,Integer classify,Long userId);

    IPage<UserMoneyDetails> selectMoneyDetailsByTime(Page<UserMoneyDetails> page, String time, Long userId,Integer classify);

    Double selectSumMoneyByUserIdAndTime(Long userId,Integer type,String time);

    Double selectSumMoneyByTime(Integer type,String time);

    IPage<UserMoneyDetails> selectEarningsMoneyDetailsByTime(Page<UserMoneyDetails> page, Integer type,String time,Integer classify);

    IPage<UserMoneyDetails> selectEarningsMoneyDetailsByTimeAndUserId(Page<UserMoneyDetails> page, Integer type,String time,Integer classify,Long userId,
                                                                      String startTime,String endTime);

}
