package com.mxsella.smartrecharge.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityScanRechargeBinding;
import com.mxsella.smartrecharge.utils.PermissionUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.ScanDialog;

public class ScanRechargeActivity extends BaseActivity<ActivityScanRechargeBinding> {

    public static final int REQUEST_CODE_SCAN_DEFAULT_MODE = 10008;
    ScanDialog scanDialog;

    @Override
    public void initView() {
        PermissionUtil.getInstance().requestCameraPermission(this);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });


    public void onButtonClick(View view) {
        barcodeLauncher.launch(new ScanOptions());
    }

    public void huawei(View view) {
        scanDialog = new ScanDialog();
        scanDialog.show(getSupportFragmentManager(),"scan_dialog");
//        HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create();
//        ScanUtil.startScan(this, REQUEST_CODE_SCAN_DEFAULT_MODE, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_DEFAULT_MODE && data != null) {

            HmsScan hmsScan = data.getParcelableExtra(ScanUtil.RESULT);
            if (hmsScan != null && !TextUtils.isEmpty(hmsScan.getOriginalValue())) {
                scanDialog.dismiss();
                ToastUtils.showToast(hmsScan.getOriginalValue());
            }
        }
    }

    @Override
    public int layoutId() {
        return R.layout.activity_scan_recharge;
    }

}