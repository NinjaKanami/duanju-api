package com.sqx.modules.invite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


/**
 * @description invite_award
 * @author fang
 * @date 2023-12-12
 */
@Data
public class InviteAward implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 邀请奖励id
     */
    private Integer inviteAwardId;

    /**
     * 图片
     */
    private String inviteImg;

    /**
     * 邀请人数
     */
    private Integer inviteCount;

    /**
     * 奖励月份 0是永久
     */
    private Integer inviteMonth;

    /**
     * 创建时间
     */
    private String createTime;

    public InviteAward() {}
}
