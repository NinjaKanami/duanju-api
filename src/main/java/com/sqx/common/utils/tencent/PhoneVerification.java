package com.sqx.common.utils.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.*;

public class PhoneVerification {

    private final String secretId;
    private final String secretKey;

    public PhoneVerification(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    /**
     * 腾讯云手机号三要素核验
     * <p>文档地址: <a href="https://cloud.tencent.com/document/product/1007/39765">腾讯云-手机号三要素核验</a>
     * 返回结果的Result字段为0即为三要素一致。其他结果可以看文档</p>
     *
     * @param phone  手机号
     * @param idCard 身份证号
     * @param name   姓名
     */
    public PhoneVerificationResponse validate(String phone, String idCard, String name) throws TencentCloudSDKException {
        Credential cred = new Credential(this.secretId, this.secretKey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("faceid.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        FaceidClient client = new FaceidClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        PhoneVerificationRequest req = new PhoneVerificationRequest();
        req.setIdCard(idCard);
        req.setName(name);
        req.setPhone(phone);
        return client.PhoneVerification(req);
    }
}