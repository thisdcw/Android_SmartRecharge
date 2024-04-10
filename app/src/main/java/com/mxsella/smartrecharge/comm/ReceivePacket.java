package com.mxsella.smartrecharge.comm;

import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;

import java.util.Arrays;

public class ReceivePacket {

    private static final String TAG = "ReceivePacket";
    public static final String TYPE_MAC = "mac";
    public static final String TYPE_PAY = "pay";
    public static final String TYPE_INFO = "info";
    public static final String TYPE_LENGTH = "length";
    public static final String TYPE_UNKNOWN = "unknown";
    private byte[] bytes;
    private int workTime;
    private int remainTimes;
    private String type;
    private byte command;
    private byte tail;
    private double data = -1;
    private int len = -1;
    private String mac;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setData(double data) {
        this.data = data;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(int remainTimes) {
        this.remainTimes = remainTimes;
    }

    public byte getCommand() {
        return command;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public byte getTail() {
        return tail;
    }

    public void setTail(byte tail) {
        this.tail = tail;
    }

    public void setTypePay(int remainTimes, int workTime) {
        setRemainTimes(remainTimes);
        setWorkTime(workTime);
    }

    public ReceivePacket(byte[] bytes) {
        this.bytes = bytes;
        this.command = bytes[0];
        this.len = bytes[1];
        this.tail = bytes[bytes.length - 1];

        if (command == (byte) Protocol.ADDR_MAC) {
            this.type = TYPE_MAC;
            String s = Protocol.bytesToHexString(Arrays.copyOfRange(bytes, 2, 8));
            setMac(s);
            Config.saveDeviceMac(s);
        } else if (command == (byte) Protocol.ADDR_PAY) {
            this.type = TYPE_PAY;
            remainTimes = Protocol.bytesToInt(Arrays.copyOfRange(bytes, 2, 4));
            workTime = Protocol.bytesToInt(Arrays.copyOfRange(bytes, 4, 6));
            setTypePay(remainTimes, workTime);
        } else if (command == (byte) Protocol.ADDR_INFO) {
            this.type = TYPE_INFO;
            remainTimes = Protocol.bytesToInt(Arrays.copyOfRange(bytes, 2, 4));
            workTime = Protocol.bytesToInt(Arrays.copyOfRange(bytes, 4, 6));
            setTypePay(remainTimes, workTime);
        } else {
            this.type = TYPE_UNKNOWN;
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String getTypeUnknown() {
        return TYPE_UNKNOWN;
    }

    public byte getFrom() {
        return command;
    }

    public void setFrom(byte command) {
        this.command = command;
    }

    public double getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Override
    public String toString() {
        return "ReceivePacket{" +
                "bytes=" + Arrays.toString(bytes) +
                ", type='" + type + '\'' +
                ", command=" + command +
                ", data=" + data +
                ", len=" + len +
                '}';
    }
}
