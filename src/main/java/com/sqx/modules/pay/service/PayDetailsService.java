package com.sqx.modules.pay.service;

import com.sqx.common.utils.PageUtils;

public interface PayDetailsService {

    PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state, String userName, String orderId);

    Double selectSumPay(String createTime, String endTime, Long userId);

    PageUtils payMemberAnalysis(int page, int limit, String time, Integer flag);

    PageUtils selectUserMemberList(int page, int limit, String phone);

    Double selectSumMember(String time, Integer flag);

    Double selectSumPayByState(String time, Integer flag, Integer state);

    Double selectSumPayByClassify(String time, Integer flag, Integer classify,Integer payClassify);

    Double instantselectSumPay(String date, Long userId);

}
