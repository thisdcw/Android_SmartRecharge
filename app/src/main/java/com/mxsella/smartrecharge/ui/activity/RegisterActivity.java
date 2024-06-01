package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.databinding.ActivityRegisterBinding;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private UserViewModel userViewModel;

    @Override
    public int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        userViewModel = new UserViewModel();

        userViewModel.getVerifyResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });

        userViewModel.getRegisterResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
    }

    public void toLogin(View view) {
        navToWithFinish(LoginActivity.class);
    }

    public void doRegister(View view) {
        String phone = binding.edtTelephone.getText().toString().trim();
        String verifyCode = binding.edtVerifyCode.getText().toString().trim();
        String inviteCode = binding.edtInviteCode.getText().toString().trim();
        userViewModel.register(phone, verifyCode, inviteCode);
    }

    public void getVerifyCode(View view) {
        String phone = binding.edtTelephone.getText().toString().trim();
        userViewModel.getVerifyCode(phone, Constants.TYPE_REGISTER);
    }
}