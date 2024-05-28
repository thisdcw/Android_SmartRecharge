package com.mxsella.smartrecharge.common;

import java.io.Serial;
import java.util.HashMap;

public class Constants {
    public static final String BLE_CONNECT = "com.mxsella.smartrecharge.BLE_CONNECT";
    public static final String BLE_DISCONNECT = "com.mxsella.smartrecharge.BLE_DISCONNECT";
    public static final String ROLE_ADMIN = "admin";//管理员
    public static final String ROLE_BRAND = "brand";//品牌商
    public static final String ROLE_AGENT = "agent";//代理商
    public static final String ROLE_STORE = "store";//店铺

    public static final HashMap<String, String> roleMap = new HashMap<String, String>() {

        {
            put(ROLE_ADMIN, "我的品牌商");
            put(ROLE_BRAND, "我的代理商");
            put(ROLE_AGENT, "我的店铺");
            put(ROLE_STORE, "我的设备");
        }
    };
}
