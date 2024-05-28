package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.net.NetConstants;
import com.mxsella.smartrecharge.databinding.ActivityRegisterBinding;
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

        userViewModel.getVerifyState().observe(this, isGet -> {
            if (isGet) {
                ToastUtils.showToast("获取验证码成功");
            } else {
                ToastUtils.showToast("获取验证码失败");
            }
        });

        userViewModel.getRegisterState().observe(this, isRegister -> {
            if (isRegister) {
                ToastUtils.showToast("注册成功");
                navToWithFinish(LoginActivity.class);
            } else {
                ToastUtils.showToast("注册失败");
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
        userViewModel.getVerifyCode(phone, NetConstants.TYPE_REGISTER);
    }
}