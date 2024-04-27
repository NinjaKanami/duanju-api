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
public class SdkType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sdk类型
     */
    @TableId(value = "type_id", type = IdType.AUTO)
    private Long typeId;

    /**
     * 赠送金额
     */
    private BigDecimal giveNum;


    /**
     * 有效天数
     */
    private Integer validDay;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 备注
     */
    @TableField(condition = SqlCondition.LIKE)
    private String remarks;
}
