package com.sqx.modules.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动推广
 */
@Data
@TableName("activity")
public class Activity implements Serializable {
    @TableId(type = IdType.INPUT)
    private Long id;
    
    private String createAt;
    
    private String imageUrl;
    
    private String url;
    
    private String title;

    private String state;

}
