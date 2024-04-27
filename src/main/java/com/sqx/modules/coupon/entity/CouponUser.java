package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("coupon_user")
public class CouponUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户优惠券id
     */
    @TableId(type = IdType.AUTO)
    private Long couponUserId;
    /**
     *用户id
     */
    private Long userId;
    /**
     * 优惠券点券
     */
    private BigDecimal couponMoney;
    /**
     * 优惠券使用规则
     */
    private String couponName;
    public CouponUser() {}
}
