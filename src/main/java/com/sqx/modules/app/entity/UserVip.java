package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("user_vip")
public class UserVip implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户会员ID
     */
    @TableId
    private Long vipId;
    /**
     * 会员类型
     */
    private Integer vipNameType;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会员是否到期
     */
    private Integer isVip;


    /**
     * 购买时间
     */
    private String createTime;
    /**
     * 到期时间
     */
    private String endTime;

    /**
     * 会员类型 1活动赠送 2充值开通
     */
    private Integer vipType;

    public UserVip() {}
}
