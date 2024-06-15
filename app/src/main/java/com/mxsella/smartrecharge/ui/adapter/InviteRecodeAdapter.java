package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemInviteRecordBinding;
import com.mxsella.smartrecharge.inter.ClickClipListener;
import com.mxsella.smartrecharge.model.domain.InviteRecord;

public class InviteRecodeAdapter extends BaseQuickAdapter<InviteRecord, DataBindingHolder<ItemInviteRecordBinding>> {

    private ItemInviteRecordBinding binding;
    private ClickClipListener<String> clickClip;

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

    public ClickClipListener<String> getClickClip() {
        return clickClip;
    }

    public void setClickClip(ClickClipListener<String> clickClip) {
        this.clickClip = clickClip;
    }
}
