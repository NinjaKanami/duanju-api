package com.sqx.modules.invite.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *  invite_money
 * @author fang 2020-07-28
 */
@Data
public class InviteMoney implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邀请收益钱包id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 总获取收益
     */
    private Double moneySum;

    /**
     * 当前点券
     */
    private Double money;

    /**
     * 累计提现
     */
    private Double cashOut;

    public InviteMoney() {
    }

}
