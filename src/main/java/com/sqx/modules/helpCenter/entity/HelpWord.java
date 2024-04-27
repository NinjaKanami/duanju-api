package com.sqx.modules.helpCenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description help_word
 * @author fang
 * @date 2022-06-06
 */
@Data
public class HelpWord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 帮助文档id
     */
    private Long helpWordId;

    /**
     * 帮助标题
     */
    private String helpWordTitle;

    /**
     * 帮助分类
     */
    private Integer helpClassifyId;

    /**
     * 帮助文档内容
     */
    private String helpWordContent;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private String createTime;

    public HelpWord() {}
}
