package com.mxsella.smartrecharge.ui.fragment;

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
    private ScanDialog scanDialog;

    private int scanValue = 0;

    private DealRechargeDialog dealRechargeDialog;
    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    private String deviceId;

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
        deviceId = Config.getDeviceId();
        binding.remainTimes.setText(getString(R.string.remainTimes, String.valueOf(remainTimes)));
        binding.workTime.setText(getString(R.string.seconds, String.valueOf(workTime)));
        iCommunicateService = BleService.getInstance();

        iCommunicateService.setListener(this::handlePacket);

        binding.scan.setOnClickListener(v -> scanRecharge());
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

    public void scanRecharge() {
        huawei();
    }

    public void huawei() {
        scanDialog = new ScanDialog(context)
                .setScanSize(300)
                .setScanResultCallBack(result -> {
                    HmsScan hmsScan = result[0];
                    handleScanData(hmsScan);
                    scanDialog.dismiss();
                });
        scanDialog.show(getChildFragmentManager(), "customer_scan");
    }

    public void reset() {
        if (!BleService.getInstance().isConnected()) {
            ToastUtils.showToast("请先连接设备");
            return;
        }
        iCommunicateService.send(Protocol.command(PayUtils.encode(0)));
    }

    private void handleScanData(HmsScan data) {
        String value = data.getOriginalValue();
        LogUtil.d("扫描到的内容是: " + value);
        scanValue = Integer.parseInt(value);

        showRechargeDialog();

    }

    private void showRechargeDialog() {
        dealRechargeDialog = new DealRechargeDialog(scanValue);
        dealRechargeDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                deviceViewModel.deviceRecharge(deviceId, scanValue);
                charge();
            }

            @Override
            public void onCancel() {

            }
        });
        dealRechargeDialog.show(getChildFragmentManager(), "scan_recharge_confirm");
    }

    public void charge() {
        LogUtil.d("要充值的次数: " + scanValue);
        if (!BleService.getInstance().isConnected()) {
            ToastUtils.showToast("请先连接设备");
            return;
        }
        //TODO 获取要充值的次数 + remainTimes
        int times = scanValue + remainTimes;
        iCommunicateService.send(Protocol.command(PayUtils.encode(times)));
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
        if (packet.getType().equals(ReceivePacket.TYPE_PAY)) {
            remainTimes = packet.getRemainTimes();
            workTime = packet.getWorkTime();
            LogUtil.test("支付成功! remainTimes => " + remainTimes + " work time => " + workTime);
            //TODO 充值
            deviceViewModel.deviceRecharge(deviceId, scanValue);
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
}