package com.sqx.modules.invite.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.invite.entity.InviteMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 邀请收益钱包
 *
 */
@Mapper
public interface InviteMoneyDao extends BaseMapper<InviteMoney> {


    InviteMoney selectInviteMoneyByUserId(Long userId);

    int updateInviteMoneySum(@Param("money") Double money,@Param("userId") Long userId);

    int updateInviteMoneySumSub(@Param("money") Double money,@Param("userId") Long userId);

    int updateInviteMoneyCashOut(@Param("type") Integer type,@Param("money") Double money,@Param("userId") Long userId);

    int updateInviteMoneySub(@Param("money") Double money,@Param("userId") Long userId);

}
