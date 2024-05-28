package com.mxsella.smartrecharge.model;

import java.util.Date;

public class Brand {

    private int id;

    private String deviceId;

    private int productId;

    private String brand;

    private String agent;

    private String store;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", productId=" + productId +
                ", brand='" + brand + '\'' +
                ", agent='" + agent + '\'' +
                ", store='" + store + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
