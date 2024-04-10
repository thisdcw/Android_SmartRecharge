package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemBleBinding;
import com.mxsella.smartrecharge.entity.BleDeviceInfo;

public class BleAdapter extends BaseQuickAdapter<BleDeviceInfo, DataBindingHolder<ItemBleBinding>> {
    ItemBleBinding binding;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemBleBinding> itemBleBindingDataBindingHolder, int i, @Nullable BleDeviceInfo bleDeviceInfo) {
        binding = itemBleBindingDataBindingHolder.getBinding();
        binding.setBle(bleDeviceInfo);
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemBleBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemBleBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
