package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_money_details")
@ApiModel("钱包详情")
public class UserMoneyDetails implements Serializable {
    /**
     * 钱包详情id
     */
    @ApiModelProperty("钱包详情id")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 渠道用户id
     */
    @ApiModelProperty("渠道用户id")
    @TableField("sys_user_id")
    private Long sysUserId;

    /**
     * 对应用户id
     */
    @TableField("by_user_id")
    @ApiModelProperty("对应用户id")
    private Long byUserId;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 1注册  2首次购买  3购买 4提现
     */
    @ApiModelProperty("1充值钱包明细 2提现钱包明细")
    //10普通达人推广剧收益
    private Integer classify;
    /**
     * 类别（1充值2支出）
     */
    @ApiModelProperty("类别（1充值2支出）")
    private Integer type;
    /**
     * 状态 1待支付 2已到账 3取消
     */
    @ApiModelProperty("状态 1待支付 2已到账 3取消")
    private Integer state;
    /**
     * 点券
     */
    @ApiModelProperty("点券")
    private BigDecimal money;

    /**
     * 订单金额
     */
    private BigDecimal payMoney;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    private String ordersNo;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String byUserName;

    @TableField(exist = false)
    private String agencyIndex;

    @TableField(exist = false)
    private String isChannel;

    @TableField(exist = false)
    private String isRecommend;

    @TableField(exist = false)
    private String inviterCode;

}
