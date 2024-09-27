package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sqx.modules.orders.entity.Orders;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description course  短剧
 * @author fang
 * @date 2021-03-27
 */
@Data
@TableName("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短剧id
     */
    @TableId(type = IdType.AUTO)
    private Long courseId;

    /**
     * 轮播图
     */
    private String bannerImg;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    @TableField("title_img")
    private String titleImg;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格种类 0单集购买 1整剧购买
     */
    private Integer priceType;

    /**
     * 上下架 1上架  2下架
     */
    private Integer status;

    /**
     * 是否完结 0未完结 1已完结
     */
    private Integer over;

    /**
     * 分类
     */
    @TableField("classify_id")
    private Long classifyId;
    /**
     * 短剧分类对象
     */
    @TableField(exist = false)
    private CourseClassification courseClassification;

    /**
     * 购买次数
     */
    @TableField("pay_num")
    private Integer payNum;

    /**
     * 短剧标签
     */
    @TableField("course_label")
    private String courseLabel;

    @TableField("course_label_ids")
    private String courseLabelIds;

    /**
     * 内容图
     */
    private String img;

    /**
     * 短剧介绍
     */
    private String details;

    /**
     * 删除标识 0未删除 1已删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;
    /**
     * 文件地址
     */
    @TableField("msg_url")
    private String msgUrl;
    /**
     * 上传方式0OSS-1本地
     */
    @TableField("msg_type")
    private Integer msgType;

    /**
     * 是否是推荐商品
     */
    @TableField("is_recommend")
    private Integer isRecommend;
    /**
     * 首页金刚区分类
     */
    private  Integer bannerId;

    /**
     * 是否收费 1vip 2免费 3云短剧
     */
    private Integer isPrice;

    /**
     * 短剧目录
     */
    @TableField(exist = false)
    private List<CourseDetails> listsDetail;



    /**
     * 短剧分类 1短剧 2链接 3文档
     */
    private Integer courseType;

    /**
     * 播放量
     */
    private Integer viewCounts;

    /**
     * 抖音封面图id
     */
    private String dyImgId;

    /**
     * 抖音短剧id
     */
    private String dyCourseId;

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
     * 资质 许可证号
     */
    private String licenseNum;

    /**
     * 资质 登记号
     */
    private String registrationNum;

    /**
     * 资质 普通备案号
     */
    private String ordinaryRecordNum;

    /**
     * 资质 重点备案号
     */
    private String keyRecordNum;

    /**
     * 微信短剧id
     */
    private String wxCourseId;

    /**
     * 微信是否显示 1是
     */
    private Integer wxShow;

    /**
     * 抖音是否显示 1是
     */
    private Integer dyShow;

    /**
     * 排序序列号
     */
    private Integer sequence;

    /**
     * 是否可砍剧
     */
    private Integer isCut;
    /**
     * 需邀请的用户数量
     */
    private Integer inviteTarget;
    /**
     * 每次砍剧获得的点券
     */
    private Integer rewardMoney;
    /**
     * 砍剧时间限制（小时）
     */
    private Integer cutTimeLimit;


    @TableField(exist = false)
    private String remark;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String courseCount;

    @TableField(exist = false)
    private Integer isMyCourse;

    @TableField(exist = false)
    private Orders orders;

    @TableField(exist = false)
    private String courseDetailsName;

    @TableField(exist = false)
    private Long courseDetailsId;

    @TableField(exist = false)
    private Integer courseDetailsCount;

    @TableField(exist = false)
    private String produceEndTime;

    @TableField(exist = false)
    private Integer isCollect;

    public Course() {}
}
