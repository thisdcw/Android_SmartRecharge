package com.mxsella.smartrecharge.model.domain;

public class Device {
    private Integer id;
    private String deviceId;

    private Integer productId;

    private String brand;
    private String agent;

    private String store;

    private String createTime;

    private String brandName;

    private String agentName;

    private String storeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", productId=" + productId +
                ", brand='" + brand + '\'' +
                ", agent='" + agent + '\'' +
                ", store='" + store + '\'' +
                ", createTime='" + createTime + '\'' +
                ", brandName='" + brandName + '\'' +
                ", agentName='" + agentName + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
