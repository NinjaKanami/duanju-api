package com.sqx.modules.utils;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author fang
 * @date 2020/2/26
 */
@Data
public class WXConfigUtil implements WXPayConfig {
    private  byte[] certData;
    private  String appId = "";
    private  String key = "";
    private  String mchId = "";


    //初始化加载证书
    public WXConfigUtil(String filePath) throws Exception {

        File file = new File(filePath);
        InputStream fis = null;
        try {
            fis = Files.newInputStream(file.toPath());
            this.certData = IOUtils.toByteArray(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

    }


    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }


}
