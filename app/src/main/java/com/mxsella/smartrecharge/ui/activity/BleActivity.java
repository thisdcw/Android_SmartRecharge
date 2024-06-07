package com.mxsella.smartrecharge.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.mxsella.smartrecharge.MyApplication;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.comm.BleService;
import com.mxsella.smartrecharge.comm.IBleConnectStateCallback;
import com.mxsella.smartrecharge.comm.ICommunicateService;
import com.mxsella.smartrecharge.comm.Protocol;
import com.mxsella.smartrecharge.comm.ReceivePacket;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.Constants;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityBleBinding;
import com.mxsella.smartrecharge.entity.BleDeviceInfo;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.ui.adapter.BleAdapter;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;
import com.mxsella.smartrecharge.viewmodel.DeviceViewModel;

import java.util.ArrayList;
import java.util.List;

public class BleActivity extends BaseActivity<ActivityBleBinding> {

    private ICommunicateService iCommunicateService;
    private List<BleDeviceInfo> bleList = new ArrayList<>();
    private BleAdapter bleAdapter;
    private int position;
    private BleDeviceInfo connectDevice;

    private final DeviceViewModel deviceViewModel = new DeviceViewModel();

    @Override
    public int layoutId() {
        return R.layout.activity_ble;
    }

    @Override
    public void initView() {
        iCommunicateService = BleService.getInstance();
        iCommunicateService.setListener(this::handlePacket);
        bleAdapter = new BleAdapter();
        binding.rcvBle.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvBle.setAdapter(bleAdapter);
        if (BleService.getInstance().isConnected()) {
            BleDevice curDevice = BleService.getInstance().getCurDevice();
            BleDeviceInfo bleDeviceInfo = new BleDeviceInfo();
            bleDeviceInfo.setConnectState(true);
            bleDeviceInfo.setDeviceAddress(curDevice.getMac());
            bleDeviceInfo.setDeviceName(curDevice.getName());
            bleAdapter.add(bleDeviceInfo);
        }
        binding.navBar.getRightTextView().setOnClickListener(v -> {
            scanBle();
        });
        bleAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            position = i;
            connectDevice = bleAdapter.getItem(i);
            if (!BleService.getInstance().isConnected()) {
                LogUtil.d("未连接点击");
                connect();
            } else {
                disConnect();
            }
        });
        deviceViewModel.getDeviceState().observe(this, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                dealBle();
            } else {
                ToastUtils.showToast("无权限");
                disConnect();
            }
        });
    }

    private void handlePacket(ReceivePacket packet) {
        if (packet.getType().equals(ReceivePacket.TYPE_MAC)) {
            String mac = packet.getMac();
            deviceViewModel.getDeviceSate(mac);
            LogUtil.test("mac码 -> " + mac);
        }

    }

    public void connect() {
        BleService.getInstance().connect(connectDevice.getDeviceAddress(), new IBleConnectStateCallback() {
            @Override
            public void startConnect() {
                LogUtil.d("开始连接");
            }

            @Override
            public void connectSuccess() {
                sendBroadcast(new Intent(Constants.BLE_CONNECT));
                LogUtil.d("连接成功");
                connectDevice.setConnectState(true);
                iCommunicateService.send(Protocol.MAC);
                bleAdapter.set(position, connectDevice);
            }

            @Override
            public void connectFailure(String msg) {
                LogUtil.d("连接失败");
            }

            @Override
            public void disConnect() {
                sendBroadcast(new Intent(Constants.BLE_DISCONNECT));
                LogUtil.d("断开连接");
                connectDevice.setConnectState(false);
                bleAdapter.set(position, connectDevice);
                Config.saveBle(new BleDeviceInfo());
                MyApplication.getInstance().setConnected(false);
            }
        });
    }

    public void dealBle() {
        Config.saveBle(connectDevice);
        MyApplication.getInstance().setConnected(true);
    }

    void disConnect() {
        BleService.getInstance().disconnect();
    }

    public void scanBle() {
        boolean enable = BleManager.getInstance().isBlueEnable();
        if (enable) {
            LogUtil.d("扫描中...");
            binding.empty.setVisibility(View.GONE);
            binding.avi.smoothToShow();
            BleManager.getInstance().scan(bleScanCallback);
        } else {
            ToastUtils.showToast("蓝牙未启用");
        }

    }

    private BleScanCallback bleScanCallback = new BleScanCallback() {
        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            binding.avi.smoothToHide();
            List<BleDeviceInfo> deviceInfoList = new ArrayList<>();
            BleDevice curDevice = BleService.getInstance().getCurDevice();
            if (curDevice != null) {
                BleDeviceInfo ble = Config.getBle();
                BleDeviceInfo bleDeviceInfo = new BleDeviceInfo();
                bleDeviceInfo.setDeviceName(ble.getDeviceName());
                bleDeviceInfo.setDeviceAddress(ble.getDeviceAddress());
                bleDeviceInfo.setConnectState(true);
                deviceInfoList.add(bleDeviceInfo);
            }
            if (curDevice == null && scanResultList.size() == 0) {
                ToastUtils.showToast("没有设备");
                binding.empty.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < scanResultList.size(); i++) {
                    BleDeviceInfo bleDeviceInfo = new BleDeviceInfo();
                    BleDevice device = scanResultList.get(i);
                    bleDeviceInfo.setDeviceName(device.getName());
                    bleDeviceInfo.setDeviceAddress(device.getMac());
                    if (device.getName() != null) {
                        deviceInfoList.add(bleDeviceInfo);
                        LogUtil.d("设备 -> " + bleDeviceInfo.getDeviceName());
                    }
                }
                bleList.clear();
                bleList.addAll(deviceInfoList);
                bleAdapter.submitList(deviceInfoList);
                bleAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScanStarted(boolean success) {

        }

        @Override
        public void onScanning(BleDevice bleDevice) {

        }
    };

}