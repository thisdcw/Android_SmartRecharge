package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class UserRechargeRequestBody {
    @SerializedName("productName")
    private String productName;
    @SerializedName("targetUid")
    private String targetUid;
    @SerializedName("times")
    private Integer times;

    public UserRechargeRequestBody(String productName, String targetUid, Integer times) {
        this.productName = productName;
        this.targetUid = targetUid;
        this.times = times;
    }
}
