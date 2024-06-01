package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class DeviceRechargeRequestBody {
    @SerializedName("productName")
    private String productName;
    @SerializedName("targetDeviceId")
    private String targetDeviceId;
    @SerializedName("times")
    private Integer times;

    public DeviceRechargeRequestBody(String productName, String targetDeviceId, Integer times) {
        this.productName = productName;
        this.targetDeviceId = targetDeviceId;
        this.times = times;
    }
}
