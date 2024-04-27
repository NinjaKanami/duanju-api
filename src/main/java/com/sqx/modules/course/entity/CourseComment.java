package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.app.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @description course_comment  短剧评论
 * @author fang
 * @date 2021-03-27
 */
@Data
@TableName("course_comment")
public class CourseComment implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 短剧评论id
     */
    @TableId(type = IdType.AUTO)
    private Long courseCommentId;

    /**
     * 用户id
     */
    private Long userId;
    @TableField(exist = false)
    private UserEntity userEntity;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private Integer isCommentGood;

    @TableField(exist = false)
    private Integer isGood;

    /**
     * 短剧id
     */
    private Long courseId;
    /**
     * 短剧
      */
    @TableField(exist = false)
    private Course course;
    /**
     * 点赞数
     */
    private Integer goodsNum;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createTime;

    public CourseComment() {}
}