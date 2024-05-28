package com.mxsella.smartrecharge.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class PermissionUtil {
    private Activity activity;
    private static volatile PermissionUtil instance;
    public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;
    public static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;
    public static final String BLUETOOTH_CONNECT = Manifest.permission.BLUETOOTH_CONNECT;
    public static final String BLUETOOTH_ADVERTISE = Manifest.permission.BLUETOOTH_ADVERTISE;
    public static final String BLUETOOTH_SCAN = Manifest.permission.BLUETOOTH_SCAN;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    public static final String INTERNET = Manifest.permission.INTERNET;
    public static final String VIBRATE = Manifest.permission.VIBRATE;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String STORAGE = Manifest.permission.MANAGE_EXTERNAL_STORAGE;
    public static final int BLUETOOTH_CODE = 10001;
    public static final int ACCESS_COARSE_LOCATION_CODE = 10006;
    public static final int ACCESS_FINE_LOCATION_CODE = 10007;
    public static final int ACCESS_NETWORK_STATE_CODE = 10008;
    public static final int INTERNET_CODE = 10009;
    public static final int VIBRATE_CODE = 10010;
    public static final int CAMERA_CODE = 10011;
    public static final int STORAGE_CODE = 10011;


    public static PermissionUtil getInstance(Activity activity) {
        if (instance == null) {
            synchronized (PermissionUtil.class) {
                if (instance == null) {
                    instance = new PermissionUtil(activity);
                }
            }
        }
        return instance;
    }

    public PermissionUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * 通过权限名称获取请求码
     *
     * @param permissionName 权限名称
     * @return requestCode 权限请求码
     */
    private int getPermissionRequestCode(String permissionName) {
        switch (permissionName) {
            case BLUETOOTH:
            case BLUETOOTH_ADMIN:
            case BLUETOOTH_CONNECT:
            case BLUETOOTH_ADVERTISE:
            case BLUETOOTH_SCAN:
                return BLUETOOTH_CODE;
            case ACCESS_COARSE_LOCATION:
                return ACCESS_COARSE_LOCATION_CODE;
            case ACCESS_FINE_LOCATION:
                return ACCESS_FINE_LOCATION_CODE;
            case ACCESS_NETWORK_STATE:
                return ACCESS_NETWORK_STATE_CODE;
            case INTERNET:
                return INTERNET_CODE;
            case VIBRATE:
                return VIBRATE_CODE;
            case CAMERA:
                return CAMERA_CODE;
            case STORAGE:
            case READ_EXTERNAL:
            case WRITE_EXTERNAL:
                return STORAGE_CODE;
            default:
                return 1000;
        }
    }

    /**
     * 请求文件管理权限
     *
     * @return
     */
    public PermissionUtil requestExternalPermission() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissions = new String[]{STORAGE};
        } else {
            permissions = new String[]{READ_EXTERNAL, WRITE_EXTERNAL};
        }
        for (String permission : permissions) {
            if (!hasPermission(activity, permission)) {
                requestPermission(activity, permission);
            }
        }
        return this;
    }

    /**
     * 获取蓝牙权限
     *
     * @return
     */
    public PermissionUtil requestBlePermission() {
        String[] ble = {BLUETOOTH, BLUETOOTH_ADMIN, BLUETOOTH_CONNECT, BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
        for (String s : ble) {
            if (!hasPermission(activity, s)) {
                requestPermission(activity, s);
            }
        }
        return this;
    }

    /**
     * 获取相机权限
     */
    public PermissionUtil requestCameraPermission() {
        if (!hasPermission(activity, CAMERA)) {
            requestPermission(activity, CAMERA);
        }
        return this;
    }

    /**
     * 请求权限
     *
     * @param activity   Activity实例
     * @param permission 权限名称
     */
    public void requestPermission(Activity activity, String permission) {
        int requestCode = getPermissionRequestCode(permission);
        // 请求此权限
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /**
     * 检查是否拥有某权限
     *
     * @param activity   Activity实例
     * @param permission 权限名称
     * @return true 有，false 没有
     */
    public boolean hasPermission(Activity activity, String permission) {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
