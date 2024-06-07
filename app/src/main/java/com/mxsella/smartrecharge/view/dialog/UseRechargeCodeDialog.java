package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogUseCodeBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;

public class UseRechargeCodeDialog extends BaseDialog<DialogUseCodeBinding> {

    private DialogClickListener dialogListener;

    private String productName;

    public UseRechargeCodeDialog(String productName) {
        this.productName = productName;
    }

    @Override
    public void initView() {
        super.initView();
        binding.tvPrompt.setText(getString(R.string.device_info_device_id, String.valueOf(getProductName())));
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Override
    public void initEventAndData() {
        binding.cancel.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onCancel();
            }
            dismiss();
        });
        binding.confirm.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onConfirm();
            }
            dismiss();
        });

    }

    public void setDialogListener(DialogClickListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public DialogClickListener getDialogListener() {
        return dialogListener;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_use_code;
    }

}
