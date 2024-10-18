package com.sqx.modules.box.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName collect_log
 */
@TableName(value ="collect_log")
@Data
public class CollectLog implements Serializable {
    /**
     * 藏品记录id
     */
    @TableId(value = "collect_log_id", type = IdType.AUTO)
    private Long collectLogId;

    /**
     * 数据id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 0碎片 1藏品
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 获得数量
     */
    @TableField(value = "plus")
    private Integer plus;

    /**
     * 消耗数量
     */
    @TableField(value = "reduce")
    private Integer reduce;

    /**
     * 获得物品
     */
    @TableField(value = "item_name")
    private String itemName;

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
        CollectLog other = (CollectLog) that;
        return (this.getCollectLogId() == null ? other.getCollectLogId() == null : this.getCollectLogId().equals(other.getCollectLogId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getPlus() == null ? other.getPlus() == null : this.getPlus().equals(other.getPlus()))
            && (this.getReduce() == null ? other.getReduce() == null : this.getReduce().equals(other.getReduce()))
            && (this.getItemName() == null ? other.getItemName() == null : this.getItemName().equals(other.getItemName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCollectLogId() == null) ? 0 : getCollectLogId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getPlus() == null) ? 0 : getPlus().hashCode());
        result = prime * result + ((getReduce() == null) ? 0 : getReduce().hashCode());
        result = prime * result + ((getItemName() == null) ? 0 : getItemName().hashCode());
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
        sb.append(", collectLogId=").append(collectLogId);
        sb.append(", userId=").append(userId);
        sb.append(", type=").append(type);
        sb.append(", plus=").append(plus);
        sb.append(", reduce=").append(reduce);
        sb.append(", itemName=").append(itemName);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
