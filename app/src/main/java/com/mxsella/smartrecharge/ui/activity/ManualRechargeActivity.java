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

public class ManualRechargeActivity extends BaseActivity<ActivityManualRechargeBinding> {
    ICommunicateService iCommunicateService;

    private int remainTimes = 0;
    private int workTime = 0;
    @Override
    public void initView() {
        iCommunicateService = BleService.getInstance();
        iCommunicateService.setListener(packet -> {
            switch (packet.getType()) {
                case ReceivePacket.TYPE_INFO:
                    remainTimes = packet.getRemainTimes();
                    workTime = packet.getWorkTime();
                    LogUtil.test("remainTimes => " + remainTimes + "work time => " + workTime);
                    runOnUiThread(() -> {
                        binding.readRemainTime.setText(remainTimes + " 次");
                        binding.readWorkTime.setText(workTime + " s");
                    });
                    break;
                case ReceivePacket.TYPE_PAY:
                    remainTimes = packet.getRemainTimes();
                    workTime = packet.getWorkTime();
                    LogUtil.test("支付成功 !remainTimes => " + remainTimes + "work time => " + workTime);
                    runOnUiThread(() -> {
                        binding.payStatus.setText("充值成功!!! 充值次数: " + remainTimes + "工作时间: " + workTime);
                    });
                    break;
                case ReceivePacket.TYPE_MAC:
                    String mac = packet.getMac();
                    LogUtil.test("mac码-> " + mac);
                    runOnUiThread(() -> {
                        binding.macAddress.setText("" + mac);
                    });
                    break;
                default:
                    break;
            }

        });
    }
    public void reset(View view){
        iCommunicateService.send(Protocol.command(PayUtils.encode(0)));
    }
    public void charge(View view) {
        iCommunicateService.send(Protocol.command(PayUtils.encode()));
        // 使当前线程休眠三秒钟
        try {
            Thread.sleep(3000); // 3000毫秒 = 3秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int times = Integer.parseInt(binding.charge.getText().toString().trim()) + remainTimes;
        iCommunicateService.send(Protocol.command(PayUtils.encode(times)));
    }

    public void info(View view) {
        iCommunicateService.send(Protocol.command(PayUtils.encode()));
    }

    public void mac(View view){
        iCommunicateService.send(Protocol.MAC);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_manual_recharge;
    }
}