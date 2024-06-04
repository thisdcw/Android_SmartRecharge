package com.mxsella.smartrecharge.model.enums;

public enum CodeType {
    LOGIN("1"),
    REGISTER("0"),
    LOGOUT("2"),
    MODIFY("3");

    private final String type;

    CodeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
