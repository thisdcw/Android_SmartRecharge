package com.mxsella.smartrecharge.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityModifyUserBinding;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class ModifyUserActivity extends BaseActivity<ActivityModifyUserBinding> {

    private final UserViewModel userViewModel = new UserViewModel();
    private static final int PICK_IMAGE = 1;

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

        binding.avatar.setOnClickListener(v -> {
            openGallery();
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                binding.avatar.setBitMapImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sure(View view) {
        userViewModel.changeUserinfo(binding.username.getText().toString().trim(), "");
    }
}