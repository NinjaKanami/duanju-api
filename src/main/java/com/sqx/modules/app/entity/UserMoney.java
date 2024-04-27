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
@TableName("user_money")
@ApiModel("用户钱包")
public class UserMoney implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 钱包点券
     */
    @ApiModelProperty("钱包点券")
    private BigDecimal money;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    /**
     * 渠道用户id
     */
    @ApiModelProperty("渠道用户id")
    @TableField("sys_user_id")
    private Long sysUserId;

}
