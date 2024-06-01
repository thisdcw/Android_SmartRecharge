package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityMainBinding;
import com.mxsella.smartrecharge.utils.PermissionUtil;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    public void initView() {
        PermissionUtil.getInstance(this).requestBlePermission().requestExternalPermission().requestCameraPermission();

        initEvent();


    }

    public void connectBle(View view) {
        navTo(BleActivity.class);
    }

    public void scanRecharge(View view) {
        navTo(ScanRechargeActivity.class);
    }

    public void manualRecharge(View view) {
        navTo(ManualRechargeActivity.class);
    }

    public void info(View view) {
        navTo(DeviceInfoActivity.class);
    }

    public void entry(View view) {
        navTo(ProductEntryActivity.class);
    }

    public void logout(View view) {
        Config.setLogin(false);
        navToFinishAll(WelcomeActivity.class);
    }

    private void initEvent() {
        NavController nav = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.device_fragment) {
                nav.navigate(R.id.device_fragment);
                return true;
            } else if (item.getItemId() == R.id.history_fragment) {
                nav.navigate(R.id.history_fragment);
                return true;
            } else if (item.getItemId() == R.id.settings_fragment) {
                nav.navigate(R.id.settings_fragment);
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }
}