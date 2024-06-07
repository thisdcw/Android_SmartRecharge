package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class DeviceStateRequestBody {
    @SerializedName("productName")
    private String productName;
    @SerializedName("deviceId")
    private String deviceId;

    public DeviceStateRequestBody(String productName, String deviceId) {
        this.productName = productName;
        this.deviceId = deviceId;
    }
}
