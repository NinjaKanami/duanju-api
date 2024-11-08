package com.sqx.modules.performer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@TableName(value = "ptag")
@Data
public class PTag implements Serializable {
    @NotNull(message = "类型id不能为空")
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "类型名称不能为空")
    @TableField("name")
    private String name;

    @TableField("is_visible")
    private Integer isVisible; // 演员类型是否在用户榜单页面可见

    @TableField("page_index")
    private Integer pageIndex; // 演员类型在用户端榜单页面的显示顺序
}
