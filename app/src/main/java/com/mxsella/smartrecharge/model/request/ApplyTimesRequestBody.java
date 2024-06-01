package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class ApplyTimesRequestBody {
    @SerializedName("productName")
    private String productName;
    @SerializedName("times")
    private Integer times;

    public ApplyTimesRequestBody(String productName, Integer times) {
        this.productName = productName;
        this.times = times;
    }
}
