package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class UpdateDeviceBrandRequestBody {

    @SerializedName("productName")
    private String productName;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("targetUid")
    private String targetUid;

    public UpdateDeviceBrandRequestBody(String productName, String deviceId, String targetUid) {
        this.productName = productName;
        this.deviceId = deviceId;
        this.targetUid = targetUid;
    }
}
