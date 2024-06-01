package com.mxsella.smartrecharge.common;

import com.mxsella.smartrecharge.model.enums.UserEnum;

import java.io.Serial;
import java.util.HashMap;

public class Constants {
    public static final String BLE_CONNECT = "com.mxsella.smartrecharge.BLE_CONNECT";
    public static final String BLE_DISCONNECT = "com.mxsella.smartrecharge.BLE_DISCONNECT";

    public static final String TYPE_REGISTER = "0";
    public static final String TYPE_LOGIN = "1";
    public static final String TYPE_LOGOUT = "2";
    public static final String ROLE_ADMIN = "admin";//管理员
    public static final String ROLE_BRAND = "brand";//品牌商
    public static final String ROLE_AGENT = "agent";//代理商
    public static final String ROLE_STORE = "store";//店铺

    public static final HashMap<String, UserEnum> roleMap = new HashMap<String, UserEnum>() {
        private static final long serialVersionUID = 594984571348454340L;

        {
            put(ROLE_ADMIN, UserEnum.ADMIN);
            put(ROLE_BRAND, UserEnum.BRAND);
            put(ROLE_AGENT, UserEnum.AGENT);
            put(ROLE_STORE, UserEnum.STORE);
        }
    };
}
