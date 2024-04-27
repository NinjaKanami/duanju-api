package com.sqx.modules.app.entity;

import lombok.Data;

import java.util.List;

@Data
public class AppUserInfo {



    private String openid;
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<String> privilege;


}
