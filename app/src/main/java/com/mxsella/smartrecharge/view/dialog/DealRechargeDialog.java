package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogDealApplyBinding;
import com.mxsella.smartrecharge.databinding.DialogDealRechargeBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;

public class DealRechargeDialog extends BaseDialog<DialogDealRechargeBinding> {

    private DialogClickListener dialogListener;

    private Integer productId;

    public DealRechargeDialog(Integer productId) {
        this.productId = productId;
    }

    @Override
    public void initView() {
        super.initView();
        binding.tvPrompt.setText(getString(R.string.device_info_product_id, String.valueOf(getProductId())));
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
        return R.layout.dialog_deal_recharge;
    }

}
