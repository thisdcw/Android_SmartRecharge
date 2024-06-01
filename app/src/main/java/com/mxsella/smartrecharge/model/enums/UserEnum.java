package com.mxsella.smartrecharge.model.enums;

public enum UserEnum {
    ADMIN("admin", "品牌商", "管理员"),
    BRAND("brand", "代理商", "品牌商"),
    AGENT("agent", "店铺", "代理商"),
    STORE("store", null, "店铺");
    private String role;

    private String childRole;

    private String cn;

    UserEnum(String role, String childRole, String cn) {
        this.role = role;
        this.childRole = childRole;
        this.cn = cn;
    }

    public String getRole() {
        return role;
    }

    public String getChildRole() {
        return childRole;
    }

    public String getCn() {
        return cn;
    }
}
