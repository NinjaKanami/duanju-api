package com.sqx.modules.utils.wx;

/**
 * @author fang
 * @date 2020/12/10
 */
public enum SignType {
    /**
     * HMAC-SHA256 加密
     */
    HMACSHA256("HMAC-SHA256"),
    /**
     *  MD5 加密
     */
    MD5("MD5"),
    /**
     * RSA
     */
    RSA("RSA");

    SignType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }
}