package com.mxsella.smartrecharge.common.manager;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

    public interface PermissionResultCallback {
        void onPermissionGranted();
        void onPermissionDenied(List<String> deniedPermissions);
    }

    // 请求权限
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode, PermissionResultCallback callback) {
        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsNeeded.toArray(new String[0]), requestCode);
        } else {
            callback.onPermissionGranted();
        }
    }

    // 处理权限请求结果
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, PermissionResultCallback callback) {
        List<String> deniedPermissions = new ArrayList<>();
        boolean allPermissionsGranted = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                deniedPermissions.add(permissions[i]);
            }
        }

        if (allPermissionsGranted) {
            callback.onPermissionGranted();
        } else {
            callback.onPermissionDenied(deniedPermissions);
        }
    }
}
