package com.mxsella.smartrecharge.common;

import android.content.SharedPreferences;
import android.util.Log;

import com.mxsella.smartrecharge.entity.BleDeviceInfo;
import com.mxsella.smartrecharge.utils.LogUtil;

public class Config {
    public static final boolean isDebug = true;
    public static SharedPreferences mainSP;
    // Ble名称
    private static final String BLE_NAME = "ble_name";
    // Ble地址
    private static final String BLE_ADDRESS = "ble_address";
    private static final String BLE = "ble";
    private static final String SECRET_KEY = "secret_key";
    private static final String DEVICE_MAC = "device_mac";
    private static final String PASSWORD = "password";

    public static void saveBle(BleDeviceInfo deviceInfo) {
        save(BLE, deviceInfo.toString());
    }

    public static BleDeviceInfo getBle() {
        String result = mainSP.getString(BLE, new BleDeviceInfo().toString());
        return BleDeviceInfo.parse(result);
    }

    public static String getPassword() {
        return mainSP.getString(PASSWORD, "111100");
    }

    public static void savePassword(String password) {
        save(PASSWORD, password);
    }

    public static String getDeviceMac() {
        return "F724C9D500AD";
//        return mainSP.getString(DEVICE_MAC, "F724C9D500AD");
    }

    public static void saveDeviceMac(String deviceMac) {
        save(DEVICE_MAC, deviceMac);
    }

    public static int getSecretKey() {
        int key = Integer.parseInt(mainSP.getString(SECRET_KEY, "-1"));
        LogUtil.d("key => " + key);
        return key;
    }

    public static void saveSecretKey(int key) {
        save(SECRET_KEY, key);
    }

    public static String getBleName() {
        return mainSP.getString(BLE_NAME, "");
    }

    public static void saveBleName(String name) {
        save(BLE_NAME, name);
    }

    public static String getBleAddress() {
        return mainSP.getString(BLE_ADDRESS, "");
    }

    public static void saveBleAddress(String address) {
        save(BLE_ADDRESS, address);
    }

    public static void save(String key, Object value) {
        mainSP.edit().putString(key, String.valueOf(value)).apply();
    }
}