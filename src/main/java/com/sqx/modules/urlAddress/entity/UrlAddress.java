package com.sqx.modules.urlAddress.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description url_address
 * @author fang
 * @date 2024-01-10
 */
@Data
public class UrlAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 域名池id
     */
    private Integer urlId;

    /**
     * 域名地址
     */
    private String urlAddress;

    /**
     * 使用次数
     */
    private Integer num;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    public UrlAddress() {}
}
