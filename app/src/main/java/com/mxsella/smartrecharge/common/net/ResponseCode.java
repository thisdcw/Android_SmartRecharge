package com.mxsella.smartrecharge.common.net;

public enum ResponseCode {
    SUCCESS(200, "成功", "");

    private final int code;
    /**
     * 状态码信息
     */
    private final String msg;
    /**
     * 状态码描述(详情)
     */
    private final String description;

    ResponseCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }
}
