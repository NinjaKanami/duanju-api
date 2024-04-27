package com.sqx.modules.pay.config;

/**
 * @author WALKMAN
 * @Description: 支付宝支付参数
 **/
public class AliPayConstants {

    /**
     * 支付宝环境
     */
    public static final String REQUEST_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 编码格式
     */
    public static String CHARSET = "UTF-8";

    /**
     * 参数格式
     */
    public static String FORMAT = "json";

    /**
     * 加密方式
     */
    public static String SIGNTYPE = "RSA2";

    /**
     * 支付类型-提现(固定)
     */
    public static String PAY_TYPE = "ALIPAY_LOGONID";

    /**
     * 平台和支付宝签约属性-固定值
     */
    public static String PRODUCT_CODE = "QUICK_WAP_WAY";

    /**
     * 支付宝提现成功状态
     */
    public static String SUCCESS_CODE = "10000";

}
