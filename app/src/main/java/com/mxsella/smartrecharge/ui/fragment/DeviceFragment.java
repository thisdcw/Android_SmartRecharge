package com.mxsella.smartrecharge.ui.fragment;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentDeviceBinding;
import com.mxsella.smartrecharge.ui.activity.ManualRechargeActivity;

public class DeviceFragment extends BaseFragment<FragmentDeviceBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    public void initEventAndData() {
        binding.manual.setOnClickListener(v -> {
            navTo(ManualRechargeActivity.class);
        });
    }
}