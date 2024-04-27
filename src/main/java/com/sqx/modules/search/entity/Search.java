package com.sqx.modules.search.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("search")
public class Search implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 搜索id
     */
    @TableId(type = IdType.AUTO)
    private Long searchId;
    /**
     * 搜索名称
     */
    private String searchName;
    /**
     * 用户
     */
    private Long userId;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;

    public Search() {}
}
