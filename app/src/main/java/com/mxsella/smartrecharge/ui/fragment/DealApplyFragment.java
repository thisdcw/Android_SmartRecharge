package com.mxsella.smartrecharge.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentDealApplyBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.ui.adapter.ApplyDealListAdapter;
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
    private static final int FRESH_DELAY = 2000;
    private final int LOAD_DELAY = 2000;

    private String productName;

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    private final ApplyDealListAdapter adapter = new ApplyDealListAdapter();

    private DealApplyDialog dealApplyDialog;

    @Override
    public void initEventAndData() {
        productName = deviceViewModel.getProductName();
        getApplyList();
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        binding.rv.setAdapter(adapter);

        RefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            if (productName==null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            cur = 1;
            size = 20;
            getApplyList();
            refreshlayout.finishRefresh(FRESH_DELAY);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            if (productName==null) {
                refreshlayout.finishRefresh(false);//传入false表示刷新失败
            }
            size += 20;
            getApplyList();
            refreshlayout.finishLoadMore(LOAD_DELAY);//传入false表示加载失败
        });
        deviceViewModel.getGetChildApplyListResult().observe(this, result -> {
            if (result.getResultCode()== ResultCode.SUCCESS){
                ListResponse<ApplyTimes> list = result.getData();
                List<ApplyTimes> records = list.getRecords();
                if (!records.isEmpty()) {
                    adapter.submitList(records);
                }
            }
        });
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            ApplyTimes item = adapter.getItem(i);
            showDealDialog(item);
        });
        deviceViewModel.getDealApplyResult().observe(this, result -> {
            if (result.getResultCode()==ResultCode.SUCCESS){
                ToastUtils.showToast(result.getMessage());
                getApplyList();
            }else {
                ToastUtils.showToast(result.getMessage());
            }

        });
        deviceViewModel.getLoadingSate().observe(this,loading->{
            if (loading){
                binding.avi.show();
            }else {
                binding.avi.hide();
            }
        });
    }

    private void showDealDialog(ApplyTimes applyTimes) {
        dealApplyDialog = new DealApplyDialog();
        dealApplyDialog.setDialogListener(new DealApplyDialog.DialogListener() {
            @Override
            public void onConfirmClick() {
                boolean pass = dealApplyDialog.isPass();
                deviceViewModel.dealApply(applyTimes.getApplyId(), pass);
            }

            @Override
            public void onCancelClick() {

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