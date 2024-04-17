package com.mxsella.smartrecharge.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.view.ClipView;

public class ScanDialog extends Dialog {
    private int scanSize = 300;
    private Context context;
    private RemoteView remoteView;
    private ScanResultCallBack scanResultCallBack;
    int mScreenWidth;
    int mScreenHeight;
    int halfH;
    int halfW;
    int clipDp;
    ClipView clipView;
    public ScanDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    public ScanDialog setScanResultCallBack(ScanResultCallBack scanResultCallBack) {
        this.scanResultCallBack = scanResultCallBack;
        return this;
    }

    public interface ScanResultCallBack {
        void onResult(HmsScan[] result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_scan);
        // 设置扫码识别区域
        clipView = findViewById(R.id.viewGroup);
        clipView.setScanFrameSize(scanSize);
        initScanParameter();
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        clipView.addView(remoteView, params);

        findViewById(R.id.cover).setOnClickListener(v -> {
            dismiss();
        });
    }

    private void initScanParameter() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float density = dm.density;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;

        halfH = mScreenHeight / 2;
        halfW = mScreenWidth / 2;


        clipDp = (int) (scanSize * density) / 2;

        int left = halfW - clipDp;//左边x坐标
        int right = halfW + clipDp;//右边x坐标
        int top = halfH - clipDp;//上边y坐标
        int bottom = halfH + clipDp;//下边y坐标

        Rect rect = new Rect(left, top, right, bottom);
        remoteView = new RemoteView.Builder()
                .setContext((Activity) context)
                .setFormat(HmsScan.ALL_SCAN_TYPE)
                .setBoundingBox(rect)
                .build();

        remoteView.setOnResultCallback(result -> {
            if (scanResultCallBack != null) {
                scanResultCallBack.onResult(result);
            }
        });
    }

    public ScanDialog setScanSize(int scanSize) {
        this.scanSize = scanSize;
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
        remoteView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onPause();
        remoteView.onStop();
        remoteView.onDestroy();
    }
}
