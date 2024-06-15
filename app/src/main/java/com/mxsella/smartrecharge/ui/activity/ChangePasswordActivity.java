package com.mxsella.smartrecharge.ui.activity;

import android.os.CountDownTimer;
import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityChangePasswordBinding;
import com.mxsella.smartrecharge.model.enums.CodeType;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.MD5Utils;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding> {

    private boolean usePwd = false;

    private boolean isGetCode = false;
    private boolean isGetting = false;

    private CountDownTimer countDownTimer;

    @Override
    public int layoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        initLayout();
    }

    @Override
    public void initObserve() {
        userViewModel.getVerifyResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                isGetCode = true;
                countDown();
            }
            ToastUtils.showToast(result.getMessage());
        });
        userViewModel.getVerifyChangePwd().observe(this, result -> {
            ToastUtils.showToast(result.getMessage());
            if (result.getResultCode() == ResultCode.SUCCESS) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                navTo(LoginActivity.class);
            }
        });
        userViewModel.getChangePwd().observe(this, result -> {
            ToastUtils.showToast(result.getMessage());
            if (result.getResultCode() == ResultCode.SUCCESS) {
                navTo(LoginActivity.class);
            }
        });
    }

    @Override
    public void initListener() {
        binding.changeMode.setOnClickListener(v -> {
            usePwd = !usePwd;
            initLayout();
        });
    }

    private void countDown() {
        countDownTimer = new CountDownTimer(60 * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) millisUntilFinished / 1000;
                binding.tvGetVerifyCode.setText(String.valueOf(time));
            }

            @Override
            public void onFinish() {
                isGetting = false;
                binding.tvGetVerifyCode.setText("再次获取");
            }
        };
        countDownTimer.start();
        isGetting = true;
    }

    public void getCode(View view) {
        if (isGetting) {
            ToastUtils.showToast("请勿重复点击!");
            return;
        }
        String telephone = binding.telephone.getText().toString().trim();
        userViewModel.getVerifyCode(telephone, CodeType.MODIFY.getType());

    }

    public void sure(View view) {
        if (!usePwd && !isGetCode) {
            ToastUtils.showToast("请先获取验证码");
            return;
        }
        if (usePwd) {
            String old_pwd = binding.oldPwd.getText().toString().trim();
            String pwd = binding.newPwd.getText().toString().trim();
            userViewModel.changePassword(old_pwd, pwd);
        } else {
            String telephone = binding.telephone.getText().toString().trim();
            String code = binding.edtVerifyCode.getText().toString().trim();
            String pwd = binding.newPwd.getText().toString().trim();
            userViewModel.changePasswordWithCode(telephone, code, pwd);
        }
    }

    private void initLayout() {
        if (usePwd) {
            binding.useVerifyCode.setVisibility(View.GONE);
            binding.usePwd.setVisibility(View.VISIBLE);
            binding.changeMode.setText("验证码修改");
        } else {
            binding.useVerifyCode.setVisibility(View.VISIBLE);
            binding.usePwd.setVisibility(View.GONE);
            binding.changeMode.setText("密码修改");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}