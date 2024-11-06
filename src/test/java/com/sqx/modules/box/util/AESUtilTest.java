package com.sqx.modules.box.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zbc
 * @version 1.0
 * @since 2024/11/6 11:03
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AESUtilTest {
    @Resource
    private AESUtil aesUtil;

    @Test
    void encrypt() throws Exception {
        // 生成密钥
        String key = AESUtil.generateKey();
        System.out.println("生成的密钥: " + key);

        // 生成初始化向量
        byte[] iv = AESUtil.generateIV();
        System.out.println("生成的IV: " + Base64.getEncoder().encodeToString(iv));
        // 获取当前秒数 UTC
        // 获取当前时间的瞬时表示
        Instant now = Instant.now();

        // 将瞬时表示转换为指定时区的日期时间对象
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.of("UTC"));

        // 计算从1970年1月1日UTC时间到现在的秒数
        long secondsSinceEpoch = zonedDateTime.toEpochSecond();

        String encrypt = AESUtil.encrypt("online_1_"+secondsSinceEpoch, key, iv);
        System.out.println("加密后的数据: " + encrypt);
    }

    @Test
    void decrypt() throws Exception {
/*         // 生成密钥
        String key = AESUtil.generateKey();
        System.out.println("生成的密钥: " + key);

        // 生成初始化向量
        byte[] iv = AESUtil.generateIV();
        System.out.println("生成的IV: " + Base64.getEncoder().encodeToString(iv));
        AESUtil.decrypt("online_1_1699449600", key, iv);
        String decrypt = AESUtil.decrypt("H2SZuH7GUZpMdP8VeYyrvKaihhmyc9RUPU+wxTP4QrQ=", key, iv);
        System.out.println("解密后的数据: " + decrypt); */
    }
}
