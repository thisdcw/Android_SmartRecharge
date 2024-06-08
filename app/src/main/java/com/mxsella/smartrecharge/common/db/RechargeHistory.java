package com.mxsella.smartrecharge.common.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class RechargeHistory extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = 3610860778729982946L;
    private int id;

    private String uid;

    private String historyId;

    private String productName;

    private String createTime;
    private int times;

    private boolean isPay;
    private boolean isCheck;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "RechargeHistory{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", historyId='" + historyId + '\'' +
                ", productName='" + productName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", times=" + times +
                ", isPay=" + isPay +
                ", isCheck=" + isCheck +
                '}';
    }
}
