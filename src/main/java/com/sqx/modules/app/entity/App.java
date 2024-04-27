package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告位
 */
@Data
@TableName("app")
public class App implements Serializable {
    @TableId
    private Long id;
    
    private String createAt;
    
    private String androidWgtUrl;
    
    private String iosWgtUrl;
    
    private String wgtUrl;
    
    private String version;

    private String iosVersion;
    
    private String method;
    
    private String des;

}

