package com.mxsella.smartrecharge.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentRechargeApplyBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.ui.adapter.ApplyHistoryListAdapter;
import com.mxsella.smartrecharge.utils.SortUtil;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class RechargeApplyFragment extends BaseFragment<FragmentRechargeApplyBinding> {

    private int cur = 1;
    private int size = 20;
    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;
    private final ApplyHistoryListAdapter applyHistoryListAdapter = new ApplyHistoryListAdapter();

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    private String productName;

    @Override
    public void initEventAndData() {
        productName = deviceViewModel.getProductName();

        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(context));
        binding.rvRefresh.rv.setAdapter(applyHistoryListAdapter);
        RefreshLayout refreshLayout = binding.rvRefresh.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            cur = 1;
            size = 20;
            getApplyList();
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            size += 20;
            getApplyList();
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });
        deviceViewModel.getGetApplyListResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ListResponse<ApplyTimes> data = result.getData();
                List<ApplyTimes> records = data.getRecords();
                if (!records.isEmpty()) {
                    SortUtil.sortByDescending(records);
                    applyHistoryListAdapter.submitList(records);
                } else {
                    binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                }
            } else {
                binding.rvRefresh.empty.setVisibility(View.VISIBLE);
            }

        });
        deviceViewModel.getLoadingSate().observe(this, loading -> {
            if (loading) {
                binding.rvRefresh.avi.show();
            } else {
                binding.rvRefresh.avi.hide();
            }
        });
        getApplyList();
    }

    private void getApplyList() {
        deviceViewModel.getApplyList(cur, size);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recharge_apply;
    }
}