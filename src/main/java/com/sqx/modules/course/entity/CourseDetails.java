package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description course_details  短剧目录
 * @author fang
 * @date 2021-03-27
 */
@Data
@TableName("course_details")
public class CourseDetails implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 短剧目录id
     */
    @TableId(type = IdType.AUTO)
    private Long courseDetailsId;

    /**
     * 短剧id
     */
    private Long courseId;

    @TableField(exist = false)
    private Course course;

    @TableField(exist = false)
    private String title;

    /**
     * 封面图
     */
    private String titleImg;

    /**
     * 介绍
     */
    private String content;

    /**
     * 短剧名称
     */
    private String courseDetailsName;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 点赞数
     */
    private Integer goodNum;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 是否推荐 1是 2否
     */
    private Integer good;

    /**
     * 是否收费 1是 2免费
     */
    private Integer isPrice;

    /**
     * 抖音视频id
     */
    private String dyCourseDetailsId;

    /**
     * 视频上传状态 1上传中 2上传成功 3上传失败
     */
    private Integer dyUrlStatus;

    /**
     * 抖音封面图id
     */
    private String dyImgId;

    /**
     * 抖音集id
     */
    private String dyEpisodeId;

    /**
     * 抖音提审状态 1已提交 2已通过 3已拒绝 4已上线
     */
    private Integer dyStatus;

    /**
     * 抖音审核内容
     */
    private String dyStatusContent;

    /**
     * 当前版本号
     */
    private String dyVersion;

    /**
     * 微信视频id
     */
    private String wxCourseDetailsId;

    @TableField(exist = false)
    private Integer isCollect;

    @TableField(exist = false)
    private Integer isGood;

    @TableField(exist = false)
    private Integer courseDetailsCount;

    @TableField(exist = false)
    private String wxUrl;

    public CourseDetails() {}
}
