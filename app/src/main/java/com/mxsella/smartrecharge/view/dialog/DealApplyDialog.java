package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogDealApplyBinding;

public class DealApplyDialog extends BaseDialog<DialogDealApplyBinding> {


    public static final String PASS = "pass";
    public static final String REFUSE = "refuse";
    public static final String DO_CANCLE = "do_cancle";

    private boolean isPass;

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    private DialogListener dialogListener;

    public interface DialogListener {
        void onConfirmClick();

        void onCancelClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Override
    public void initEventAndData() {

        setDefault(PASS);
        binding.cancel.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onCancelClick();
            }
            dismiss();
        });
        binding.confirm.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onConfirmClick();
            }
            dismiss();
        });
        binding.pass.setOnClickListener(v -> {
            setDefault(PASS);
        });
        binding.refuse.setOnClickListener(v -> {
            setDefault(REFUSE);
        });
        binding.doCancle.setOnClickListener(v -> {
            setDefault(DO_CANCLE);
        });

    }

    private void setDefault(String lan) {
        // 先重置所有选项的选择状态
        binding.pass.setSelected(false);
        binding.refuse.setSelected(false);
        binding.doCancle.setSelected(false);

        // 根据传入的参数设置是否通过
        switch (lan) {
            case PASS:
                binding.pass.setSelected(true);
                setPass(true);
                break;
            case REFUSE:
                binding.refuse.setSelected(true);
                setPass(false);
                break;
            case DO_CANCLE:
                binding.doCancle.setSelected(true);
                setPass(false);
                break;
        }
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_deal_apply;
    }

}
