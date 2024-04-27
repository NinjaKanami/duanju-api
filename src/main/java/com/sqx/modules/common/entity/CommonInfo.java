package com.sqx.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用配置管理
 */
@Data
@TableName("common_info")
public class CommonInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    private long id;

    private String createAt;

    private Integer type; //1表示客服二维码 2表示公众号二维码 3表示全局佣金是否开启 4注册客服渠道id配置 5、佣金规则 6、

    private String value;

    private String max;

    private String min;

    private String conditionFrom;

}

