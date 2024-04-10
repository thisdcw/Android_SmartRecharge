package com.mxsella.smartrecharge.utils;

import android.widget.Toast;

import com.mxsella.smartrecharge.MyApplication;

public class ToastUtils {
    public static void showToast(String info) {
        showToast(info, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String info) {
        showToast(info, Toast.LENGTH_LONG);
    }

    public static void showToast(String info, int duration) {
        Toast.makeText(MyApplication.getInstance(), info, duration).show();
    }

}
