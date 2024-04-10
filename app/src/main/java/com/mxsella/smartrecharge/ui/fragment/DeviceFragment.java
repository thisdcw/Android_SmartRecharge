package com.mxsella.smartrecharge.ui.fragment;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentDeviceBinding;
import com.mxsella.smartrecharge.ui.activity.ManualRechargeActivity;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;

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