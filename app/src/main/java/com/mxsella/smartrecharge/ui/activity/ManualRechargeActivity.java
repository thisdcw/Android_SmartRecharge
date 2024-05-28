package com.mxsella.smartrecharge.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityManualRechargeBinding;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;
import com.mxsella.smartrecharge.utils.TimerUtils;
import com.mxsella.smartrecharge.utils.ToastUtils;

public class ManualRechargeActivity extends BaseActivity<ActivityManualRechargeBinding> {

    ICommunicateService iCommunicateService;
    private int remainTimes = 0;
    private int workTime = 0;
    private volatile boolean isReceiver = false;
    private volatile boolean isPaying = false;
    private TimerUtils tu;

    @Override
    public void initView() {
        iCommunicateService = BleService.getInstance();
        iCommunicateService.setListener(packet -> {
            switch (packet.getType()) {
                case ReceivePacket.TYPE_INFO:
                    if (isPaying) {
                        isReceiver = true;
                    }
                    remainTimes = packet.getRemainTimes();
                    workTime = packet.getWorkTime();
                    LogUtil.test("remainTimes => " + remainTimes + " work time => " + workTime);
                    runOnUiThread(() -> {
                        binding.readRemainTime.setText(remainTimes + " 次");
                        binding.readWorkTime.setText(workTime + " s");
                    });
                    break;
                case ReceivePacket.TYPE_PAY:
                    remainTimes = packet.getRemainTimes();
                    workTime = packet.getWorkTime();
                    LogUtil.test("支付成功! remainTimes => " + remainTimes + " work time => " + workTime);
                    runOnUiThread(() -> {
                        binding.payStatus.setText("充值成功!!! 充值次数: " + remainTimes + " 工作时间: " + workTime);
                    });
                    isPaying = false;
                    tu.cancel();
                    break;
                case ReceivePacket.TYPE_MAC:
                    String mac = packet.getMac();
                    LogUtil.test("mac码 -> " + mac);
                    runOnUiThread(() -> {
                        binding.macAddress.setText("" + mac);
                    });
                    break;
                default:
                    break;
            }

        });
    }

    public void reset(View view) {
        iCommunicateService.send(Protocol.command(PayUtils.encode(0)));
    }

    public void charge(View view) {
        if (!BleService.getInstance().isConnected()) {
            ToastUtils.showToast("请先连接设备");
            return;
        }
        if (isPaying) {
            ToastUtils.showToast("正在充值,请勿重复点击");
            return;
        }
        isPaying = true;
        iCommunicateService.send(Protocol.command(PayUtils.encode()));
        // 使当前线程休眠三秒钟
        tu = new TimerUtils();
        tu.scheduleTask(3000, () -> {
            if (isReceiver) {
                int times = Integer.parseInt(binding.charge.getText().toString().trim()) + remainTimes;
                iCommunicateService.send(Protocol.command(PayUtils.encode(times)));
            } else {
                runOnUiThread(() -> {
                    ToastUtils.showLongToast("充值失败 !!!");
                });
                tu.cancel();
                isPaying = false;
            }
        });
    }

    public void info(View view) {
        iCommunicateService.send(Protocol.command(PayUtils.encode()));
    }

    public void mac(View view) {
        iCommunicateService.send(Protocol.MAC);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_manual_recharge;
    }
}