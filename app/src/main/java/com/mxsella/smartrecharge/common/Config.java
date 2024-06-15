package com.mxsella.smartrecharge.common;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.entity.BleDeviceInfo;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.utils.LogUtil;

public class Config {
    public static final boolean isDebug = true;
    public static final boolean checkLogin = true;
    public static SharedPreferences mainSP;
    // Ble名称
    private static final String BLE_NAME = "ble_name";
    // Ble地址
    private static final String BLE_ADDRESS = "ble_address";
    private static final String BLE = "ble";
    private static final String SECRET_KEY = "secret_key";
    private static final String DEVICE_MAC = "device_mac";
    private static final String DEVICE_ID = "device_id";
    private static final String PASSWORD = "password";
    private static final String CURRENT_USER = "current_user";
    private static final String IS_LOGIN = "is_login";
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String REMAIN_TIMES = "remainTimes";


    public static int getRemainTimes() {
        return mainSP.getInt(REMAIN_TIMES, 0);
    }

    public static void saveRemainTimes(int times) {
        mainSP.edit().putInt(REMAIN_TIMES, times).apply();
    }

    public static boolean isLogin() {
        return mainSP.getBoolean(IS_LOGIN, false);
    }

    public static void setLogin(boolean isLogin) {
        mainSP.edit().putBoolean(IS_LOGIN, isLogin).apply();
    }

    public static String getProductName() {
        return mainSP.getString(CURRENT_PRODUCT_NAME, "");
    }

    public static void saveProductName(String productName) {
        save(CURRENT_PRODUCT_NAME, productName);
    }

    public static void saveUser(User currentUser) {
        LogUtil.d("保存用户: " + currentUser);
        String userInfo = new Gson().toJson(currentUser);
        if (userInfo == null) {
            save(CURRENT_USER, "");
            return;
        }
        save(CURRENT_USER, userInfo);
    }

    public static String getCurrentUser() {
        return mainSP.getString(CURRENT_USER, "");
    }

    public static void saveBle(BleDeviceInfo deviceInfo) {
        save(BLE, deviceInfo.toString());
    }

    public static BleDeviceInfo getBle() {
        String result = mainSP.getString(BLE, new BleDeviceInfo().toString());
        return BleDeviceInfo.parse(result);
    }

    public static String getPassword() {
        return mainSP.getString(PASSWORD, "");
    }

    public static void savePassword(String password) {
        save(PASSWORD, password);
    }

    public static String getDeviceMac() {
        return mainSP.getString(DEVICE_MAC, "F724C9D500AD");
    }

    public static void saveDeviceMac(String deviceMac) {
        save(DEVICE_MAC, deviceMac);
    }

    public static int getSecretKey() {
        int key = Integer.parseInt(mainSP.getString(SECRET_KEY, "-1"));
        return key;
    }

    public static void saveSecretKey(int key) {
        save(SECRET_KEY, key);
    }



    public static void save(String key, Object value) {
        mainSP.edit().putString(key, String.valueOf(value)).apply();
    }
}
