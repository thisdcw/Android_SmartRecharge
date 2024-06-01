package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemApplyDealBinding;
import com.mxsella.smartrecharge.databinding.ItemApplyHistoryBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;

public class ApplyDealListAdapter extends BaseQuickAdapter<ApplyTimes, DataBindingHolder<ItemApplyDealBinding>> {

    private ItemApplyDealBinding binding;

    private ClickClip clickClip;

    public void setClickClip(ClickClip clickClip) {
        this.clickClip = clickClip;
    }

    public interface ClickClip {
        void toClipBoard(String value);
    }

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemApplyDealBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable ApplyTimes applyTimes) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setApply(applyTimes);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemApplyDealBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemApplyDealBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
