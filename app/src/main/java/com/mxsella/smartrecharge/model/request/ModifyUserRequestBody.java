package com.mxsella.smartrecharge.model.request;

import com.google.gson.annotations.SerializedName;

public class ModifyUserRequestBody {
    @SerializedName("userName")
    private String userName;
    @SerializedName("avatar")
    private String avatar;

    public ModifyUserRequestBody(String userName, String avatar) {
        this.userName = userName;
        this.avatar = avatar;
    }
}
