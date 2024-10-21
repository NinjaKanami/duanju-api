package com.sqx.modules.box.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Zbc
 * @TableName box_item
 */
@TableName(value = "box_item")
@Data
public class BoxItem implements Serializable {
    /**
     * 盲盒物品
     */
    @TableId(value = "box_item_id")
    private Long boxItemId;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 是否积分
     */
    @TableField(value = "is_Point")
    private Integer isPoint;

    /**
     * 积分值
     */
    @TableField(value = "value")
    private Integer value;

    /**
     * 图片url
     */
    @TableField(value = "img")
    private String img;

    /**
     * 机率
     */
    @TableField(value = "odds")
    private BigDecimal odds;

    /**
     * 份额
     */
    @TableField(value = "share")
    private Long share;

    /**
     * 是否可随机
     */
    @TableField(value = "is_random")
    private Integer isRandom;

    /**
     * 剩余数量
     */
    @TableField(value = "remains")
    private Integer remains;

    /**
     * 击中次数
     */
    @TableField(value = "hit")
    private Long hit;

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

    public BoxItem() {
    }

    public BoxItem(String name, String img) {
        this.name = name;
        this.img = img;
    }

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
        BoxItem other = (BoxItem) that;
        return (this.getBoxItemId() == null ? other.getBoxItemId() == null : this.getBoxItemId().equals(other.getBoxItemId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getIsPoint() == null ? other.getIsPoint() == null : this.getIsPoint().equals(other.getIsPoint()))
                && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
                && (this.getImg() == null ? other.getImg() == null : this.getImg().equals(other.getImg()))
                && (this.getOdds() == null ? other.getOdds() == null : this.getOdds().equals(other.getOdds()))
                && (this.getShare() == null ? other.getShare() == null : this.getShare().equals(other.getShare()))
                && (this.getIsRandom() == null ? other.getIsRandom() == null : this.getIsRandom().equals(other.getIsRandom()))
                && (this.getRemains() == null ? other.getRemains() == null : this.getRemains().equals(other.getRemains()))
                && (this.getHit() == null ? other.getHit() == null : this.getHit().equals(other.getHit()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBoxItemId() == null) ? 0 : getBoxItemId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getIsPoint() == null) ? 0 : getIsPoint().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getImg() == null) ? 0 : getImg().hashCode());
        result = prime * result + ((getOdds() == null) ? 0 : getOdds().hashCode());
        result = prime * result + ((getShare() == null) ? 0 : getShare().hashCode());
        result = prime * result + ((getIsRandom() == null) ? 0 : getIsRandom().hashCode());
        result = prime * result + ((getRemains() == null) ? 0 : getRemains().hashCode());
        result = prime * result + ((getHit() == null) ? 0 : getHit().hashCode());
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
        sb.append(", boxItemId=").append(boxItemId);
        sb.append(", name=").append(name);
        sb.append(", isPoint=").append(isPoint);
        sb.append(", value=").append(value);
        sb.append(", img=").append(img);
        sb.append(", odds=").append(odds);
        sb.append(", share=").append(share);
        sb.append(", isRandom=").append(isRandom);
        sb.append(", remains=").append(remains);
        sb.append(", hit=").append(hit);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
