package com.mxsella.smartrecharge.ui.activity;

import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.databinding.ActivityRegisterBinding;
import com.mxsella.smartrecharge.model.enums.CodeType;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private UserViewModel userViewModel;
    private CountDownTimer countDownTimer;
    private boolean isGetting = false;
    @Override
    public int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        }
        userViewModel = new UserViewModel();

        userViewModel.getVerifyResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                countDown();
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });

        userViewModel.getRegisterResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                ToastUtils.showToast(result.getMessage());
                navToWithFinish(LoginActivity.class);
            } else {
                ToastUtils.showToast(result.getMessage());
            }
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
                isGetting  =false;
                binding.tvGetVerifyCode.setText("再次获取");
            }
        };
        countDownTimer.start();
        isGetting = true;
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
        if (isGetting){
            ToastUtils.showToast("请勿重复点击!");
            return;
        }
        String phone = binding.edtTelephone.getText().toString().trim();
        userViewModel.getVerifyCode(phone, CodeType.REGISTER.getType());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}