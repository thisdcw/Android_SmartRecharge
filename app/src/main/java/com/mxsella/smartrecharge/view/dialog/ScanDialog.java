package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.databinding.DialogScanBinding;
import com.mxsella.smartrecharge.ui.activity.ScanRechargeActivity;

public class ScanDialog extends DialogFragment {
    DialogScanBinding binding;

    public ScanDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_scan, null, false);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_scan);
        RemoteView build = new RemoteView.Builder().setContext(getActivity()).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        build.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        binding.fl.addView(build, params);
        huawei();
        return dialog;
    }

    public void huawei() {
        HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create();
        ScanUtil.startScan((getActivity()), ScanRechargeActivity.REQUEST_CODE_SCAN_DEFAULT_MODE, options);
    }
}
