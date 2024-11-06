package com.sqx.modules.box.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * @author Zbc
 */
@Component
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 128; // 可以选择128, 192, 或 256

    /**
     * 生成密钥
     *
     * @return 密钥
     * @throws Exception
     */
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 生成初始化向量 (IV)
     *
     * @return IV
     */
    public static byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16]; // AES块大小为16字节
        random.nextBytes(iv);
        return iv;
    }

    /**
     * 加密数据
     *
     * @param data 要加密的数据
     * @param key  密钥
     * @param iv   初始化向量
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encrypt(String data, String key, byte[] iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 解密数据
     *
     * @param encryptedData 要解密的数据
     * @param key           密钥
     * @param iv            初始化向量
     * @return 解密后的数据
     * @throws Exception
     */
    public static String decrypt(String encryptedData, String key, byte[] iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        try {
            // 生成密钥
            String key = generateKey();
            System.out.println("生成的密钥: " + key);

            // 生成初始化向量
            byte[] iv = generateIV();
            System.out.println("生成的IV: " + Base64.getEncoder().encodeToString(iv));

            // 要加密的数据
            String data = "Hello, World!";
            System.out.println("原始数据: " + data);

            // 加密数据
            String encryptedData = encrypt(data, key, iv);
            System.out.println("加密后的数据: " + encryptedData);

            // 解密数据
            String decryptedData = decrypt(encryptedData, key, iv);
            System.out.println("解密后的数据: " + decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
