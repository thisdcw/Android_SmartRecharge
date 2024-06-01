package com.mxsella.smartrecharge.model.enums;

public enum ResultCode {

    SUCCESS(200, "获取成功", ""),
    FAILED(300, "获取失败", ""),
    ERROR(500, "出错啦", "");

    private final int code;

    private final String message;

    private String description;

    ResultCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
