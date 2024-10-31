package com.sqx.modules.box.util;

import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.box.vo.BoxCollection;
import com.sqx.modules.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/10/25 13:12
 */
@Slf4j
@Component
public class DataSync {
    private static final String APP_ID = "07ce409fcefc452196481618fbe40acd";
    private static final String APP_SECRET = "E760D5B0-6A12-4A5F-9B56-D1D9D45DCD48";
    private static final String API_URL1 = "http://feifanapitest.feifan.art/out/garonne/v1/dragon/donation";
    private static final String API_URL2 = "http://feifanapitest.feifan.art/out/garonne/v1/user";


    public BoxCollection getUserCollection(String phone) {
        // 创建 BoxCollection 对象并设置属性
        BoxCollection data = new BoxCollection(0, BigDecimal.valueOf(0), false);
        try {
            // 生成时间戳
            String timestamp = String.valueOf(System.currentTimeMillis());

            // 生成签名
            String signature = getSign();

            // 构建请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("appId", APP_ID);
            headers.put("signature", signature);
            headers.put("timestamp", timestamp);
            headers.put("Content-Type", "application/json; charset=utf-8");

            // http://feifanapitest.feifan.art/out/garonne/v1/user?userMobile=12345678909

            // 构建请求 URL
            String url = API_URL2 + "?userMobile=" + URLEncoder.encode(phone, StandardCharsets.UTF_8.toString());

            // 发送 GET 请求
            String response = sendGetRequest(url, headers);

            // 解析响应
            JSONObject responseJson = JSONObject.parseObject(response);
            int ecode = responseJson.getIntValue("ecode");
            String emessage = responseJson.getString("emessage");
            JSONObject dataJson = responseJson.getJSONObject("data");


            data.setRegistered(dataJson.getBooleanValue("registered"));
            data.setCollect(dataJson.getIntValue("dragonNum"));
            data.setCollectPoint(dataJson.getBigDecimal("dragonScaleNum"));

            // 处理响应
            switch (ecode) {
                case 0:
                    // 成功
                    log.info("获取用户收藏成功: {}", data);
                    return data;
                case 501000:
                    // 验签参数错误
                    log.error("验签参数错误: {}", emessage);
                    throw new Exception("验签参数错误: " + emessage);
                case 501001:
                    // 签名已失效
                    log.error("签名已失效: {}", emessage);
                    throw new Exception("签名已失效: " + emessage);
                case 501002:
                    // 签名密钥未配置
                    log.error("签名密钥未配置: {}", emessage);
                    throw new Exception("签名密钥未配置: " + emessage);
                case 501003:
                    // 签名商户不存在
                    log.error("签名商户不存在: {}", emessage);
                    throw new Exception("签名商户不存在: " + emessage);
                case 501004:
                    // 签名非法
                    log.error("签名非法: {}", emessage);
                    throw new Exception("签名非法: " + emessage);
                case 501005:
                    // 签名过期
                    log.error("签名过期: {}", emessage);
                    throw new Exception("签名过期: " + emessage);
                default:
                    // 其他非0码
                    log.error("未知错误: {}", emessage);
                    throw new Exception("未知错误: " + emessage);
            }
        } catch (Exception e) {
            log.error("获取用户收藏失败: {}", e.getMessage());
            return data;
        }
    }

    public boolean syncUserCollection(String phone, BigDecimal exchangeNum, int exchangeType, String reqUUID) {
        try {
            // 生成时间戳
            String timestamp = String.valueOf(System.currentTimeMillis());

            // 生成签名
            String signature = getSign();

            // 构建请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("appId", APP_ID);
            headers.put("signature", signature);
            headers.put("timestamp", timestamp);
            headers.put("Content-Type", "application/json; charset=utf-8");

            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("exchangeNum", exchangeNum);
            requestBody.put("userMobile", phone);
            if (exchangeType != 0) {
                requestBody.put("exchangeType", exchangeType);
            }
            if (exchangeType == 1 && reqUUID != null) {
                requestBody.put("reqUUID", reqUUID);
            }

            // 发送 POST 请求
            String response = sendPostRequest(API_URL1, headers, requestBody.toJSONString());

            // 解析响应
            JSONObject responseJson = JSONObject.parseObject(response);
            int ecode = responseJson.getIntValue("ecode");
            String emessage = responseJson.getString("emessage");
            boolean data = responseJson.getBooleanValue("data");
            long timestampResponse = responseJson.getLongValue("timestamp");

            // 处理响应
            switch (ecode) {
                case 0:
                    // 成功
                    System.out.println("兑换成功: " + data);
                    break;
                case 501912:
                    // 幂等成功
                    System.out.println("幂等兑换成功: " + data);
                    data = true;
                    break;
                case 501000:
                    // 验签参数错误
                    log.error("验签参数错误: {}", emessage);
                    throw new Exception("验签参数错误: " + emessage);
                case 501001:
                    // 签名已失效
                    log.error("签名已失效: {}", emessage);
                    throw new Exception("签名已失效: " + emessage);
                case 501002:
                    // 签名密钥未配置
                    log.error("签名密钥未配置: {}", emessage);
                    throw new Exception("签名密钥未配置: " + emessage);
                case 501003:
                    // 签名商户不存在
                    log.error("签名商户不存在: {}", emessage);
                    throw new Exception("签名商户不存在: " + emessage);
                case 501004:
                    // 签名非法
                    log.error("签名非法: {}", emessage);
                    throw new Exception("签名非法: " + emessage);
                case 501005:
                    // 签名过期
                    log.error("签名过期: {}", emessage);
                    throw new Exception("签名过期: " + emessage);
                default:
                    // 其他非0码
                    log.error("未知错误: {}", emessage);
                    throw new Exception("未知错误: " + emessage);
            }
            return data;
        } catch (Exception e) {
            log.error("兑换失败: {}", e.getMessage());
            return false;
        }
    }


    public String getSign() throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String date = APP_ID + "&" + APP_SECRET + "&" + timestamp;

        SecretKeySpec signingKey = new SecretKeySpec(
                APP_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return Base64
                .getEncoder()
                .encodeToString(
                        mac.doFinal(date.getBytes(StandardCharsets.UTF_8))
                );
    }


    private String sendPostRequest(String url, Map<String, String> headers, String requestBody) {
        // 创建 HttpClient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 HttpPost 实例
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }

        // 设置请求体
        try {
            StringEntity entity = new StringEntity(requestBody, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
        } catch (Exception e) {
            throw new RuntimeException("设置请求体时发生异常: " + e.getMessage());
        }

        // 设置超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)  // 连接超时时间，单位为毫秒
                .setSocketTimeout(3000)   // 读取超时时间，单位为毫秒
                .build();
        httpPost.setConfig(requestConfig);

        // 发送请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                throw new Exception("HTTP 请求失败: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("HTTP 请求失败: {}", e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                log.error("关闭资源时发生异常: {}", e.getMessage());
            }
        }
        return null;
    }


    private String sendGetRequest(String url, Map<String, String> headers) {
        // 创建 HttpClient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 HttpGet 实例
        HttpGet httpGet = new HttpGet(url);

        // 设置请求头
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        // 设置超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)  // 连接超时时间，单位为毫秒
                .setSocketTimeout(3000)   // 读取超时时间，单位为毫秒
                .build();
        httpGet.setConfig(requestConfig);

        // 发送请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                throw new Exception("HTTP 请求失败: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("HTTP 请求失败: {}", e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                log.error("关闭资源时发生异常: {}", e.getMessage());
            }
        }
        return null;
    }
}
