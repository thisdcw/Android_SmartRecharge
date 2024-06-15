package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemChildUserBinding;
import com.mxsella.smartrecharge.inter.ClickClipListener;
import com.mxsella.smartrecharge.inter.OnClickListener;
import com.mxsella.smartrecharge.model.domain.ChildUser;

public class ChildUserAdapter extends BaseQuickAdapter<ChildUser, DataBindingHolder<ItemChildUserBinding>> {

    private ItemChildUserBinding binding;
    private ClickClipListener<String> clickClip;
    private OnClickListener<ChildUser> onClickListener;
    private OnClickListener<ChildUser> changeRemarkListener;

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemChildUserBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable ChildUser user) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setUser(user);
        binding.id.setOnLongClickListener(v -> {
            if (user != null) {
                clickClip.toClipBoard(user.getUid());
            }
            return true;
        });
        binding.subName.setOnClickListener(v -> {
            if (getOnClickListener() != null) {
                onClickListener.onClick(user);
            }
        });
        binding.remark.setOnClickListener(v -> {
            if (getChangeRemarkListener() != null) {
                changeRemarkListener.onClick(user);
            }
        });
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemChildUserBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemChildUserBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }

    public OnClickListener<ChildUser> getChangeRemarkListener() {
        return changeRemarkListener;
    }

    public void setChangeRemarkListener(OnClickListener<ChildUser> changeRemarkListener) {
        this.changeRemarkListener = changeRemarkListener;
    }

    public OnClickListener<ChildUser> getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener<ChildUser> onClickListener) {
        this.onClickListener = onClickListener;
    }


    public ClickClipListener<String> getClickClip() {
        return clickClip;
    }

    public void setClickClip(ClickClipListener<String> clickClip) {
        this.clickClip = clickClip;
    }


}
