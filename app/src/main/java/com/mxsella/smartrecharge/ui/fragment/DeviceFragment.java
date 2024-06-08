package com.mxsella.smartrecharge.ui.fragment;

import android.os.Build;
import android.os.CountDownTimer;
import android.view.View;

import com.huawei.hms.ml.scan.HmsScan;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseFragment;
import com.mxsella.smartrecharge.databinding.FragmentDeviceBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.ui.activity.BleActivity;
import com.mxsella.smartrecharge.ui.activity.ManualRechargeActivity;
import com.mxsella.smartrecharge.ui.activity.ScanRechargeActivity;
import com.mxsella.smartrecharge.ui.activity.UseRechargeCodeActivity;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.DealRechargeDialog;
import com.mxsella.smartrecharge.view.dialog.ScanDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

public class DeviceFragment extends BaseFragment<FragmentDeviceBinding> {
    private ICommunicateService iCommunicateService;
    private int remainTimes = 0;
    private int workTime = 0;
    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

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
    public void initEventAndData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setStatusBarColor(getResources().getColor(R.color.device_fragment_bkg));
        }
        binding.remainTimes.setText(getString(R.string.remainTimes, String.valueOf(remainTimes)));
        binding.workTime.setText(getString(R.string.seconds, String.valueOf(workTime)));
        iCommunicateService = BleService.getInstance();

        iCommunicateService.setListener(this::handlePacket);

        binding.scan.setOnClickListener(v -> useCode());
        binding.update.setOnClickListener(v -> info());
        binding.ble.setOnClickListener(v -> connectBle());
        binding.reset.setOnClickListener(v -> reset());

        deviceViewModel.getDeviceRechargeResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                //充值成功之后查询用户剩余次数
                deviceViewModel.getUserTimes();
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
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