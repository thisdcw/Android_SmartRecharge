package com.mxsella.smartrecharge.ui.activity;

import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityLoginBinding;
import com.mxsella.smartrecharge.model.enums.CodeType;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private UserViewModel userViewModel;

    private boolean usePwd = false;
    private boolean isGetting = false;
    private CountDownTimer countDownTimer;

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        }
        userViewModel = new UserViewModel();

        userViewModel.getLoginResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                Config.setLogin(true);
                Config.saveUser(result.getData());
                navToWithFinish(MainActivity.class);
            }
        });

        userViewModel.getVerifyResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                countDown();
            }
            ToastUtils.showToast(result.getMessage());
        });
    }

    public void doLogin(View view) {
        String phone = binding.edtTelephone.getText().toString().trim();
        if (usePwd) {
            String pwd = binding.edtPassword.getText().toString().trim();
            userViewModel.loginWithPwd(phone, pwd);
        } else {
            String code = binding.edtVerifyCode.getText().toString().trim();
            userViewModel.loginWhitCode(phone, code);
        }
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

    public void usePassword(View view) {
        if (usePwd) {
            binding.lytPassword.setVisibility(View.GONE);
            binding.fltVerifyCode.setVisibility(View.VISIBLE);
            binding.tvLoginWay.setText(R.string.login_way_password);
            usePwd = !usePwd;
        } else {
            binding.lytPassword.setVisibility(View.VISIBLE);
            binding.fltVerifyCode.setVisibility(View.GONE);
            binding.tvLoginWay.setText(R.string.login_way_verifyCode);
            usePwd = !usePwd;
        }
    }

    public void toRegister(View view) {
        navToWithFinish(RegisterActivity.class);
    }

    public void getVerifyCode(View view) {
        if (isGetting) {
            ToastUtils.showToast("请勿重复点击!");
            return;
        }
        String phone = binding.edtTelephone.getText().toString().trim();
        userViewModel.getVerifyCode(phone, CodeType.LOGIN.getType());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}