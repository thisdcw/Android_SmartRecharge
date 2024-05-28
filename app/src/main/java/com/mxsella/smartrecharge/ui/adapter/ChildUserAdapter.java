package com.mxsella.smartrecharge.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.DataBindingHolder;
import com.mxsella.smartrecharge.databinding.ItemChildUserBinding;
import com.mxsella.smartrecharge.model.User;

public class ChildUserAdapter extends BaseQuickAdapter<User, DataBindingHolder<ItemChildUserBinding>> {

    private ItemChildUserBinding binding;

    private ClickClip clickClip;

    public void setClickClip(ClickClip clickClip) {
        this.clickClip = clickClip;
    }

    public interface ClickClip {
        void toClipBoard(String value);
    }

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemChildUserBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable User user) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setUser(user);
        binding.phone.setOnLongClickListener(v -> {
            if (user != null) {
                clickClip.toClipBoard(user.getTelephone());
            }
            return true;
        });
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemChildUserBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemChildUserBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
