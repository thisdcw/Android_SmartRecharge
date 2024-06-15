package com.mxsella.smartrecharge.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentDealApplyBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.ui.adapter.ApplyDealListAdapter;
import com.mxsella.smartrecharge.utils.SortUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.DealApplyDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class DealApplyFragment extends BaseFragment<FragmentDealApplyBinding> {

    private int cur = 1;
    private int size = 20;

    private String productName;

    private final ApplyDealListAdapter adapter = new ApplyDealListAdapter();

    private DealApplyDialog dealApplyDialog;

    @Override
    public void initObserve() {
        deviceViewModel.getDealApplyResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                getApplyList();
            } else {
                ToastUtils.showToast(result.getMessage());
            }

        });
        deviceViewModel.getLoadingSate().observe(this, loading -> {
            if (loading) {
                binding.rvRefresh.avi.show();
            } else {
                binding.rvRefresh.avi.hide();
            }
        });
        deviceViewModel.getGetChildApplyListResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ListResponse<ApplyTimes> list = result.getData();
                List<ApplyTimes> records = list.getRecords();
                if (!records.isEmpty()) {
                    SortUtil.sortByDescending(records);
                    adapter.submitList(records);
                } else {
                    binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                }
            } else {
                binding.rvRefresh.text.setText(result.getMessage());
                binding.rvRefresh.empty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void initOnClick() {

        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            ApplyTimes item = adapter.getItem(i);
            if (item == null) {
                return;
            }
            if (item.getApplyState() != 0) {
                ToastUtils.showToast("该消息已被处理");
                return;
            }
            showDealDialog(item);
        });

    }

    @Override
    public void initView() {
        productName = deviceViewModel.getProductName();
        getApplyList();
        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(context));
        binding.rvRefresh.rv.setAdapter(adapter);
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
            refreshlayout.finishRefresh(Constants.FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (productName == null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            size += 20;
            getApplyList();
            refreshlayout.finishLoadMore(Constants.LOAD_DELAY);//传入false表示加载失败
        });
    }

    private void showDealDialog(ApplyTimes applyTimes) {
        dealApplyDialog = new DealApplyDialog(applyTimes.getApplyTimes());
        dealApplyDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                deviceViewModel.dealApply(applyTimes.getApplyId(), true);
            }

            @Override
            public void onCancel() {
                deviceViewModel.dealApply(applyTimes.getApplyId(), false);
            }
        });
        dealApplyDialog.show(getChildFragmentManager(), "deal_apply");
    }

    private void getApplyList() {
        deviceViewModel.getChildApplyList(cur, size);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_deal_apply;
    }
}