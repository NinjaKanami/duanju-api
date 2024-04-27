package com.sqx.modules.utils.wx;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fang
 * @date 2020/12/10
 */
public class WxPayUtils {

    private static final String FIELD_SIGN = "sign";

    public static final String WX_COM_DO_TRANS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 创建签名
     * @param map 方法
     * @param paterNerkey api密钥
     * @return
     */
    public static String createSign(Map<String, Object> map, String paterNerkey) {
        return createSign(map, paterNerkey, SignType.MD5);
    }

    public static String createSign(Map<String, Object> map, String partnerKey, SignType signType) {
        map.remove(FIELD_SIGN);

        String sign = createLinkString(map, "&");

        String SignTemp = sign += "&key=" + partnerKey;

        if (signType == SignType.MD5) {
            return md5(SignTemp).toUpperCase();
        } else {
            return hmacSha256(SignTemp, partnerKey).toUpperCase();
        }

    }

    /**
     * 生成MD5字符串
     * SecureUtil 来自 hutool
     * @param data 数据
     * @return MD5字符串
     */
    public static String md5(String data) {
        return SecureUtil.md5(data);
    }

    /**
     * 生成16进制的 sha256 字符串
     * SecureUtil 来自 hutool
     * @param data 数据
     * @param key  密钥
     * @return sha256 字符串
     */
    public static String hmacSha256(String data, String key) {
        return SecureUtil.hmac(HmacAlgorithm.HmacSHA256, key).digestHex(data, CharsetUtil.UTF_8);
    }

    /**
     * 排序并拼接
     * @param map 需要排序并拼接的map
     * @param delimiter 拼接符
     * @return 拼接字符串
     */
    public static String createLinkString(Map<String, Object> map, String delimiter) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        String sign = keys.stream()
                .filter(k -> !Objects.isNull(map.get(k)))
                .map(key -> {
                    return key + "=" + map.get(key).toString();
                })
                .collect(Collectors.joining(delimiter));

        return sign;
    }


    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String getGeneralOrder(){
        Date date=new Date();
        String newString = String.format("%0"+4+"d", (int)((Math.random()*9+1)*1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format+newString;
    }

}