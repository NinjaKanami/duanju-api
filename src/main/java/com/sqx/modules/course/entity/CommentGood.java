package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description comment_good  评论点赞
 * @author fang
 * @date 2021-06-23
 */
@Data
public class CommentGood implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 评论点赞id
     */
    @TableId(type = IdType.AUTO)
    private Long commentGoodId;

    /**
     * 评论id
     */
    private Long courseCommentId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    public CommentGood() {}
}
