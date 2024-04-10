package com.mxsella.smartrecharge.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.mxsella.smartrecharge.MyApplication;
import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseActivity;
import com.mxsella.smartrecharge.databinding.ActivityUsbactivityBinding;
import com.mxsella.smartrecharge.inter.DataTransListerner;
import com.mxsella.smartrecharge.utils.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

public class USBActivity extends BaseActivity<ActivityUsbactivityBinding> {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private HashMap<String, UsbDevice> deviceList;
    private UsbDevice usbDevice;
    UsbManager usbManager;
    private PendingIntent intent;
    private UsbInterface usbInterface;
    private UsbEndpoint usbEpOut;
    private UsbEndpoint usbEpIn;
    private UsbDeviceConnection deviceConnection;
    private boolean isRequestPermission = false;
    private ReceiveThread mReceiveThread;
    private SendThread mSendThread;
    private boolean isRunning = false;
    private DataTransListerner mDataTransListerner;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        LogUtil.i("-> 1111");
                        if (device.getVendorId() == 1027 && device.getProductId() == 24596) {
                            USBActivity.this.usbDevice = device;
                            LogUtil.i("-> 1112" + usbDevice);
                        }
                        findInterface();
                        start();
                    } else {
                        LogUtil.d("Permission denied for device " + device);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                // 处理设备连接
                LogUtil.d("连接");
                initUsbDevice();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                // 处理设备断开
                LogUtil.d("连接断开");
                isRunning = false;
            }
        }
    };


    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_usbactivity;
    }

    @Override
    public void initView() {
        intent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        registerReceiver();
    }

    public boolean initUsbDevice() {

        UsbManager usbManager = (UsbManager) MyApplication.getInstance().getSystemService(Context.USB_SERVICE);

        this.usbManager = usbManager;
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        this.deviceList = deviceList;
        LogUtil.d("初始化");
        for (UsbDevice usbDevice : deviceList.values()) {
            if (usbDevice.getVendorId() == 1027 && usbDevice.getProductId() == 24596) {
                LogUtil.d("找到设备");
                this.usbDevice = usbDevice;
            }
        }
        findInterface();
        return start();
    }

    private void getEndpoint(UsbInterface usbInterface) {
        LogUtil.i("getEndpoint1: " + usbInterface.getEndpointCount());
        for (int endpointCount = usbInterface.getEndpointCount() - 1; endpointCount >= 0; endpointCount--) {
            UsbEndpoint endpoint = usbInterface.getEndpoint(endpointCount);
            LogUtil.i("getEndpoint: " + endpoint.getType());
            if (endpoint.getType() == 2) {
                if (endpoint.getDirection() == 0) {
                    LogUtil.d("1");
                    this.usbEpOut = endpoint;
                } else {
                    LogUtil.d("2");
                    this.usbEpIn = endpoint;
                }
            }
        }
    }

    public void findInterface() {
        UsbDevice usbDevice = this.usbDevice;
        if (usbDevice == null) {
            return;
        }
        if (usbDevice.getInterfaceCount() > 0) {
            UsbInterface usbInterface = this.usbDevice.getInterface(0);
            this.usbInterface = usbInterface;
            LogUtil.i("22" + usbInterface.toString());
        }
        getEndpoint(this.usbInterface);
        if (this.usbInterface != null) {
            LogUtil.i("usbInterface not null == " + (usbDevice == null ? "null -> " : "not null -> ") + usbManager.hasPermission(usbDevice));
            HashMap<String, UsbDevice> deviceList1 = usbManager.getDeviceList();
            for (int i = 0; i < deviceList1.size(); i++) {
                UsbDevice usbDevice1 = deviceList1.get(i);
                System.out.println("device => " + usbDevice1);
            }
            if (usbManager.hasPermission(usbDevice)) {
                LogUtil.i("已经获得权限");
                UsbDeviceConnection openDevice = this.usbManager.openDevice(usbDevice);
                boolean b = openDevice.claimInterface(this.usbInterface, true);
                LogUtil.i("打开设备" + (openDevice != null ? "true" : "false") + " claimInterface-> " + b);
                if (openDevice == null) {
                    LogUtil.i("设备连接为空");
                } else if (openDevice != null && openDevice.claimInterface(this.usbInterface, true)) {
                    this.deviceConnection = openDevice;
                    LogUtil.i("连接状态-> " + (openDevice != null ? "true" : "false"));
                } else {
                    LogUtil.i("关闭连接 ");
                    openDevice.close();
                }
            } else if (isRequestPermission) {
                LogUtil.d("已经请求权限");
            } else {
                LogUtil.d("请求权限");
                isRequestPermission = true;
                this.usbManager.requestPermission(this.usbDevice, this.intent);
            }
        }
    }

    public boolean start() {
        LogUtil.i("开始" + isRunning + "连接状态 -> " + (deviceConnection == null ? "null" : "not null"));
        if (!this.isRunning && this.deviceConnection != null) {
            LogUtil.d("非运行状态已连接");
            SendThread sendThread = this.mSendThread;
            if (sendThread != null && sendThread.isRunning) {
                this.mSendThread.close();
            }
            this.mSendThread = new SendThread();
            ReceiveThread receiveThread = this.mReceiveThread;
            if (receiveThread != null && receiveThread.isRunning) {
                this.mReceiveThread.close();
            }
            this.mReceiveThread = new ReceiveThread();
            this.isRunning = true;
            this.mSendThread.start();
            this.mReceiveThread.start();
        }
        if (this.isRunning) {
            LogUtil.d("运行状态");

        } else {
            LogUtil.d("非运行状态");
        }
        LogUtil.i("运行状态:" + isRunning);
        return this.isRunning;
    }

    private class ReceiveThread extends Thread {
        private boolean isRunning;

        private ReceiveThread() {
            this.isRunning = true;
        }

        public void close() {
            this.isRunning = false;
        }

        public void run() {
            super.run();
            Log.i("OTGManager", "run: ReceiveThread");
            try {
                try {
                    byte[] bArr = new byte[512];
                    while (this.isRunning) {
                        int i1 = deviceConnection.bulkTransfer(usbEpIn, bArr, 512, 3000);
                        LogUtil.d("读取otg 设备数据 => " + Arrays.toString(bArr) + "数据长度" + i1);
                        if (i1 > 2) {
                            byte[] bArr2 = new byte[496];
                            System.arraycopy(bArr, 13, bArr2, 0, 496);
                            byte b = bArr[12];
                            if (b == 16) {
                                Log.i("OTGManager", "run: start");
                            } else if (b == 1) {
                                Log.i("OTGManager", "run: end");
                            }
                            LogUtil.e("-->1");
                            if (mDataTransListerner != null) {
                                LogUtil.e("-->2");
                                mDataTransListerner.onImageData(bArr2, DataTransListerner.ProtocolType.OTG);
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("-->12");
                    Log.e("OTGManager", "run: " + e.getMessage());
                    e.printStackTrace();
                    close();
                    this.isRunning = false;
                }
            } catch (Throwable th) {
                close();
                LogUtil.e("-->13");
                this.isRunning = false;
                throw th;
            }
            LogUtil.e("-->16");
        }
    }

    public class SendThread extends Thread {
        private boolean isRunning;

        private SendThread() {
            this.isRunning = true;
        }

        public void close() {
            this.isRunning = false;
            interrupt();
        }

        @Override
        public void run() {
            int currentSendNum;
            super.run();
            LogUtil.i("run: SendThread");
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 0;
            while (this.isRunning) {
//                if (fimwareQueue.isEmpty() || !isSendFirmwareData) {
//                    if (SystemClock.uptimeMillis() - currentTime > 50) {
//                        if (!msgsQueue.isEmpty()) {
//                            Log.i(TAG, "run: " + (SystemClock.uptimeMillis() - currentTime));
//                            DeviceMsg deviceMsg = (DeviceMsg) msgsQueue.poll();
//                            deviceMsg.setLastSendTime(SystemClock.uptimeMillis());
//                            Log.i(TAG, "send: " + Integer.toHexString(deviceMsg.getMsgId()));
//                            sendDatas(deviceMsg);
//                        }
//                    } else {
//
//                        Log.i(TAG, "run: send error");
//                        return;
//                    }
//                    i++;
//                    if (i > 3) {
//                        if (!checkIsConnectDevice()) {
//                            LogUtil.d("未连接关闭");
//                            closeSession();
//                        }
//                        i = 0;
//                    }
//                    try {
//                        Thread.sleep(60L);
//                    } catch (InterruptedException e2) {
//                        e2.printStackTrace();
//                    }
//                } else {
//                    DeviceMsg deviceMsg2 = (DeviceMsg) fimwareQueue.poll();
//                    deviceMsg2.setCurrentSendNum(Videoio.CAP_AVFOUNDATION);
//                    isFimwareFlag = false;
//                    if (((FirmwareUpdateMsg) deviceMsg2).getFirmwareFlag() == 16) {
//                        OTGManager oTGManager = OTGManager.this;
//                        oTGManager.fimwareAllSize = oTGManager.fimwareQueue.size();
//                        fimwareCurrentSize = 0;
//                        mDataTransListerner.onCmdMessage(Constant.DEVICE_FIRMWARE_UPDATE, new byte[4], 16, DataTransListerner.ProtocolType.OTG);
//                    } else {
//                        OTGManager oTGManager2 = OTGManager.this;
//                        oTGManager2.fimwareCurrentSize = oTGManager2.fimwareAllSize - fimwareQueue.size();
//                    }
//                    Log.i("", "=====================Send=firmwareCurrentSize=" + fimwareCurrentSize + " size=" + fimwareQueue.size());
//                    while (true) {
//                        currentSendNum = deviceMsg2.getCurrentSendNum();
//                        if (currentSendNum < 0) {
//                            break;
//                        }
//                        if (currentSendNum % 600 == 0) {
//                            sendDatas(deviceMsg2);
//                        }
//                        if (isFimwareFlag) {
//                            break;
//                        }
//                        deviceMsg2.setCurrentSendNum(deviceMsg2.getCurrentSendNum() - 1);
//                        try {
//                            Thread.sleep(5L);
//                        } catch (InterruptedException e3) {
//                            e3.printStackTrace();
//                        }
//                    }
//                    if (currentSendNum == -1) {
//                        mDataTransListerner.onCmdMessage(Constant.DEVICE_FIRMWARE_UPDATE, new byte[4], 3, DataTransListerner.ProtocolType.OTG);
//                        fimwareQueue.clear();
//                    }
//                }
            }
            this.isRunning = false;
        }
    }

    public void closeSession() {
        this.isRequestPermission = false;
        this.isRunning = false;
//        Queue<DeviceMsg> queue = this.fimwareQueue;
//        if (queue != null) {
//            queue.clear();
//        }
//        Queue<DeviceMsg> queue2 = this.msgsQueue;
//        if (queue2 != null) {
//            queue2.clear();
//        }
        SendThread sendThread = this.mSendThread;
        if (sendThread != null) {
            sendThread.close();
        }
        ReceiveThread receiveThread = this.mReceiveThread;
        if (receiveThread != null) {
            receiveThread.close();
        }
        if (this.deviceConnection != null) {
            this.deviceConnection = null;
        }
        if (this.usbDevice != null) {
            this.usbDevice = null;
        }
        if (this.usbInterface != null) {
            this.usbInterface = null;
        }
    }
}