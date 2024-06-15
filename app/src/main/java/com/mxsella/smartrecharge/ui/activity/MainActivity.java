package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.db.RechargeHistory;
import com.mxsella.smartrecharge.common.db.RechargeHistoryManager;
import com.mxsella.smartrecharge.databinding.ActivityMainBinding;
import com.mxsella.smartrecharge.model.domain.RechargeCode;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PermissionUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private List<RechargeHistory> collect;

    private RechargeHistoryManager historyManager;

    @Override
    public void initView() {
        PermissionUtil.getInstance(this).requestBlePermission().requestExternalPermission().requestCameraPermission();
        historyManager = RechargeHistoryManager.getInstance();
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
        checkData();
        deviceViewModel.getUserTimes();
    }

    @Override
    public void initObserve() {
        deviceViewModel.getUseRechargeCodeResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                //更新状态
                RechargeCode data = result.getData();
                RechargeHistory historyById = historyManager.getHistoryById(data.getHistoryId());
                historyById.setCheck(true);
                historyManager.saveOrUpdate(historyById);
            }
        });

        deviceViewModel.getGetUserTimesResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                Config.saveRemainTimes(result.getData());
                ToastUtils.showToast(result.getResultCode().getMessage());
            } else {
                ToastUtils.showToast(result.getResultCode().getMessage());
            }
        });
    }

    @Override
    public void initListener() {

    }

    public void connectBle(View view) {
        navTo(BleActivity.class);
    }

    public void manualRecharge(View view) {
        navTo(ManualRechargeActivity.class);
    }


    public void entry(View view) {
        navTo(ProductEntryActivity.class);
    }

    public void logout(View view) {
        Config.setLogin(false);
        Config.saveRemainTimes(0);
        navToFinishAll(WelcomeActivity.class);
    }

    private void checkData() {
        String uid = userViewModel.getCurrentUser().getUid();
        String productName = deviceViewModel.getProductName();
        if (uid == null || productName == null) {
            ToastUtils.showToast("请先设置产品名称");
            return;
        }
        List<RechargeHistory> history = historyManager.getAllRechargeHistoryByKeyword(uid, productName);
        if (history != null) {
            collect = history.stream().filter(h -> h.isPay() && !h.isCheck()).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                RechargeHistory rechargeHistory = collect.get(i);
                deviceViewModel.useRechargeCode(rechargeHistory.getHistoryId());
            }
            LogUtil.test(collect.toString());
        }

    }

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }
}