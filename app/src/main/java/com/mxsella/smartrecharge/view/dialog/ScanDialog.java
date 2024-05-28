package com.mxsella.smartrecharge.view.dialog;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogScanBinding;
import com.mxsella.smartrecharge.inter.ScanLifeListener;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.ClipView;

import java.io.IOException;

public class ScanDialog extends BaseDialog<DialogScanBinding> {
    private int scanSize = 300;
    private Context context;
    private RemoteView remoteView;
    private ScanResultCallBack scanResultCallBack;
    private int mScreenWidth;
    private int mScreenHeight;
    private int halfH;
    private int halfW;
    private int clipDp;

    private static final int REQUEST_CODE_PICK_IMAGE = 10002;

    public ScanDialog(@NonNull Context context) {
        this.context = context;
    }

    public ScanDialog setScanResultCallBack(ScanResultCallBack scanResultCallBack) {
        this.scanResultCallBack = scanResultCallBack;
        return this;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_scan;
    }

    @Override
    public void initEventAndData() {
        binding.picture.setOnClickListener(v -> {
            selectImageFromGallery();
        });
    }

    public interface ScanResultCallBack {
        void onResult(HmsScan[] result);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Override
    public void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        binding.viewGroup.setScanFrameSize(scanSize);
        initScanParameter();
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        binding.viewGroup.addView(remoteView, params);
        binding.cover.setOnClickListener(v -> {
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

    public void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                scanQRCodeFromGallery(imageUri);
            }
        }
    }

    public void scanQRCodeFromGallery(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            HmsScan[] result = ScanUtil.decodeWithBitmap(context, bitmap, new HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create());
            if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].originalValue)) {
                scanResultCallBack.onResult(result);
            } else {
                ToastUtils.showLongToast("No QR code found in the image.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dismiss();
    }

    @Override
    public void onStarted() {
        super.onStart();
        remoteView.onStart();
        remoteView.onResume();
    }

    @Override
    public void onStopped() {
        super.onStop();
        remoteView.onPause();
        remoteView.onStop();
        remoteView.onDestroy();
    }
}
