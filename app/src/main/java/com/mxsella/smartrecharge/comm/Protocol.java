package com.mxsella.smartrecharge.comm;

import com.mxsella.smartrecharge.utils.PayUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class Protocol {

    public static final int HEAD = 0xBFFB; // 帧头
    public static final short ADDR_MAC = 0xF0;
    public static final short ADDR_PAY = 0xF1;
    public static final short ADDR_INFO = 0xF2;
    public static final short DATA_LENGTH = 0x06;
    public static final byte[] INFO = Protocol.hexToByteArray("BFFBF2069E9E9E9E9E4BA5");
    public static final byte[] MAC = Protocol.hexToByteArray("BFFBF006010101010000FA");

    public static byte[] encode(short address, short command) {
        StringBuilder sb = new StringBuilder();
        short len = (short) ((command == -1) ? 0 : 1);
        ByteBuffer buffer = ByteBuffer.allocate(5 + len);
        buffer.put(int2Bytes(HEAD));
        buffer.put((byte) address);
        buffer.put((byte) len);
        if (len != 0)
            buffer.put((byte) command);
        buffer.put((byte) (len == 0 ? address : address + len + command));
        sb.append(Integer.toHexString(HEAD))
                .append(String.format("%02x", address))
                .append(String.format("%02x", len))
                .append(len == 0 ? "" : String.format("%02x", command))
                .append(String.format("%02x", address + len + command));
        return buffer.array();
    }

    public static byte[] command(String data) {
        return Protocol.hexToByteArray(Protocol.intToHexString(Protocol.HEAD) + data);
    }

    public static String intToHexString(int value) {
        return Integer.toHexString(value).toUpperCase(); // 转换为大写形式的十六进制字符串
    }

    public static String decimalToTwoByteHex(StringBuilder sb, int key) {
        StringBuilder res = new StringBuilder();
        int num = 0;
        // 将十进制数转换为两个字节的十六进制字符串
        for (int i = 0; i < sb.length(); i += 2) {
            String charts = sb.substring(i, Math.min(i + 2, sb.length()));
            // 处理每两个字符的操作
            int number = Integer.parseInt(charts, 16) - key;
            num += number;
            res.append(String.format("%04x", number));
        }
        num = num + 248;
        res.append(String.format("%04x", num));
        return res.toString();
    }

    public static int calculateXOR(String hexString) {
        int result = 0;
        for (int i = 0; i < hexString.length(); i += 2) {
            // 提取两个字符作为一个十六进制数
            String hexByte = hexString.substring(i, i + 2);
            // 将十六进制数转换为十进制数
            int decimalValue = Integer.parseInt(hexByte, 16);
            // 计算异或值
            result ^= decimalValue;
        }
        return result;
    }

    public static int generateRandomInt(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }

    public static String intToTwoByteHex(int decimal) {
        // 将整数限制在0到65535范围内
        String str = Integer.toHexString(decimal);
        // 将限制后的整数转换为两位的十六进制字符串
        return str.substring(str.length() - 2);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            // 将每个字节转换为十六进制，并追加到字符串后面
            hexString.append(String.format("%02X", b & 0xFF));
        }
        return hexString.toString();
    }

    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static byte[] encode(short address, byte[] data) {
        short len = (short) data.length;
        ByteBuffer buffer = ByteBuffer.allocate(5 + len);
        buffer.put(int2Bytes(HEAD));
        buffer.put((byte) address);
        buffer.put((byte) len);
        if (len != 0)
            buffer.put(data);
        int sum = 0;
        for (byte b : data) {
            sum += b;
        }
        buffer.put((byte) (address + len + sum));
        return buffer.array();
    }

    public static byte[] encodeElectric(short address, int hz, int us, int up, int sp, int down, int rest, int times) {
        ByteBuffer buffer = ByteBuffer.allocate(5 + 9);
        buffer.put(int2Bytes(HEAD));
        buffer.put((byte) address);
        buffer.put((byte) 9);
        buffer.put((byte) 1);
        buffer.put((byte) hz);
        buffer.put((byte) ((us >> 8) & 0xFF));
        buffer.put((byte) us);
        buffer.put((byte) up);
        buffer.put((byte) sp);
        buffer.put((byte) down);
        buffer.put((byte) rest);
        buffer.put((byte) times);
        int sum = address + 9 + 1 + hz + ((us >> 8) & 0xFF) + us + up + sp + down + rest + times;
        buffer.put((byte) sum);
        return buffer.array();
    }

    /**
     * short转字节数组
     */
    public static byte[] short2Bytes(short num) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((num >> 8) & 0xFF);
        bytes[1] = (byte) num;
        return bytes;
    }

    /**
     * int转字节数组
     */
    public static byte[] int2Bytes(int num) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((num >> 8) & 0xFF);
        bytes[1] = (byte) num;
        return bytes;
    }

    /**
     * short数组转字节数组
     */
    public static byte[] shorts2Bytes(short[] shorts) {
        byte[] bytes = new byte[shorts.length * 2];
        byte[] temp = null;
        int i = 0;
        for (short s : shorts) {
            temp = short2Bytes(s);
            bytes[i++] = temp[0];
            bytes[i++] = temp[1];
        }
        return bytes;
    }

    /**
     * 是否相等
     */
    public static boolean endWith(byte[] src, byte[] target) {
        if (target.length == 0) return false;
        if (src.length < target.length) return false;
        for (int i = 0; i < target.length; i++) {
            if (target[target.length - i - 1] != src[src.length - i - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字节数组转int
     */
    public static int bytesToInt(byte[] bytes) {
        int res = 0;
        for (int i = 0; i < bytes.length; i++) {
            res = (res << 8) | (bytes[i] & 0xff);
        }
        return res;
    }
}
