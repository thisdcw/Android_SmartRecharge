package com.mxsella.smartrecharge.ui.activity;

import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityManualRechargeBinding;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;
import com.mxsella.smartrecharge.utils.TimerUtils;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class ManualRechargeActivity extends BaseActivity<ActivityManualRechargeBinding> {

    private ICommunicateService iCommunicateService;
    private int remainTimes = 0;
    private int workTime = 0;
    private final AtomicBoolean isReceiver = new AtomicBoolean(false);
    private final AtomicBoolean isPaying = new AtomicBoolean(false);
    private TimerUtils tu;
    private static final int MAX_RETRY_COUNT = 3;
    private int retryCount = 0;
    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    @Override
    public void initView() {
        iCommunicateService = BleService.getInstance();
        iCommunicateService.setListener(this::handlePacket);

        deviceViewModel.getUserRechargeResult().observe(this, result -> {
            if (result.getResultCode()== ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
    }

    private void handlePacket(ReceivePacket packet) {
        switch (packet.getType()) {
            case ReceivePacket.TYPE_INFO:
                handleInfoPacket(packet);
                break;
            case ReceivePacket.TYPE_PAY:
                handlePayPacket(packet);
                break;
            case ReceivePacket.TYPE_MAC:
                handleMacPacket(packet);
                break;
            default:
                LogUtil.test("Unknown packet type received");
                break;
        }
    }

    private void handleInfoPacket(ReceivePacket packet) {
        if (isPaying.get()) {
            isReceiver.set(true);
        }
        remainTimes = packet.getRemainTimes();
        workTime = packet.getWorkTime();
        LogUtil.test("remainTimes => " + remainTimes + " work time => " + workTime);
        runOnUiThread(() -> {
            binding.readRemainTime.setText(remainTimes + " 次");
            binding.readWorkTime.setText(workTime + " s");
        });
    }

    private void handlePayPacket(ReceivePacket packet) {
        remainTimes = packet.getRemainTimes();
        workTime = packet.getWorkTime();
        LogUtil.test("支付成功! remainTimes => " + remainTimes + " work time => " + workTime);
        runOnUiThread(() -> {
            binding.payStatus.setText("充值成功!!! 充值次数: " + remainTimes + " 工作时间: " + workTime);
        });
        isPaying.set(false);
        isReceiver.set(false);
        retryCount = 0;
        tu.cancel();
    }

    private void handleMacPacket(ReceivePacket packet) {
        String mac = packet.getMac();
        LogUtil.test("mac码 -> " + mac);
        runOnUiThread(() -> {
            binding.macAddress.setText("" + mac);
        });
    }

    public void reset(View view) {
        iCommunicateService.send(Protocol.command(PayUtils.encode(0)));
    }

    public void charge(View view) {
        if (Config.isDebug) {
            int times = Integer.parseInt(binding.charge.getText().toString().trim()) + remainTimes;
            deviceViewModel.deviceRecharge("111111", times);
        } else {
            if (!BleService.getInstance().isConnected()) {
                ToastUtils.showToast("请先连接设备");
                return;
            }
            if (isPaying.get()) {
                ToastUtils.showToast("正在充值,请勿重复点击");
                return;
            }
            isPaying.set(true);
            sendChargeCommand();
        }
    }

    private void sendChargeCommand() {
        iCommunicateService.send(Protocol.command(PayUtils.encode()));
        tu = new TimerUtils();
        tu.scheduleTask(3000, this::handleChargeTimeout);
    }

    private void handleChargeTimeout() {
        if (isReceiver.get()) {
            int times = Integer.parseInt(binding.charge.getText().toString().trim()) + remainTimes;
            iCommunicateService.send(Protocol.command(PayUtils.encode(times)));

            //TODO 充值
            deviceViewModel.deviceRecharge("111111", times);
        } else {
            if (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                LogUtil.test("Retrying charge... Attempt: " + retryCount);
                sendChargeCommand();
            } else {
                runOnUiThread(() -> {
                    ToastUtils.showLongToast("充值失败 !!!");
                });
                tu.cancel();
                isPaying.set(false);
                retryCount = 0;
            }
        }
    }

    public void info(View view) {
        if (!isPaying.get()) {
            iCommunicateService.send(Protocol.command(PayUtils.encode()));
        }
    }

    public void mac(View view) {
        iCommunicateService.send(Protocol.MAC);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_manual_recharge;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tu != null) {
            tu.cancel();
        }
    }
}