package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemDeviceBinding;
import com.mxsella.smartrecharge.model.domain.Device;

public class DeviceListAdapter extends BaseQuickAdapter<Device, DataBindingHolder<ItemDeviceBinding>> {

    private ItemDeviceBinding binding;

    private ClickClip clickClip;

    public void setClickClip(ClickClip clickClip) {
        this.clickClip = clickClip;
    }

    public interface ClickClip {
        void toClipBoard(String value);
    }

    public EditListener editListener;

    public interface EditListener {
        void edit(View v, int position);
    }

    public EditListener getEditListener() {
        return editListener;
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemDeviceBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable Device device) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setDevice(device);
        binding.deviceId.setOnLongClickListener(v -> {
            if (device != null) {
                clickClip.toClipBoard(device.getDeviceId());
            }
            return true;
        });
        binding.edit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.edit(v, i);
            }
        });
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemDeviceBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemDeviceBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
