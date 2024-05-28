package com.mxsella.smartrecharge.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static final String SALT = "";

    public static String md5(String input) {
        try {
            // 创建MessageDigest对象
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // 将字符串转换为字节数组
            byte[] bytes = input.getBytes();

            // 使用MessageDigest更新摘要
            digest.update(bytes);

            // 获得摘要的字节数组
            byte[] messageDigest = digest.digest();

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
