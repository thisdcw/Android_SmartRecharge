package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityRechargeCodeListBinding;
import com.mxsella.smartrecharge.model.domain.RechargeCode;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.ui.adapter.RechargeCodeAdapter;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class RechargeCodeListActivity extends BaseActivity<ActivityRechargeCodeListBinding> {

    private final RechargeCodeAdapter rechargeCodeAdapter = new RechargeCodeAdapter();
    private int cur = 1;
    private boolean isCur = false;
    private int size = 20;
    private RefreshLayout refreshLayout;

    @Override
    public int layoutId() {
        return R.layout.activity_recharge_code_list;
    }

    @Override
    public void initView() {
        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRefresh.rv.setAdapter(rechargeCodeAdapter);
        getCodeList();
        refreshLayout = binding.rvRefresh.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        binding.navBar.getRightTextView().setText("当前设备");
    }

    @Override
    public void initObserve() {
        deviceViewModel.getRechargeCodeListResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                List<RechargeCode> records = result.getData().getRecords();
                if (!records.isEmpty()) {
                    rechargeCodeAdapter.submitList(records);
                } else {
                    binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                }
            }
            ToastUtils.showToast(result.getMessage());
        });
        deviceViewModel.getLoadingSate().observe(this, isLoading -> {
            if (isLoading) {
                binding.rvRefresh.avi.smoothToShow();
            } else {
                binding.rvRefresh.avi.smoothToHide();
            }
        });
        deviceViewModel.getUseRechargeCodeResult().observe(this, result -> {
            ToastUtils.showToast(result.getMessage());
            if (result.getResultCode() == ResultCode.SUCCESS) {
                getCodeList();
            }
        });
    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            cur = 1;
            size = 20;
            getCodeList();
            refreshlayout.finishRefresh(Constants.FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            getCodeList();
            refreshlayout.finishLoadMore(Constants.LOAD_DELAY);//传入false表示加载失败
        });
        binding.navBar.getRightTextView().setOnClickListener(v -> {
            if (isCur) {
                binding.navBar.getRightTextView().setText("当前设备");
            } else {
                binding.navBar.getRightTextView().setText("所有设备");
            }
            rechargeCodeAdapter.submitList(null);
            isCur = !isCur;
            getCodeList();
        });
    }

    private void getCodeList() {
        if (isCur) {
            getCurCodeList();
        } else {
            deviceViewModel.getRechargeCodeList(cur, size);
        }
    }

    private void getCurCodeList() {
        String deviceMac = Config.getDeviceMac();
        deviceViewModel.getRechargeCodeList(cur, size, deviceMac);
    }
}