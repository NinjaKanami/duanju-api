package com.sqx.modules.box.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName box_log
 */
@TableName(value ="box_log")
@Data
public class BoxLog implements Serializable {
    /**
     * 盲盒记录id
     */
    @TableId(value = "box_log_id", type = IdType.AUTO)
    private Long boxLogId;

    /**
     * 数据id
     */
    @TableField(value = "foreign_id")
    private Long foreignId;

    /**
     * 0积分 1盲盒 3观剧
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 0加 1减
     */
    @TableField(value = "action")
    private Integer action;

    /**
     * 数量
     */
    @TableField(value = "num")
    private Integer num;

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
        BoxLog other = (BoxLog) that;
        return (this.getBoxLogId() == null ? other.getBoxLogId() == null : this.getBoxLogId().equals(other.getBoxLogId()))
            && (this.getForeignId() == null ? other.getForeignId() == null : this.getForeignId().equals(other.getForeignId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAction() == null ? other.getAction() == null : this.getAction().equals(other.getAction()))
            && (this.getNum() == null ? other.getNum() == null : this.getNum().equals(other.getNum()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBoxLogId() == null) ? 0 : getBoxLogId().hashCode());
        result = prime * result + ((getForeignId() == null) ? 0 : getForeignId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAction() == null) ? 0 : getAction().hashCode());
        result = prime * result + ((getNum() == null) ? 0 : getNum().hashCode());
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
        sb.append(", boxLogId=").append(boxLogId);
        sb.append(", foreignId=").append(foreignId);
        sb.append(", type=").append(type);
        sb.append(", action=").append(action);
        sb.append(", num=").append(num);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
