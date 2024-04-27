package com.sqx.modules.pay.utils;

import java.security.PrivateKey;
import java.security.Signature;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;

public class DYSign {

    public static void main(String []args) {
        // 请求时间戳
        long timestamp = System.currentTimeMillis()/1000L;
        // 开发者填入自己的小程序app_id
        String appId = "testAppId";
        // 随机字符串
        String nonceStr = UUID.randomUUID().toString();
        // 应用公钥版本,每次重新上传公钥后需要更新,可通过「开发管理-开发设置-密钥设置」处获取
        String keyVersion = "1";
        // 应用私钥,用于加签 重要：1.测试时请修改为开发者自行生成的私钥;2.请勿将示例密钥用于生产环境;3.建议开发者不要将私钥文本写在代码中
        String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZSHNcFfthd/bV\nYexEJWOBVEjjDcXjfr1fYevuraNFfMmLPKV836BbvCiUSWHzJYEpkJ934e/j28NB\nEcEbPDLiGlLTd6AVwR22TkUwpLr41oQprz0HKFwhVPZ0HQCGIv0pVMA53TFSitIq\niqbNLmgm5yzSNqNy1t/0X/RfqEtA6Eoxw9u/Sx57i+pBFuLlZYanlm57+b7t1khg\n9JHvF0ulo7DScyJ4qgrD7oQf0RIQB0rqCFIeYuYO1cfvnxb9x4DPodEyVoAM4i9Y\ndFop9ZHt73W/icuLku/P8/G1+arzB5b7S1S3ky5/KdS8AEA9Ww5czZcdf9Jgm2S6\nRymjFGjzAgMBAAECggEBAIryGNgdePyWcSJmHHR9a+CdFWD0aDBa/7CJpAN8VKc1\ngcB8Xgp+7+6X9jTM/EQa+CVEWrmiDgF/gVPnkyNsAzff4rqcEnoFzzglZSS9/lp4\nod7jYa+uTy1LxgflDkeJSfEASStqrT4EZpR3kNInQfQZ1BBNxQXhb6smm+9mL6kK\nQJjAqBgEqtUAmNv0GnH89ZPPgZuIZeeL4cb4BhMEoa5MBnI+HDf07cN1nECQXRJl\nHU/iyhAPfP7RpO8O9KGDEDE36qebu0Cu4yUjWANXiqECFv93sQzONotkl3VPealv\nXM+jzGT7YdgHo/t3QKE8flMBo/XUzGTqi8j5AOXiaBkCgYEAylKVtjQMYgg4qMwd\n9Je+KZ9qL6QVHCsB2NPUt8N99oj70efsG4aGaEAadr8meNhIJ5lpoK+FXqSBIbbD\nS/xeOVI3XoMx/EdKLw/ZNi87G/EHYK9z7Fr3W7q8DFXe2hZ/ojFXC/aaBanjVVBK\n/6RfPzXfnx+vGX/t1FhcLC+yQD8CgYEAwfMtrXfH+3dW77dxXT/CTFJVs/o1K2qb\nepnQ6A33KMHPLBtPZZ6z5rzIO7OMSNItOTXTEoRXHmOKc5FtXGtbCvGSRByb6FgD\nWG3p2Bp2sZLuJBzXmLbSnEbHTNHM6uTgxNgWAh8pYpjPY8xF7BqYz2rGT47OPBmc\ntRzDjnzjak0CgYAqkM1mk/S2+zvQZ4E14GblouBYPZEjZ/jvgUGTl9F8eL1iIAUQ\nlXDZpgLrULPrYLVtf101rTfF/Z4dVbIo3mOEc8OqYre1d9onpJHyUGWDL2Z59O/S\nniDEb7j4b2h/QZSArxi9L5if8GofnNDqj85qIg92Dthr6PpEXoKl2TMLSQKBgFzQ\nBHHYukiqSV4ZyRQ4qMBhPkYMXFlUgObgqMoDtN06MewHfa1BjxHCEYgQWfeXLLEO\nAt3/mrkeJWk8lLr/XOgVxkr17d34EFHG93rE3zwG9hMuAjZAdvT2IfWvCIL32GAa\nkB2fz+ww+D3nySY9bBcGH7R+wE6eaxF4nFSZizKZAoGBAJzuaWCnVK0djfgvUsjm\nGUtyDvgyREcpAsXvES1pB2NLVeEUxm0uRtj6k4DhCv3rJfUwfMr0+sa9NUnXuaSR\nVqLYvAD8bNPKXwn7ymzQ7WioCqmZuUhLnQRppkjhfQGKLH0MnMw9Xh9FwJ9kzGNE\nUnTEhaaHsoaHMlLlRET32gyG".replace("\n","");
        // 生成好的data
        String data = "{\"skuList\":[{\"skuId\":\"1\",\"price\":9999,\"quantity\":1,\"title\":\"标题\",\"imageList\":[\"https://dummyimage.com/234x60\"],\"type\":401,\"tagGroupId\":\"idxxx\"}],\"outOrderNo\":\"1213\",\"totalAmount\":9999,\"limitPayWayList\":[],\"payExpireSeconds\":3000,\"orderEntrySchema\":{\"path\":\"page/index/index\",\"params\":\"{\\\"poi\\\":\\\"6601248937917548558\\\",\\\"aweme_useTemplate\\\":1}\"}}";

        String byteAuthorization = getByteAuthorization(privateKeyStr, data, appId, nonceStr, timestamp, keyVersion);
        System.out.println(byteAuthorization);
    }

    public static String getByteAuthorization(String privateKeyStr, String data, String appId, String nonceStr, long timestamp, String keyVersion) {
        String byteAuthorization = "";
        try {
            // 生成签名
            String signature = getSignature(privateKeyStr, "POST", "/requestOrder", timestamp, nonceStr, data);
            // 构造byteAuthorization
            StringBuilder sb = new StringBuilder();
            sb.append("SHA256-RSA2048 ").
                    append("appid=").append(appId).append(",").
                    append("nonce_str=").append(nonceStr).append(",").
                    append("timestamp=").append(timestamp).append(",").
                    append("key_version=").append(keyVersion).append(",").
                    append("signature=").append(signature);
            byteAuthorization = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return byteAuthorization;
    }

    public static String getSignature(String privateKeyStr, String method, String uri, long timestamp, String nonce, String data) throws Exception {
        String rawStr = method + "\n" +
                uri + "\n" +
                timestamp + "\n" +
                nonce + "\n" +
                data + "\n";
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(string2PrivateKey(privateKeyStr));
        sign.update(rawStr.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static PrivateKey string2PrivateKey(String privateKeyStr) {
        PrivateKey prvKey = null;
        try {
            byte[] privateBytes = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            prvKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prvKey;
    }
}

