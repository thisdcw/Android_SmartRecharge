package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogProductInputBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;

public class ProductInputDialog extends BaseDialog<DialogProductInputBinding> {


    private String productId;

    private DialogClickListener dialogListener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Override
    public void initEventAndData() {
        binding.productId.setText(getString(R.string.product_id, getProductId()));
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setDialogListener(DialogClickListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_product_input;
    }
}
