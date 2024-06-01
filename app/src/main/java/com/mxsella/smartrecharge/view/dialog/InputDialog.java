package com.mxsella.smartrecharge.view.dialog;

import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogInputBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;

public class InputDialog extends BaseDialog<DialogInputBinding> {

    private String title;
    private String prompt;
    private String hint;
    private boolean showCancle = true;

    private DialogClickListener dialogListener;

    public InputDialog(String title) {
        this.title = title;
    }

    public InputDialog(String title, String prompt, String hint) {
        this.title = title;
        this.prompt = prompt;
        this.hint = hint;
    }

    public InputDialog(String title, String prompt, String hint, DialogClickListener dialogListener) {
        this.title = title;
        this.prompt = prompt;
        this.hint = hint;
        this.dialogListener = dialogListener;
    }

    @Override
    public void initView() {
        super.initView();
        if (getPrompt() != null) {
            binding.tvPrompt.setText(getPrompt());
        }
        if (getHint() != null) {
            binding.etInput.setHint(getHint());
        }
        if (!showCancle) {
            binding.tvCancel.setVisibility(View.GONE);
        }
        binding.tvTitle.setText(getTitle());
    }

    @Override
    public void initEventAndData() {
        binding.tvCancel.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onCancel();
            }
            dismiss();
        });
        binding.tvConfirm.setOnClickListener(v -> {
            if (dialogListener != null) {
                dialogListener.onConfirm();
            }
            dismiss();
        });
    }

    public void showHint(boolean show) {
        if (show) {
            binding.etInput.setHint(getHint());
        } else {
            binding.etInput.setHint("");
        }
    }


    public boolean isShowCancle() {
        return showCancle;
    }

    public void setShowCancle(boolean showCancle) {
        this.showCancle = showCancle;
    }

    public String getInput() {
        return binding.etInput.getText().toString().trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setDialogListener(DialogClickListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public DialogClickListener getDialogListener() {
        return dialogListener;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_input;
    }
}
