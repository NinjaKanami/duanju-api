package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  充值记录
 * @author fang 2020-05-14
 */
@Data
@TableName("pay_details")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 分类（1微信 1app微信 2微信公众号 3微信小程序 4支付宝app 5支付宝h5 6抖音 7苹果）
     */
    private Integer classify;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 支付宝交易订单号
     */
    private String tradeNo;

    /**
     * 充值点券
     */
    private Double money;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0待支付 1支付成功 2失败
     */
    private Integer state;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付类型 1 订单  2会员
     */
    private Integer type;

    private String remark;

    private String productId;

    @TableField(exist = false)
    private String refundContent;

    @TableField(exist = false)
    private String outRequestNo;

}
