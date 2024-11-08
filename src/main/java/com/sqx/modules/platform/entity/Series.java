package com.sqx.modules.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName series
 */
@TableName(value ="series")
@Data
public class Series implements Serializable {
    /**
     * 剧单id
     */
    @TableId(value = "series_id", type = IdType.AUTO)
    private Long seriesId;

    /**
     * 剧单头图
     */
    @TableField(value = "series_img")
    private String seriesImg;

    /**
     * 剧单名
     */
    @TableField(value = "series_name")
    private String seriesName;

    /**
     * 剧单描述
     */
    @TableField(value = "series_describe")
    private String seriesDescribe;

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

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

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
        Series other = (Series) that;
        return (this.getSeriesId() == null ? other.getSeriesId() == null : this.getSeriesId().equals(other.getSeriesId()))
            && (this.getSeriesImg() == null ? other.getSeriesImg() == null : this.getSeriesImg().equals(other.getSeriesImg()))
            && (this.getSeriesName() == null ? other.getSeriesName() == null : this.getSeriesName().equals(other.getSeriesName()))
            && (this.getSeriesDescribe() == null ? other.getSeriesDescribe() == null : this.getSeriesDescribe().equals(other.getSeriesDescribe()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSeriesId() == null) ? 0 : getSeriesId().hashCode());
        result = prime * result + ((getSeriesImg() == null) ? 0 : getSeriesImg().hashCode());
        result = prime * result + ((getSeriesName() == null) ? 0 : getSeriesName().hashCode());
        result = prime * result + ((getSeriesDescribe() == null) ? 0 : getSeriesDescribe().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", seriesId=").append(seriesId);
        sb.append(", seriesImg=").append(seriesImg);
        sb.append(", seriesName=").append(seriesName);
        sb.append(", seriesDescribe=").append(seriesDescribe);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", sort=").append(sort);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
