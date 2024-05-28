package com.mxsella.smartrecharge.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityWelcomeBinding;
import com.mxsella.smartrecharge.utils.LogUtil;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    private static final long DELAY_TIME = 3000; // 3秒

    @Override
    public int layoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        // 在3秒后跳转
        new Handler().postDelayed(() -> {
            String user = Config.getCurrentUser();
            if (Config.checkLogin && user.isEmpty()) {
                navToWithFinish(LoginActivity.class);
            } else {
                navToWithFinish(MainActivity.class);
            }
        }, DELAY_TIME);
    }
}