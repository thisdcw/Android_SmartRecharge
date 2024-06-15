package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.databinding.ItemDeviceBinding;
import com.mxsella.smartrecharge.inter.ClickClipListener;
import com.mxsella.smartrecharge.model.domain.Device;

import org.apache.commons.lang3.StringUtils;

public class DeviceListAdapter extends BaseQuickAdapter<Device, DataBindingHolder<ItemDeviceBinding>> {

    private ItemDeviceBinding binding;
    private ClickClipListener<String> clickClip;
    public EditListener editListener;

    public interface EditListener {
        void edit(View v, int position);
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
        if (device != null && StringUtils.isAnyBlank(device.getStoreName())) {
            binding.state.setText("未分配");
            binding.state.setTextColor(getContext().getResources().getColor(R.color.settings_code_list_item_text));
        } else {
            binding.state.setText("已分配");
            binding.state.setTextColor(getContext().getResources().getColor(R.color.primary));
        }
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemDeviceBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemDeviceBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }


    public ClickClipListener<String> getClickClip() {
        return clickClip;
    }

    public void setClickClip(ClickClipListener<String> clickClip) {
        this.clickClip = clickClip;
    }

    public EditListener getEditListener() {
        return editListener;
    }

    public void setEditListener(EditListener editListener) {
        this.editListener = editListener;
    }
}
