package com.mxsella.smartrecharge.ui.fragment;

import android.view.View;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentSettingsBinding;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.ui.activity.ChildUserListActivity;
import com.mxsella.smartrecharge.ui.activity.DeviceListActivity;
import com.mxsella.smartrecharge.ui.activity.InviteCodeListActivity;
import com.mxsella.smartrecharge.view.CustomerImageView;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> {

    private User currentUser = new User();

    @Override
    public void initEventAndData() {
        String json = Config.getCurrentUser();
        currentUser = new Gson().fromJson(json, User.class);
        if (currentUser != null) {
            if (currentUser.getRole().equals(Constants.ROLE_STORE)) {
                binding.lltChildrenUser.setVisibility(View.GONE);
                return;
            }
            binding.group.setText(Constants.roleMap.get(currentUser.getRole()));
            binding.username.setText(currentUser.getUserName());
            binding.role.setText(currentUser.getRole());
            binding.civ.setImageUrl(currentUser.getAvatar());
        }

        binding.lltInviteRecord.setOnClickListener(v -> {
            navTo(InviteCodeListActivity.class);
        });

        binding.lltChildrenUser.setOnClickListener(v -> {
            navTo(ChildUserListActivity.class);
        });

        binding.lltDevice.setOnClickListener(v -> {
            navTo(DeviceListActivity.class);
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }
}