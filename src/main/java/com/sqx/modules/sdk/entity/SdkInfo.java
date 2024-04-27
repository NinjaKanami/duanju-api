package com.sqx.modules.sdk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-20
 */
@Data
public class SdkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 卡密id
     */
    @TableId(value = "sdk_id", type = IdType.AUTO)
    private Long sdkId;

    /**
     * 卡密内容
     */
    private String sdkContent;

    /**
     * 卡密类型id
     */
    private Long typeId;

    /**
     * 0未使用 1已使用 2已过期
     */
    private Integer status;
    /**
     * 金豆
     */
    private BigDecimal giveNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 过期时间
     */
    private String overdueTime;
    /**
     * 使用时间
     */
    private String useTime;

    /**
     * 剧荐管
     */
    private Long sysUserId;

    @TableField(exist = false)
    private String sysUserName;

    /**
     * 类型备注
     */
    @TableField(condition = SqlCondition.LIKE)
    private String sdkRemarks;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;


}
