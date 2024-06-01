package com.mxsella.smartrecharge.utils;

import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.common.Config;

import java.util.Locale;

public class PayUtils {

    /**
     * 生成秘钥
     */
    public static int secretKey() {

        int mac_num = Protocol.calculateXOR(Config.getDeviceMac());
        String password = Config.getPassword();
        int pwd_num = Integer.parseInt(String.valueOf(password.charAt(0)));

        for (int i = 1; i < password.length(); i++) {
            pwd_num ^= Integer.parseInt(String.valueOf(password.charAt(i)));
        }
        int key = mac_num + pwd_num;
        Config.saveSecretKey(key);
        return key;
    }

    /**
     * 加密
     */
    public static String encode(int times) {
        int key = secretKey();
        int i1 = Protocol.generateRandomInt(65536);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02X", Protocol.ADDR_PAY));
        sb.append(String.format("%02X", Protocol.DATA_LENGTH));
        sb.append(String.format("%04x", times));
        sb.append(String.format("%04x", 0));
        sb.append(String.format("%04x", i1));
        return en(sb, key);
    }

    public static String encode() {
        int key = secretKey();
        int i1 = Protocol.generateRandomInt(65536);
        int remainTimes = Protocol.generateRandomInt(255);
        int workTime = Protocol.generateRandomInt(255);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02X", Protocol.ADDR_INFO));
        sb.append(String.format("%02X", Protocol.DATA_LENGTH));
        sb.append(String.format("%04x", remainTimes));
        sb.append(String.format("%04x", workTime));
        sb.append(String.format("%04x", i1));
        return en(sb, key);
    }

    /**
     * 加密:
     * 1. 计算校验码
     * 2. 对每个字节进行加密并拼接
     *
     * @param sb
     * @param key
     * @return
     */
    public static String en(StringBuilder sb, int key) {
        StringBuilder res = new StringBuilder();
        int num = 0;
        //计算校验码
        for (int i = 0; i < sb.length(); i += 2) {
            String charts = sb.substring(i, Math.min(i + 2, sb.length()));
            int number = Integer.parseInt(charts, 16);
            num += number;
        }
        String s = String.format("%x", num);
        if (s.length() > 2) {
            s = s.substring(s.length() - 2);
        }
        // 将十进制数转换为两个字节的十六进制字符串
        for (int i = 0; i < 4; i += 2) {
            String charts = sb.substring(i, Math.min(i + 2, sb.length()));
            res.append(charts);
        }
        for (int i = 4; i < sb.length(); i += 2) {
            String charts = sb.substring(i, Math.min(i + 2, sb.length()));
            // 处理每两个字符的操作
            int number = Integer.parseInt(charts, 16) - key;
            String str = String.format("%02x", number);
            if (str.length() > 2) {
                res.append(str.substring(str.length() - 2));
            } else {
                res.append(str);
            }
        }
        res.append(s);
        return res.toString().toUpperCase(Locale.ROOT);
    }

    /**
     * 解密
     */
    public static int decode(byte[] data) {
        int key = secretKey();
        int sum = -1;
        for (byte datum : data) {
            int i = datum + key;
            sum += i;
        }
        return sum;
    }

    /**
     * 判断是否有效
     */
    public static boolean isValid(byte[] bytes) {
        String str = StringUtils.bytesToHex(bytes);
        int sum = calculateSUM(str);
        int check = Integer.parseInt(String.valueOf(bytes[bytes.length - 2] + bytes[bytes.length - 1]), 16);
        return false;
    }

    public static int calculateSUM(String hexString) {
        int result = 0;
        for (int i = 0; i < hexString.length() - 2; i += 2) {
            // 提取两个字符作为一个十六进制数
            String hexByte = hexString.substring(i, i + 2);
            // 将十六进制数转换为十进制数
            int decimalValue = Integer.parseInt(hexByte, 16);
            // 计算和
            result += decimalValue;
        }
        return result;
    }
}
