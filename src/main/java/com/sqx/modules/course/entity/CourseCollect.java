package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description course_collect  收藏
 * @author fang
 * @date 2021-03-27
 */
@Data
@TableName("course_collect")
public class CourseCollect implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 收藏id
     */
    @TableId(type = IdType.AUTO)
    private Long courseCollectId;

    /**
     * 短剧id
     */
    private Long courseId;

    /**
     * 集id
     */
    private Long courseDetailsId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 分类 1收藏 2点赞 3历史记录 4是否自动扣费 5是否整剧购买
     */
    private Integer classify;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
    @TableField(exist = false)
    private Integer type;

    private Double courseDetailsSec;


    public CourseCollect() {}
}
