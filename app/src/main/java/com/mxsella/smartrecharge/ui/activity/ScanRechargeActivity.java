package com.mxsella.smartrecharge.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.huawei.hms.ml.scan.HmsScan;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityScanRechargeBinding;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PermissionUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.ScanDialog;

public class ScanRechargeActivity extends BaseActivity<ActivityScanRechargeBinding> {

    private ScanDialog scanDialog;

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void huawei(View view) {
        scanDialog = new ScanDialog(mContext)
                .setScanSize(300)
                .setScanResultCallBack(result -> {
                    HmsScan hmsScan = result[0];
                    handleScanData(hmsScan);
                    scanDialog.dismiss();
                });
        scanDialog.show(getSupportFragmentManager(), "customer_scan");
    }

    private void handleScanData(HmsScan data) {
        String value = data.getOriginalValue();
        LogUtil.d("扫描到的内容是: " + value);
        ToastUtils.showToast(value);
    }


    @Override
    public int layoutId() {
        return R.layout.activity_scan_recharge;
    }

}