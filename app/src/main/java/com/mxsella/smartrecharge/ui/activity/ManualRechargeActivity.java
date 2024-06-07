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
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.PayUtils;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.view.dialog.InputDialog;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

public class ManualRechargeActivity extends BaseActivity<ActivityManualRechargeBinding> {

    private ICommunicateService iCommunicateService;
    private int remainTimes = 0;
    private int rechargeTimes = 0;
    private int workTime = 0;
    private String deviceId;
    private final DeviceViewModel deviceViewModel = new DeviceViewModel();
    private final UserViewModel userViewModel = new UserViewModel();

    private InputDialog diyRechargeDialog;

    private final int[][] card_time = {
            {10, 30, 50},
            {100, 200, 300},
            {500, 800}
    };

    @Override
    public void initView() {
        //TODO 此处要获取到设备id
        deviceId = Config.getDeviceId();
        iCommunicateService = BleService.getInstance();
        iCommunicateService.setListener(this::handlePacket);

        if (userViewModel.getCurrentUser().getRole().equals(UserEnum.ADMIN.getRole())) {
            binding.remainTimes.setText("∞");
        } else {
            binding.remainTimes.setText(String.valueOf(Config.getRemainTimes()));
        }

        deviceViewModel.getDeviceRechargeResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                ToastUtils.showToast(result.getMessage());
                //充值成功之后查询用户剩余次数
                deviceViewModel.getUserTimes();
            } else {
                ToastUtils.showToast(result.getMessage());
            }
        });
        deviceViewModel.getGetUserTimesResult().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                binding.remainTimes.setText(String.valueOf(result.getData()));
                Config.saveRemainTimes(result.getData());
                ToastUtils.showToast(result.getResultCode().getMessage());
            } else {
                ToastUtils.showToast(result.getResultCode().getMessage());
            }
        });
        initCardOnClick();
    }

    private void initCardOnClick() {
        binding.card11.setOnClickListener(v -> {
            rechargeTimes = card_time[0][0];
            setSelect();
            binding.card11.setSelected(true);
        });
        binding.card12.setOnClickListener(v -> {
            rechargeTimes = card_time[0][1];
            setSelect();
            binding.card12.setSelected(true);
        });
        binding.card13.setOnClickListener(v -> {
            rechargeTimes = card_time[0][2];
            setSelect();
            binding.card13.setSelected(true);
        });
        binding.card21.setOnClickListener(v -> {
            rechargeTimes = card_time[1][0];
            setSelect();
            binding.card21.setSelected(true);
        });
        binding.card22.setOnClickListener(v -> {
            rechargeTimes = card_time[1][1];
            setSelect();
            binding.card22.setSelected(true);
        });
        binding.card23.setOnClickListener(v -> {
            rechargeTimes = card_time[1][2];
            setSelect();
            binding.card23.setSelected(true);
        });
        binding.card31.setOnClickListener(v -> {
            rechargeTimes = card_time[2][0];
            setSelect();
            binding.card31.setSelected(true);
        });
        binding.card32.setOnClickListener(v -> {
            rechargeTimes = card_time[2][1];
            setSelect();
            binding.card32.setSelected(true);
        });
        binding.diy.setOnClickListener(v -> {
            showDiyTimes();
            setSelect();
            binding.diy.setSelected(true);
        });
    }

    public void setSelect() {
        binding.card11.setSelected(false);
        binding.card12.setSelected(false);
        binding.card13.setSelected(false);
        binding.card21.setSelected(false);
        binding.card22.setSelected(false);
        binding.card23.setSelected(false);
        binding.card31.setSelected(false);
        binding.card32.setSelected(false);
        binding.diy.setSelected(false);
    }

    private void showDiyTimes() {
        diyRechargeDialog = new InputDialog("请输入你要充值的次数");
        diyRechargeDialog.setDialogListener(new DialogClickListener() {
            @Override
            public void onConfirm() {
                String input = diyRechargeDialog.getInput();
                rechargeTimes = Integer.parseInt(input);
                binding.diyTimes.setText(input);
            }

            @Override
            public void onCancel() {

            }
        });

        diyRechargeDialog.show(getSupportFragmentManager(), "diy_recharge");

    }

    private void handlePacket(ReceivePacket packet) {
        if (packet.getType().equals(ReceivePacket.TYPE_PAY)) {
            remainTimes = packet.getRemainTimes();
            workTime = packet.getWorkTime();
            LogUtil.test("支付成功! remainTimes => " + remainTimes + " work time => " + workTime);
            //TODO 充值
            deviceViewModel.deviceRecharge(deviceId, rechargeTimes);
        } else {
            LogUtil.test("Unknown packet type received");
        }
    }


    public void charge(View view) {
        LogUtil.d("要充值的次数: " + rechargeTimes);
        if (!BleService.getInstance().isConnected()) {
            ToastUtils.showToast("请先连接设备");
            return;
        }
        //TODO 获取要充值的次数 + remainTimes
        int times = rechargeTimes + remainTimes;
        iCommunicateService.send(Protocol.command(PayUtils.encode(times)));
    }


    @Override
    public int layoutId() {
        return R.layout.activity_manual_recharge;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}