package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemHistoryBinding;
import com.mxsella.smartrecharge.entity.History;

public class HistoryAdapter extends BaseQuickAdapter<History, DataBindingHolder<ItemHistoryBinding>> {

    ItemHistoryBinding binding;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemHistoryBinding> itemHistoryBindingDataBindingHolder, int i, @Nullable History history) {
        binding.setHistory(history);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemHistoryBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<ItemHistoryBinding>(binding);
    }
}
