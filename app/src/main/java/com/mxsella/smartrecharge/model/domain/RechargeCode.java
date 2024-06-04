package com.mxsella.smartrecharge.model.domain;

public class RechargeCode {

    private String historyId;

    private String deviceId;

    private String rechargeCode;

    private int rechargeState;

    private String createTime;

    private int times;

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRechargeCode() {
        return rechargeCode;
    }

    public void setRechargeCode(String rechargeCode) {
        this.rechargeCode = rechargeCode;
    }

    public int getRechargeState() {
        return rechargeState;
    }

    public void setRechargeState(int rechargeState) {
        this.rechargeState = rechargeState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "RechargeCode{" +
                "historyId='" + historyId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", rechargeCode='" + rechargeCode + '\'' +
                ", rechargeState=" + rechargeState +
                ", createTime='" + createTime + '\'' +
                ", times=" + times +
                '}';
    }
}
