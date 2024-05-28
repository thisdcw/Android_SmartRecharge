package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class SubNameRequestBody {

    @SerializedName("subName")
    private String subName;

    public SubNameRequestBody(String subName) {
        this.subName = subName;
    }
}
