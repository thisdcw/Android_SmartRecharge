package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class ModifySubRequestBody {
    @SerializedName("subUid")
    private String subUid;
    @SerializedName("subName")
    private String subName;

    @SerializedName("remark")
    private String remark;

    public ModifySubRequestBody(String subUid, String subName, String remark) {
        this.subUid = subUid;
        this.subName = subName;
        this.remark = remark;
    }
}
