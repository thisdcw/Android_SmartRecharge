package com.mxsella.smartrecharge.ui.activity;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityTimesHistoryBinding;
import com.mxsella.smartrecharge.model.domain.UserHistory;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.ui.adapter.TimesHistoryAdapter;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class TimesHistoryActivity extends BaseActivity<ActivityTimesHistoryBinding> {

    private final TimesHistoryAdapter timesHistoryAdapter = new TimesHistoryAdapter();
    private int cur = 1;
    private int size = 20;

    @Override
    public int layoutId() {
        return R.layout.activity_times_history;
    }

    @Override
    public void initView() {
        binding.rvRefresh.rv.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvRefresh.rv.setAdapter(timesHistoryAdapter);
        binding.rvRefresh.refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        binding.rvRefresh.refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        getUserTimesHistory();
    }

    @Override
    public void initObserve() {
        deviceViewModel.getGetUserTimesHistoryListResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                List<UserHistory> records = result.getData().getRecords();
                if (!records.isEmpty()) {
                    timesHistoryAdapter.submitList(records);
                } else {
                    binding.rvRefresh.empty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initListener() {
        binding.rvRefresh.refreshLayout.setOnRefreshListener(refreshlayout -> {

            cur = 1;
            size = 20;
            getUserTimesHistory();
            refreshlayout.finishRefresh(Constants.FRESH_DELAY);//传入false表示刷新失败
        });
        binding.rvRefresh.refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            size += 20;
            getUserTimesHistory();
            refreshlayout.finishLoadMore(Constants.LOAD_DELAY);//传入false表示加载失败
        });
    }

    private void getUserTimesHistory() {
        deviceViewModel.getUserTimesHistoryList(cur, size);
    }
}