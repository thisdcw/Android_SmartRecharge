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
import com.mxsella.smartrecharge.databinding.ItemRechargeCodeBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.RechargeCode;

public class RechargeCodeAdapter extends BaseQuickAdapter<RechargeCode, DataBindingHolder<ItemRechargeCodeBinding>> {

    private ItemRechargeCodeBinding binding;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemRechargeCodeBinding> itemRechargeCodeBindingDataBindingHolder, int i, @Nullable RechargeCode rechargeCode) {
        binding = itemRechargeCodeBindingDataBindingHolder.getBinding();
        binding.setRechargeCode(rechargeCode);
        if (rechargeCode != null) {
            int state = rechargeCode.getRechargeState();
            switch (state) {
                case 0:
                    binding.state.setTextColor(ContextCompat.getColor(getContext(), R.color.list_item_state_pending));
                    break;
                case 1:
                    binding.state.setTextColor(ContextCompat.getColor(getContext(), R.color.list_item_state_pass));
                    break;
            }
        }
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemRechargeCodeBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemRechargeCodeBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
