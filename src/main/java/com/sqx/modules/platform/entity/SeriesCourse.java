package com.sqx.modules.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName series_course
 */
@TableName(value ="series_course")
@Data
public class SeriesCourse implements Serializable {
    /**
     * 剧单_短剧id
     */
    @TableId(value = "series_course_id", type = IdType.AUTO)
    private Long seriesCourseId;

    /**
     * 剧单id
     */
    @TableField(value = "series_id")
    private Long seriesId;

    /**
     * 短剧id
     */
    @TableField(value = "course_id")
    private Long courseId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public SeriesCourse() {
    }

    public SeriesCourse(Long seriesId, Long courseId) {
        this.seriesId = seriesId;
        this.courseId = courseId;
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
        SeriesCourse other = (SeriesCourse) that;
        return (this.getSeriesCourseId() == null ? other.getSeriesCourseId() == null : this.getSeriesCourseId().equals(other.getSeriesCourseId()))
            && (this.getSeriesId() == null ? other.getSeriesId() == null : this.getSeriesId().equals(other.getSeriesId()))
            && (this.getCourseId() == null ? other.getCourseId() == null : this.getCourseId().equals(other.getCourseId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSeriesCourseId() == null) ? 0 : getSeriesCourseId().hashCode());
        result = prime * result + ((getSeriesId() == null) ? 0 : getSeriesId().hashCode());
        result = prime * result + ((getCourseId() == null) ? 0 : getCourseId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", seriesCourseId=").append(seriesCourseId);
        sb.append(", seriesId=").append(seriesId);
        sb.append(", courseId=").append(courseId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
