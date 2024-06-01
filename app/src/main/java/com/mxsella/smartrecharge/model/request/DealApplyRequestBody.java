package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class DealApplyRequestBody {
    @SerializedName("productName")
    private String productName;
    @SerializedName("applyId")
    private String applyId;
    @SerializedName("pass")
    private Boolean pass;

    public DealApplyRequestBody(String productName, String applyId, Boolean pass) {
        this.productName = productName;
        this.applyId = applyId;
        this.pass = pass;
    }
}
