package com.mxsella.smartrecharge.model.domain;

public class UserHistory {

    private String historyId;

    private String subName;

    private String deviceId;

    private String createTime;

    private int type;

    private int times;

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "UserHistory{" +
                "historyId='" + historyId + '\'' +
                ", subName='" + subName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", type=" + type +
                ", times=" + times +
                '}';
    }
}
