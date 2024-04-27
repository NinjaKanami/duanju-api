package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description course_user  用户短剧
 * @author fang
 * @date 2021-03-27
 */
@Data
@TableName("course_user")
public class CourseUser implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 我的短剧id
     */
    @TableId(type = IdType.AUTO)
    private Long courseUserId;

    /**
     * 短剧id
     */
    private Long courseId;

    /**
     * 集数id
     */
    private Long courseDetailsId;

    /**
     * 分类 1整集/2单集
     */
    private Integer classify;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String courseCount;

    public CourseUser() {}
}
