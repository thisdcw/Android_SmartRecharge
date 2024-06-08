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
import com.mxsella.smartrecharge.databinding.GridItemBinding;
import com.mxsella.smartrecharge.databinding.ItemApplyDealBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.RechargeCode;

public class GridItemAdapter extends BaseQuickAdapter<RechargeCode, DataBindingHolder<GridItemBinding>> {

    private GridItemBinding binding;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<GridItemBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable RechargeCode rechargeCode) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setRecharge(rechargeCode);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<GridItemBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = GridItemBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
