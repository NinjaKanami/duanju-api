package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.performer.entity.Performer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author fang
 * @description course  短剧
 * @date 2021-03-27
 */
@ApiModel(description = "course  短剧")
@Data
@TableName("course")
public class Course implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 短剧id
     */
    @ApiModelProperty("短剧id")
    @TableId(type = IdType.AUTO)
    private Long courseId;

    /**
     * 曾用名
     */
    @ApiModelProperty("曾用名")
    private String formerName;

    /**
     * 出品方
     */
    @ApiModelProperty("出品方")
    private String producer;

    /**
     * 评分
     */
    @ApiModelProperty("评分")
    private BigDecimal score;

    /**
     * 是否外部 0否 1是
     */
    @ApiModelProperty("是否外部 0否 1是")
    private Integer isExternal;

    /**
     * 外部链接
     */
    @ApiModelProperty("外部链接")
    private String externalUrl;

    /**
     * 轮播图
     */
    @ApiModelProperty("轮播图")
    private String bannerImg;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    @TableField("title_img")
    private String titleImg;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 价格种类 0单集购买 1整剧购买
     */
    @ApiModelProperty("价格种类 0单集购买 1整剧购买")
    private Integer priceType;

    /**
     * 上下架 1上架  2下架
     */
    @ApiModelProperty("上下架 1上架  2下架")
    private Integer status;

    /**
     * 是否完结 0未完结 1已完结
     */
    @ApiModelProperty("是否完结 0未完结 1已完结")
    private Integer over;

    /**
     * 分类
     */
    @ApiModelProperty("分类")
    @TableField("classify_id")
    private Long classifyId;
    /**
     * 短剧分类对象
     */
    @ApiModelProperty("短剧分类对象")
    @TableField(exist = false)
    private CourseClassification courseClassification;

    /**
     * 购买次数
     */
    @ApiModelProperty("购买次数")
    @TableField("pay_num")
    private Integer payNum;

    /**
     * 短剧标签
     */
    @ApiModelProperty("短剧标签")
    @TableField("course_label")
    private String courseLabel;

    @ApiModelProperty(hidden = true)
    @TableField("course_label_ids")
    private String courseLabelIds;

    /**
     * 内容图
     */
    @ApiModelProperty("内容图")
    private String img;

    /**
     * 短剧介绍
     */
    @ApiModelProperty("短剧介绍")
    private String details;

    /**
     * 删除标识 0未删除 1已删除
     */
    @ApiModelProperty("删除标识 0未删除 1已删除")
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private String updateTime;
    /**
     * 文件地址
     */
    @ApiModelProperty("文件地址")
    @TableField("msg_url")
    private String msgUrl;
    /**
     * 上传方式0OSS-1本地
     */
    @ApiModelProperty("上传方式0OSS-1本地")
    @TableField("msg_type")
    private Integer msgType;

    /**
     * 是否是推荐商品
     */
    @ApiModelProperty("是否是推荐商品")
    @TableField("is_recommend")
    private Integer isRecommend;
    /**
     * 首页金刚区分类
     */
    @ApiModelProperty("首页金刚区分类")
    private Integer bannerId;

    /**
     * 是否收费 1vip 2免费 3云短剧
     */
    @ApiModelProperty("是否收费 1vip 2免费 3云短剧")
    private Integer isPrice;

    /**
     * 短剧目录
     */
    @ApiModelProperty("短剧目录")
    @TableField(exist = false)
    private List<CourseDetails> listsDetail;


    /**
     * 短剧分类 1短剧 2链接 3文档
     */
    @ApiModelProperty("短剧分类 1短剧 2链接 3文档")
    private Integer courseType;

    /**
     * 播放量
     */
    @ApiModelProperty("播放量")
    private Integer viewCounts;

    /**
     * 抖音封面图id
     */
    @ApiModelProperty("抖音封面图id")
    private String dyImgId;

    /**
     * 抖音短剧id
     */
    @ApiModelProperty("抖音短剧id")
    private String dyCourseId;

    /**
     * 抖音提审状态 1已提交 2已通过 3已拒绝 4已上线
     */
    @ApiModelProperty("抖音提审状态 1已提交 2已通过 3已拒绝 4已上线")
    private Integer dyStatus;

    /**
     * 抖音审核内容
     */
    @ApiModelProperty("抖音审核内容")
    private String dyStatusContent;

    /**
     * 当前版本号
     */
    @ApiModelProperty("当前版本号")
    private String dyVersion;

    /**
     * 资质 许可证号
     */
    @ApiModelProperty("资质 许可证号")
    private String licenseNum;

    /**
     * 资质 登记号
     */
    @ApiModelProperty("资质 登记号")
    private String registrationNum;

    /**
     * 资质 普通备案号
     */
    @ApiModelProperty("资质 普通备案号")
    private String ordinaryRecordNum;

    /**
     * 资质 重点备案号
     */
    @ApiModelProperty("资质 重点备案号")
    private String keyRecordNum;

    /**
     * 微信短剧id
     */
    @ApiModelProperty("微信短剧id")
    private String wxCourseId;

    /**
     * 微信是否显示 1是
     */
    @ApiModelProperty("微信是否显示 1是")
    private Integer wxShow;

    /**
     * 抖音是否显示 1是
     */
    @ApiModelProperty("抖音是否显示 1是")
    private Integer dyShow;

    /**
     * 排序序列号
     */
    @ApiModelProperty("排序序列号")
    private Integer sequence;

    /**
     * 是否可砍剧
     */
    @ApiModelProperty("是否可砍剧")
    private Integer isCut;
    /**
     * 需邀请的用户数量
     */
    @ApiModelProperty("需邀请的用户数量")
    private Integer inviteTarget;
    /**
     * 每次砍剧获得的点券
     */
    @ApiModelProperty("每次砍剧获得的点券")
    private Integer rewardMoney;
    /**
     * 砍剧时间限制（小时）
     */
    @ApiModelProperty("砍剧时间限制（小时）")
    private Integer cutTimeLimit;

    /**
     * 地区id
     */
    @ApiModelProperty("地区id")
    private Integer areaId;

    /**
     * 平台id
     */
    @ApiModelProperty("平台id")
    private Integer platformId;


    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String remark;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String avatar;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String courseCount;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Integer isMyCourse;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Orders orders;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String courseDetailsName;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Long courseDetailsId;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Integer courseDetailsCount;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String produceEndTime;

    @ApiModelProperty("是否收藏")
    @TableField(exist = false)
    private Integer isCollect;

    @ApiModelProperty("是否看过")
    @TableField(exist = false)
    private Integer isView;

    @ApiModelProperty("用户评分")
    @TableField(exist = false)
    private BigDecimal userScore;

    @ApiModelProperty("真实分数")
    @TableField(exist = false)
    private BigDecimal realScore;

    @ApiModelProperty("演员列表")
    @TableField(exist = false)
    private List<Performer> performerList;

    @ApiModelProperty("演员IDs")
    @TableField(exist = false)
    private List<Long> performerIds;


    public Course() {
    }
}
