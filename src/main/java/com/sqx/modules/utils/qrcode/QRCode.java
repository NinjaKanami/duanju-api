package com.sqx.modules.utils.qrcode;

import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class QRCode {
    @Bean
    public QrConfig qrConfig(){
        QrConfig qrConfig=new QrConfig();
        qrConfig.setBackColor(Color.white.getRGB());
        qrConfig.setForeColor(Color.black.getRGB());
        return qrConfig;
    }
}
