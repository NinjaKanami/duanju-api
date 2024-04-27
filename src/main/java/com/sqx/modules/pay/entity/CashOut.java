package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 提现申请
 * @author fang
 * @date 2020/7/8
 */
@Data
@TableName("cash_out")
public class CashOut implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请提现id
     */
    @TableId(type = IdType.INPUT)
    private long id;

    /**
     * 申请时间
     */
    private String createAt;

    /**
     * 是否转账
     */
    private String outAt;

    /**
     * 提现点券
     */
    private String money;

    /**
     * 转账时间
     */
    private Boolean isOut;

    /**
     * 会员编号
     */
    private String relationId;

    /**
     * 用户id
     */
    private Long userId;

    private String wxImg;

    @TableField(exist = false)
    private String userName;

    /**
     * 支付宝账号
     */
    private String zhifubao;

    /**
     * 支付宝姓名
     */
    private String zhifubaoName;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 状态 0待转账 1成功 -1退款
     */
    private Integer state;

    /**
     * 原因
     */
    private String refund;

    /**
     * 手续费
     */
    private Double rate;

    private Long sysUserId;

    /**
     * 提现类型 1用户提现  2分销商提现
     */
    private Integer userType;

    /**
     * 提现方式 1支付宝 2微信
     */
    private Integer classify;


    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private Integer status;

    @TableField(exist = false)
    private String sysUserName;

}