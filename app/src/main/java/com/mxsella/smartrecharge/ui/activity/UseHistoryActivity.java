package com.mxsella.smartrecharge.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.common.db.RechargeHistory;
import com.mxsella.smartrecharge.common.db.RechargeHistoryManager;
import com.mxsella.smartrecharge.databinding.ActivityUseHistoryBinding;
import com.mxsella.smartrecharge.ui.adapter.UseHistoryAdapter;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;
import java.util.stream.Collectors;

public class UseHistoryActivity extends BaseActivity<ActivityUseHistoryBinding> {

    private final UseHistoryAdapter adapter = new UseHistoryAdapter();

    @Override
    public int layoutId() {
        return R.layout.activity_use_history;
    }

    @Override
    public void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
        getHistoryList();

    }

    private void getHistoryList() {
        RechargeHistoryManager rechargeHistoryManager = RechargeHistoryManager.getInstance();
        List<RechargeHistory> allRechargeHistory = rechargeHistoryManager.getAllRechargeHistory();
        LogUtil.test(allRechargeHistory.toString());
        List<RechargeHistory> collect = allRechargeHistory.stream().sorted((h1, h2) -> {
            try {
                int comparison = h1.getCreateTime().compareTo(h2.getCreateTime());
                return -comparison;
            } catch (Exception e) {
                throw new RuntimeException("Error parsing date", e);
            }
        }).collect(Collectors.toList());
        adapter.submitList(collect);
    }
}