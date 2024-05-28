package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemInviteRecordBinding;
import com.mxsella.smartrecharge.model.InviteRecord;

public class InviteRecodeAdapter extends BaseQuickAdapter<InviteRecord, DataBindingHolder<ItemInviteRecordBinding>> {

    private ItemInviteRecordBinding binding;

    private ClickClip clickClip;

    public void setClickClip(ClickClip clickClip) {
        this.clickClip = clickClip;
    }

    public interface ClickClip {
        void toClipBoard(String value);
    }

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemInviteRecordBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable InviteRecord inviteRecord) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setInviteRecord(inviteRecord);
        binding.code.setOnLongClickListener(v -> {
            if (inviteRecord != null) {
                clickClip.toClipBoard(inviteRecord.getCode());
            }
            return true;
        });
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemInviteRecordBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemInviteRecordBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
