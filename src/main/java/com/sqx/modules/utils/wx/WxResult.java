package com.sqx.modules.utils.wx;

import lombok.Data;

/**
 * @author fang
 * @date 2020/12/10
 */
@Data
public class WxResult {

    /**
     * 返回状态码
     */
    private String return_code;

    /**
     * 返回信息
     */
    private String return_msg;

    /**
     * 商户appid
     */
    private String mch_appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 设备号
     */
    private String device_info;

    /**
     * 随机字符串
     */
    private String nonce_str;

    /**
     * 业务结果
     */
    private String result_code;

    /**
     * 错误代码
     */
    private String err_code;

    /**
     * 错误代码描述
     */
    private String err_code_des;

    /**
     * 商户订单号
     */
    private String partner_trade_no;

    /**
     * 微信付款单号
     */
    private String payment_no;

    /**
     * 付款成功时间
     */
    private String payment_time;


}