package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityRechargeCodeListBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.RechargeCode;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.ui.adapter.RechargeCodeAdapter;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.DealApplyDialog;
import com.mxsella.smartrecharge.view.dialog.UseRechargeCodeDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class RechargeCodeListActivity extends BaseActivity<ActivityRechargeCodeListBinding> {

    private final RechargeCodeAdapter rechargeCodeAdapter = new RechargeCodeAdapter();
    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;
    private int cur = 1;

    private int size = 20;
    private UseRechargeCodeDialog useRechargeCodeDialog;
    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    @Override
    public int layoutId() {
        return R.layout.activity_recharge_code_list;
    }

    @Override
    public void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(rechargeCodeAdapter);
        getCodeList();
        RefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            cur = 1;
            size = 20;
            getCodeList();
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            getCodeList();
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });

        deviceViewModel.getGetRechargeCodeListResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                List<RechargeCode> records = result.getData().getRecords();
                if (!records.isEmpty()) {
                    rechargeCodeAdapter.submitList(records);
                } else {
                    binding.empty.setVisibility(View.VISIBLE);
                }
            }
            ToastUtils.showToast(result.getMessage());
        });
        deviceViewModel.getLoadingSate().observe(this, isLoading -> {
            if (isLoading) {
                binding.avi.smoothToShow();
            } else {
                binding.avi.smoothToHide();
            }
        });
        rechargeCodeAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            RechargeCode rechargeCode = rechargeCodeAdapter.getItem(i);
            if (rechargeCode == null) {
                return;
            }
            showUseRechargeDialog(rechargeCode);
        });
        deviceViewModel.getUseRechargeCodeResult().observe(this,result -> {
            ToastUtils.showToast(result.getMessage());
            if (result.getResultCode()==ResultCode.SUCCESS){
               getCodeList();
            }
        });
    }

    private void showUseRechargeDialog(RechargeCode rechargeCode) {
        useRechargeCodeDialog = new UseRechargeCodeDialog(deviceViewModel.getProductName());

        useRechargeCodeDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                deviceViewModel.useRechargeCode(rechargeCode.getHistoryId());
            }

            @Override
            public void onCancel() {

            }
        });

        useRechargeCodeDialog.show(getSupportFragmentManager(), "use_recharge_code");
    }

    private void getCodeList() {
        deviceViewModel.getRechargeCodeList(cur, size);
    }
}