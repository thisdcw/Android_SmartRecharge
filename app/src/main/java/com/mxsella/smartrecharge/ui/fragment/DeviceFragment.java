package com.mxsella.smartrecharge.ui.fragment;

import android.os.Build;
import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.common.manager.ObserverManager;
import com.mxsella.smartrecharge.databinding.FragmentDeviceBinding;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.ui.activity.BleActivity;
import com.mxsella.smartrecharge.ui.activity.UseRechargeCodeActivity;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class DeviceFragment extends BaseFragment<FragmentDeviceBinding> {
    private ICommunicateService iCommunicateService;
    private int remainTimes = 0;
    private int workTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    public void onResume() {
        super.onResume();
        info();
        binding.ble.setSelected(BleService.getInstance().isConnected());
    }

    @Override
    public void initObserve() {
        deviceViewModel.getDeviceRechargeResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                //充值成功之后查询用户剩余次数
                deviceViewModel.getUserTimes();
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
        deviceViewModel.getBindDeviceState().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast("绑定成功");
                getDeviceState();
            } else {
                ToastUtils.showToast("绑定失败");
            }
        });

        deviceViewModel.getDeviceState().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                String storeId = result.getData().getStore();
                String curId = deviceViewModel.getCurrentUser().getUid();
                if (!StringUtils.isAnyBlank(storeId) && storeId.equals(curId)) {
                    binding.lltBind.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void initOnClick() {
        binding.scan.setOnClickListener(v -> useCode());
        binding.update.setOnClickListener(v -> info());
        binding.ble.setOnClickListener(v -> connectBle());
        binding.reset.setOnClickListener(v -> reset());
        getDeviceState();

        binding.lltBind.setOnClickListener(v -> {
            if (!BleService.getInstance().isConnected() && !Config.isDebug) {
                ToastUtils.showToast("请先连接设备");
                return;
            }
            //TODO 绑定设备
            deviceViewModel.bindDevice(Config.getDeviceMac());
        });
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setStatusBarColor(getResources().getColor(R.color.device_fragment_bkg));
        }
        User user = deviceViewModel.getCurrentUser();
        if (user.getRole().equals(UserEnum.STORE.getRole())) {
            binding.lltTip.setVisibility(View.GONE);
        } else {
            binding.lltTip.setVisibility(View.VISIBLE);
        }
        binding.remainTimes.setText(getString(R.string.remainTimes, String.valueOf(remainTimes)));
        binding.workTime.setText(getString(R.string.seconds, String.valueOf(workTime)));
        iCommunicateService = BleService.getInstance();
        iCommunicateService.setListener(this::handlePacket);
    }

    private void getDeviceState() {
        deviceViewModel.getDeviceSate(Config.getDeviceMac());
    }

    public void connectBle() {
        navTo(BleActivity.class);
    }

    public void useCode() {
        navTo(UseRechargeCodeActivity.class);
    }

    public void reset() {

    }


    private void handlePacket(ReceivePacket packet) {
        if (packet.getType().equals(ReceivePacket.TYPE_INFO)) {
            remainTimes = packet.getRemainTimes();
            workTime = packet.getWorkTime();
            binding.remainTimes.setText(getString(R.string.remainTimes, String.valueOf(remainTimes)));
            binding.workTime.setText(getString(R.string.seconds, String.valueOf(workTime)));
            LogUtil.i(remainTimes + " 次");
            LogUtil.i(workTime + " s");
        }
        if (packet.getType().equals(ReceivePacket.TYPE_MAC)) {
            String mac = packet.getMac();
            LogUtil.test("mac码 -> " + mac);
        }
    }

    /**
     * 获取设备信息以及mac码
     */
    private void info() {
        if (!BleService.getInstance().isConnected()) {
            ToastUtils.showToast("请先连接设备");
            return;
        }
        iCommunicateService.send(Protocol.command(PayUtils.encode()));
        iCommunicateService.send(Protocol.MAC);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}