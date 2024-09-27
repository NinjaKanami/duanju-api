package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * (CourseCut)实体类
 *
 * @author makejava
 * @since 2024-09-26 19:30:52
 */
@Data
@TableName("course_cut")
public class CourseCut implements Serializable {
    private static final long serialVersionUID = 145397240402778582L;
    /**
     * 砍剧ID
     */
    @NotNull(message = "cutId 不能为空")
    @TableId(type = IdType.AUTO)
    private Long cutId;
    /**
     * 短剧ID
     */
    @NotNull(message = "userId 不能为空")
    private Long courseId;
    /**
     * 发起砍剧用户的ID
     */
    @NotNull(message = "userId 不能为空")
    private Long userId;
    /**
     * 砍剧状态 0砍剧中 1已砍剧 2已超时(弃用)
     */
    private Integer cutStatus;
    /**
     * 邀请的用户数量
     */
    private Integer inviteCount;
    /**
     * 已邀请的用户数量
     */
    private Integer invitedCount;
    /**
     * 每次砍剧获得的点券
     */
    private Integer rewardMoney;
    /**
     * 截止时间
     */
    private String deadline;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

    @TableField(exist = false)
    private List<CourseCutInvite> courseCutInviteList;

}
