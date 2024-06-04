package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityModifyUserBinding;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class ModifyUserActivity extends BaseActivity<ActivityModifyUserBinding> {

    private final UserViewModel userViewModel = new UserViewModel();

    @Override
    public int layoutId() {
        return R.layout.activity_modify_user;
    }

    @Override
    public void initView() {

        User user = userViewModel.getCurrentUser();
        binding.avatar.setImageUrl(user.getAvatar());
        binding.username.setText(user.getUserName());
        binding.username.requestFocus();
        binding.username.requestFocus(binding.username.getText().length());


        userViewModel.getChangeUserInfo().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                finish(this);
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });

    }

    public void sure(View view) {
        userViewModel.changeUserinfo(binding.username.getText().toString().trim(), "");
    }
}