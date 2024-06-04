package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class UseRechargeCodeRequest {
    @SerializedName("productName")
    private String productName;
    @SerializedName("historyId")
    private String historyId;

    public UseRechargeCodeRequest(String productName, String historyId) {
        this.productName = productName;
        this.historyId = historyId;
    }
}
