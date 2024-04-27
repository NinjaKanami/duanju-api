package com.sqx.modules.invite.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.invite.entity.Invite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface InviteDao extends BaseMapper<Invite> {

    IPage<Invite> selectInviteList(Page<Map<String,Object>> page, @Param("state") Integer state, @Param("userId") Long userId);

    Integer selectInviteCount(@Param("state") Integer state, @Param("userId") Long userId);

    Double selectInviteSum(@Param("state") Integer state, @Param("userId") Long userId);

    IPage<Map<String,Object>> selectInviteUser(Page<Map<String,Object>> page, @Param("userId") Long userId,@Param("state") Integer state,@Param("userType") Integer userType);

    Invite selectInviteByUser(@Param("userId")Long userId,@Param("inviteeUserId") Long inviteeUserId,@Param("userType") Integer userType);

    Integer selectInviteByUserIdCountNotTime(@Param("userId")Long userId);

    Integer selectInviteByUserIdCount(@Param("userId") Long userId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);

    Double selectInviteByUserIdSum(@Param("userId") Long userId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);

    Double sumInviteMoney(@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> inviteAnalysis(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);


}
