package com.sqx.modules.box.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Zbc
 * @TableName box_online
 */
@TableName(value = "box_online")
@Data
public class BoxOnline implements Serializable {
    /**
     * 盲盒积分id
     */
    @TableId(value = "box_online_id", type = IdType.AUTO)
    private Long boxOnlineId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 分钟
     */
    @TableField(value = "minute")
    private Integer minute;

    /**
     * 下一次获得盲盒需要的分钟
     */
    @TableField(value = "next_minute")
    private Integer nextMinute;

    /**
     * 今日已获得盲盒
     */
    @TableField(value = "reward")
    private Integer reward;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BoxOnline other = (BoxOnline) that;
        return (this.getBoxOnlineId() == null ? other.getBoxOnlineId() == null : this.getBoxOnlineId().equals(other.getBoxOnlineId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getMinute() == null ? other.getMinute() == null : this.getMinute().equals(other.getMinute()))
                && (this.getNextMinute() == null ? other.getNextMinute() == null : this.getNextMinute().equals(other.getNextMinute()))
                && (this.getReward() == null ? other.getReward() == null : this.getReward().equals(other.getReward()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBoxOnlineId() == null) ? 0 : getBoxOnlineId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMinute() == null) ? 0 : getMinute().hashCode());
        result = prime * result + ((getNextMinute() == null) ? 0 : getNextMinute().hashCode());
        result = prime * result + ((getReward() == null) ? 0 : getReward().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", boxOnlineId=").append(boxOnlineId);
        sb.append(", userId=").append(userId);
        sb.append(", minute=").append(minute);
        sb.append(", nextMinute=").append(nextMinute);
        sb.append(", reward=").append(reward);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
