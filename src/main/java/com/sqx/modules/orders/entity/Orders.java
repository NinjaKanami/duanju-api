package com.sqx.modules.orders.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.course.entity.Course;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fang
 * @description orders
 * @date 2021-03-27
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(type = IdType.AUTO)
    private Long ordersId;

    /**
     * 订单编号
     */
    private String ordersNo;

    /**
     * 支付宝支付单号
     */
    private String tradeNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 短剧id
     */
    private Long courseId;

    private Long courseDetailsId;

    @TableField(exist = false)
    /**
     * 短剧对象
     */
    private Course course;
    /**
     * 支付点券
     */
    private BigDecimal payMoney;

    /**
     * 支付方式 1微信app 2微信公众号 3微信小程序 4支付宝  5会员免费 6点券 7抖音
     */
    private Integer payWay;

    private String payTime;


    /**
     * 状态 0待支付 1已支付 2已退款
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 退款原因
     */
    private String refundContent;

    /**
     * 订单种类 1短剧 11云短剧 2会员 3充值  4余额充值 5点卷兑换  6（虚拟 查询平台收入的）
     */
    private Integer ordersType;

    /**
     * 0会月/1季度/2年
     */
    private Integer vipNameType;

    /**
     * 达人用户
     */
    private Long memberUserId;

    @TableField(exist = false)
    private String memberUserName;

    /**
     * 达人收益
     */
    private BigDecimal memberMoney;

    /**
     * 达人等级
     */
    private Integer memberType;

    /**
     * 剧荐管用户
     */
    private Long channelUserId;

    @TableField(exist = false)
    private String channelUserName;

    /**
     * 剧荐管收益
     */
    private BigDecimal channelMoney;

    /**
     * 平台收益
     */
    private BigDecimal pingMoney;

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String qdCode;

    @TableField(exist = false)
    private String sysUserName;

    @TableField(exist = false)
    private String agencyIndex;

    /**
     * 会员对象
     */
    public Orders() {
    }
}
