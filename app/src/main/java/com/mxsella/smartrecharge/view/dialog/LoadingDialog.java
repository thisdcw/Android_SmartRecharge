package com.mxsella.smartrecharge.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogLoadingBinding;

public class LoadingDialog extends Dialog {

    DialogLoadingBinding binding;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        binding = DataBindingUtil.setContentView((Activity) context, R.layout.dialog_loading);
    }

    public void showLoading() {
        if (!isShowing()) {
            show();
            binding.avi.smoothToShow();
        }
    }

    public void hideLoading() {
        if (isShowing()) {
            binding.avi.smoothToHide();
            dismiss();
        }
    }


}
