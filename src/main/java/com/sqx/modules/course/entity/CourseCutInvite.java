package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * (CourseCutInvite)实体类
 *
 * @author makejava
 * @since 2024-09-26 19:37:44
 */
@Data
@TableName("course_cut_invite")
public class CourseCutInvite implements Serializable {
    private static final long serialVersionUID = -99055484085081732L;
    /**
     * 邀请ID
     */
    @NotNull(message = "inviteId 不能为空")
    @TableId(type = IdType.AUTO)
    private Long inviteId;
    /**
     * 砍剧ID
     */
    @NotNull(message = "cutId 不能为空")
    private Long cutId;
    /**
     * 被邀请用户ID
     */
    @NotNull(message = "invitedUserId 不能为空")
    private Long invitedUserId;
    /**
     * 助力时间
     */
    private String invitedTime;

}
