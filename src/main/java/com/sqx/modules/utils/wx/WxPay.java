package com.sqx.modules.utils.wx;

import lombok.Data;

/**
 * @author fang
 * @date 2020/12/10
 */
@Data
public class WxPay {

    /**
     * 与商户号关联应用(如微信公众号/小程序)的APPID
     */
    private String mch_appid;

    /**
     * 微信支付分配的商户号
     */
    private String mchid;

    /**
     * 微信支付分配的终端设备号
     */
    private String device_info;

    /**
     * 随机字符串，不长于32位
     */
    private String nonce_str;

    /**
     * 签名
     */
    private String sign;

    /**
     * 商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
     */
    private String partner_trade_no;

    /**
     * 商户appid下，某用户的openid
     */
    private String openid;

    /**
     * NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
     */
    private String check_name;

    /**
     * 收款用户真实姓名。
     * 强校验必填项
     */
    private String re_user_name;

    /**
     * 企业付款金额，单位为分
     */
    private Integer amount;


    /**
     * 企业付款备注
     */
    private String desc;

    /**
     * 发起者IP地址+该IP可传用户端或者服务端的IP。
     */
    private String spbill_create_ip;


}