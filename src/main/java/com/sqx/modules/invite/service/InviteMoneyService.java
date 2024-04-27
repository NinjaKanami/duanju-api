package com.sqx.modules.invite.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.invite.entity.InviteMoney;

/**
 * 邀请收益
 *
 */
public interface InviteMoneyService extends IService<InviteMoney> {

    InviteMoney selectInviteMoneyByUserId(Long userId);

    int updateInviteMoneySum(Double money,Long userId);

    int updateInviteMoneySumSub(Double money, Long userId);

    int updateInviteMoneyCashOut(Double money,Long userId);

    int updateInviteMoneyCashOut(Integer type,Double money, Long userId);

    Result inviteMoneyConvertUserMoney(UserEntity userEntity, Long payClassifyId);

}
