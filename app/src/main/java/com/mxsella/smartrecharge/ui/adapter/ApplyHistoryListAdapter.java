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
import com.mxsella.smartrecharge.databinding.ItemApplyHistoryBinding;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;

public class ApplyHistoryListAdapter extends BaseQuickAdapter<ApplyTimes, DataBindingHolder<ItemApplyHistoryBinding>> {

    private ItemApplyHistoryBinding binding;

    private ClickClip clickClip;

    public void setClickClip(ClickClip clickClip) {
        this.clickClip = clickClip;
    }

    public interface ClickClip {
        void toClipBoard(String value);
    }

    @Override
    protected void onBindViewHolder(@NonNull DataBindingHolder<ItemApplyHistoryBinding> itemInviteRecordBindingDataBindingHolder, int i, @Nullable ApplyTimes applyTimes) {
        binding = itemInviteRecordBindingDataBindingHolder.getBinding();
        binding.setApply(applyTimes);
        if (applyTimes != null) {
            int state = applyTimes.getApplyState();
            switch (state) {
                case 0:
                    binding.state.setTextColor(ContextCompat.getColor(getContext(), R.color.list_item_state_pending));
                    break;
                case 1:
                    binding.state.setTextColor(ContextCompat.getColor(getContext(), R.color.list_item_state_pass));
                    break;
                case 2:
                    binding.state.setTextColor(ContextCompat.getColor(getContext(), R.color.list_item_state_refuse));
                    break;
            }
        }
        binding.executePendingBindings();
    }

    @NonNull
    @Override
    protected DataBindingHolder<ItemApplyHistoryBinding> onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        binding = ItemApplyHistoryBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new DataBindingHolder<>(binding);
    }
}
