package com.sqx.modules.integral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_integral_details")
public class UserIntegralDetails {
    /**
     * 积分详情id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 获取类型 1签到
     */
    private Integer classify;

    /**
     * 分类 1增加 2减少
     */
    private Integer type;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 连续第几天
     */
    private Integer day;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

}
