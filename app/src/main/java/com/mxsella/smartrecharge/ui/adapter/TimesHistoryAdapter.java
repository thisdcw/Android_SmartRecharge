package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.databinding.ItemApplyDealBinding;
import com.mxsella.smartrecharge.databinding.ItemTimesHistoryBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.UserHistory;

public class TimesHistoryAdapter extends BaseQuickAdapter<UserHistory, DataBindingHolder<ItemTimesHistoryBinding>> {

    private ItemTimesHistoryBinding binding;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemTimesHistoryBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable UserHistory userHistory) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setUserHistory(userHistory);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemTimesHistoryBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemTimesHistoryBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
