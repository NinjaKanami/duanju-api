package com.sqx.modules.performer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@TableName(value = "performer")
@Data
public class Performer implements Serializable {
    @NotNull(message = "演员id不能为空")
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "演员名称不能为空")
    private String name;

    @NotNull(message = "演员性别不能为空")
    private Integer sex;

    @TableField("company")
    private String company; // 公司

    @TableField("profile")
    private String profile; // 简介

    @TableField("fake_follower")
    private Long fakeFollower; // 虚拟粉丝数

    @TableField("photo")
    private String photo; // 照片URL

    @TableField(exist = false)
    private Long realFollower; // 真实粉丝数

    @TableField(exist = false)
    private Long totalFollower; // 真实粉丝数

    @TableField(exist = false)
    private String tagsStr; // 标签id列表, 使用","分隔, 出参格式: "id:类型名, id:类型名"

    @TableField(exist = false)
    private String courseStr; // 标签名称列表, 使用","分隔, 出参格式: "id:剧名, id:剧名"
}
