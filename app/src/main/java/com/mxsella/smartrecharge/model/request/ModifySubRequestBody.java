package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class ModifySubRequestBody {
    @SerializedName("subUid")
    private String subUid;
    @SerializedName("subName")
    private String subName;

    public ModifySubRequestBody(String subUid, String subName) {
        this.subUid = subUid;
        this.subName = subName;
    }
}
