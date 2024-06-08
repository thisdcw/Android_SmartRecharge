package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.common.db.RechargeHistory;
import com.mxsella.smartrecharge.databinding.ItemHistoryBinding;
import com.mxsella.smartrecharge.databinding.ItemUseHistoryBinding;
import com.mxsella.smartrecharge.entity.History;

public class UseHistoryAdapter extends BaseQuickAdapter<RechargeHistory, DataBindingHolder<ItemUseHistoryBinding>> {

    ItemUseHistoryBinding binding;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemUseHistoryBinding> itemHistoryBindingDataBindingHolder, int i, @Nullable RechargeHistory rechargeHistory) {
        binding.setHistory(rechargeHistory);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemUseHistoryBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemUseHistoryBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
