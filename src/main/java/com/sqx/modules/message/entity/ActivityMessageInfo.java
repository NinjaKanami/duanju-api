package com.sqx.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/13
 */
@Data
@TableName("activity_message_info")
public class ActivityMessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private long id;

    private String createAt;

    private String content;

    private String title;

    private String image;

    private String url;

    private String sendState;

    private String sendTime;

    private String isSee;

    private String state;

    private String type;

    private String userId;

    private String userName;

    private String platform;

}