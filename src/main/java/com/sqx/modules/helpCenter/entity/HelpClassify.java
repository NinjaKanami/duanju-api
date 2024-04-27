package com.sqx.modules.helpCenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description help_classify
 * @author fang
 * @date 2022-06-06
 */
@Data
public class HelpClassify implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 帮助中心分类
     */
    @TableId(type = IdType.AUTO)
    private Long helpClassifyId;

    /**
     * 分类名称
     */
    private String helpClassifyName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 类型
     */
    private Integer types;

    @TableField(exist = false)
    private List<HelpClassify> helpClassifyList;

    @TableField(exist = false)
    private List<HelpWord> helpWordList;

    public HelpClassify() {}
}