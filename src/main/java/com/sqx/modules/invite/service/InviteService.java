package com.sqx.modules.invite.service;


import com.sqx.common.utils.PageUtils;
import com.sqx.modules.app.entity.UserEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface InviteService {

    PageUtils selectInviteList(int page, int limit, Integer state, Long userId);

    Integer selectInviteCount(Integer state,Long userId);

    Double selectInviteSum(Integer state,Long userId);

    int saveBody(Long userId, UserEntity userEntity);

    PageUtils selectInviteUser(int page,int limit,Long userId,Integer state,Integer userType);

    Integer selectInviteByUserIdCountNotTime(Long userId);

    Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime);

    Double selectInviteByUserIdSum(Long userId, Date startTime,Date endTime);

    Double sumInviteMoney(String time,Integer flag);

    PageUtils inviteAnalysis(int page,int limit, String time, Integer flag);

    Map updateInvite(UserEntity userEntity, String ordersNo,Integer classify, BigDecimal price);

}
