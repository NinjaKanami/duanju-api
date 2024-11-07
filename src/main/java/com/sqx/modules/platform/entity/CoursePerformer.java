package com.sqx.modules.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName course_performer
 */
@TableName(value ="course_performer")
@Data
public class CoursePerformer implements Serializable {
    /**
     * 短剧_演员id
     */
    @TableId(value = "course_performer_id", type = IdType.AUTO)
    private Integer coursePerformerId;

    /**
     * 短剧id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 演员id
     */
    @TableField(value = "performer_id")
    private Integer performerId;

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
        CoursePerformer other = (CoursePerformer) that;
        return (this.getCoursePerformerId() == null ? other.getCoursePerformerId() == null : this.getCoursePerformerId().equals(other.getCoursePerformerId()))
            && (this.getCourseId() == null ? other.getCourseId() == null : this.getCourseId().equals(other.getCourseId()))
            && (this.getPerformerId() == null ? other.getPerformerId() == null : this.getPerformerId().equals(other.getPerformerId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCoursePerformerId() == null) ? 0 : getCoursePerformerId().hashCode());
        result = prime * result + ((getCourseId() == null) ? 0 : getCourseId().hashCode());
        result = prime * result + ((getPerformerId() == null) ? 0 : getPerformerId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", coursePerformerId=").append(coursePerformerId);
        sb.append(", courseId=").append(courseId);
        sb.append(", performerId=").append(performerId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
