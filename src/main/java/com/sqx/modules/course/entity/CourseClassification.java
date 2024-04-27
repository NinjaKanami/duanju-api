package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description CourseClassification  短剧分类
 * @author wang
 * @date 2021-03-29
 */
@Data
@TableName("course_classification")
public class CourseClassification implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Long classificationId;
    /**
     * 分类名称
     */
    private String classificationName;

    /**
     * 推荐视频id
     */
    private Long courseId;

    @TableField(exist = false)
    private Map<String,Object> course;

    @TableField(exist = false)
    private CourseDetails courseDetails;

    /**
     * 是否删除0正常1已删除
     */
    private Integer isDelete;

    /**
     * 排序
     */
    private Integer sort;


}
