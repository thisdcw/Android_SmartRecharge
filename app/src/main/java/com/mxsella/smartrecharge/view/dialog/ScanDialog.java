package com.mxsella.smartrecharge.view.dialog;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.mxsella.smartrecharge.MyApplication;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.databinding.DialogScanBinding;
import com.mxsella.smartrecharge.ui.activity.ScanRechargeActivity;

public class ScanDialog extends Dialog {
    private Context context;
    private RemoteView remoteView;
    public static final String SCAN_RESULT = "scanResult";

    private ScanResultCallBack scanResultCallBack;

    public void setScanResultCallBack(ScanResultCallBack scanResultCallBack) {
        this.scanResultCallBack = scanResultCallBack;
    }

    public ScanDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public interface ScanResultCallBack {
        void onResult(HmsScan[] result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_scan);
        // 设置扫码识别区域
        setupScanView(savedInstanceState);
    }

    private void setupScanView(Bundle savedInstanceState) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        int mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        final int SCAN_FRAME_SIZE = 300;
        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);
        Rect rect = new Rect();
        rect.left = mScreenWidth / 2 - scanFrameSize / 2;
        rect.right = mScreenWidth / 2 + scanFrameSize / 2;
        rect.top = mScreenHeight / 2 - scanFrameSize / 2;
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2;

        remoteView = new RemoteView.Builder()
                .setContext((Activity) context)
                .setFormat(HmsScan.ALL_SCAN_TYPE)
                .build();

        FrameLayout frameLayout = findViewById(R.id.rv);
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                if (scanResultCallBack!=null){
                    scanResultCallBack.onResult(result);
                }
            }
        });
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        frameLayout.addView(remoteView, params);
    }

    @Override
    public void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        remoteView.onStop();
    }
}
