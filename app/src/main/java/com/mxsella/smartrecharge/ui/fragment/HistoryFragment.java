package com.mxsella.smartrecharge.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentHistoryBinding;
import com.mxsella.smartrecharge.entity.History;
import com.mxsella.smartrecharge.ui.adapter.HistoryAdapter;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.Date;

public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {
    HistoryAdapter historyAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public void initEventAndData() {
        RefreshLayout refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            LogUtil.test("刷新");
            refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            LogUtil.test("加载");
            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        });
        historyAdapter = new HistoryAdapter();
        History demo = new History();
        demo.setId(1);
        demo.setDate(new Date());
        demo.setTimes(100);
        demo.setMac("123456");
        historyAdapter.add(demo);
        binding.rcvHis.setLayoutManager(new LinearLayoutManager(context));
        binding.rcvHis.setAdapter(historyAdapter);

    }
}