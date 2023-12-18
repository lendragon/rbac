package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesDecryptor {
    public static String aesDecrypt(String encryptedData, String key, String iv) throws Exception {
        // 将密钥和向量转换为字节数组
        byte[] keyBytes = key.getBytes("UTF-8");
        byte[] ivBytes = iv.getBytes("UTF-8");

        // 解码Base64编码的密文
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // 创建AES解密器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 解密
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // 返回解密后的明文字符串
        return new String(decryptedBytes, "UTF-8");
    }

    // 示例用法
//    public static void main(String[] args) throws Exception {
//        String encryptedData = "9QdHhEEFyylFF7AtjlcZMw==";
//        String key = "0123456789abcdef";
//        String iv = "fedcba9876543210";
//        String decryptedData = aesDecrypt(encryptedData, key, iv);
//        System.out.println("解密后的数据: " + decryptedData);
//    }
}
