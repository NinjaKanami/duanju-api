package com.sqx.modules.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author fang
 * @date 2020/7/9
 */
@Data
@TableName("banner")
@AllArgsConstructor
@NoArgsConstructor
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * banner图id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 状态 1正常 2隐藏
     */
    private Integer state;

    /**
     * 分类 1 banner图  2 首页分类
     */
    private Integer classify;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String describes;
    /**
     * 短剧信息
     */
    @TableField(exist = false)
    private List<Course> course;

}