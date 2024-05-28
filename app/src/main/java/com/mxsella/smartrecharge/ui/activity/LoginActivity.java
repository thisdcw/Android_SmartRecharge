package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.net.NetConstants;
import com.mxsella.smartrecharge.databinding.ActivityLoginBinding;
import com.mxsella.smartrecharge.utils.MD5Utils;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private UserViewModel userViewModel;

    private boolean usePwd = false;

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        userViewModel = new UserViewModel();

        userViewModel.getLoginState().observe(this, isLogin -> {
            if (isLogin) {
                navToWithFinish(MainActivity.class);
            }
        });

        userViewModel.getVerifyState().observe(this, isGet -> {
            if (isGet) {
                ToastUtils.showToast("获取验证码成功");
            } else {
                ToastUtils.showToast("获取验证码失败");
            }
        });
    }

    public void doLogin(View view) {
        String phone = binding.edtTelephone.getText().toString().trim();
        if (usePwd) {
            String pwd = binding.edtPassword.getText().toString().trim();
            userViewModel.loginWithPwd(phone, MD5Utils.md5(pwd));
        } else {
            String code = binding.edtVerifyCode.getText().toString().trim();
            userViewModel.loginWhitCode(phone, code);
        }
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
        String phone = binding.edtTelephone.getText().toString().trim();
        userViewModel.getVerifyCode(phone, NetConstants.TYPE_LOGIN);
    }
}