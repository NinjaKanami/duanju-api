package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("coupon")
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 优惠券d
     */
    @TableId(type = IdType.AUTO)
    private Long couponId;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 可抵扣点券
     */
    private BigDecimal money;

    /**
     * 所属类型1邀请好友-
     */
    private Integer couponType;

    public Coupon() {}
}
