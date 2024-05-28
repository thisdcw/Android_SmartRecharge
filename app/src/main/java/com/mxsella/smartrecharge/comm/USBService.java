package com.mxsella.smartrecharge.comm;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class USBService implements ICommunicateService {

    private static final String TAG = "USBService";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static USBService util;
    private static Context mContext;

    private int COMM_PRODUCT_ID;
    private int COMM_VENDOR_ID;

    private UsbDevice usbDevice; //目标USB设备
    private UsbManager usbManager;
    /**
     * 块输出端点
     */
    private UsbEndpoint epBulkOut;
    private UsbEndpoint epBulkIn;
    /**
     * 控制端点
     */
    private UsbEndpoint epControl;
    /**
     * 中断端点
     */
    private UsbEndpoint epIntEndpointOut;
    private UsbEndpoint epIntEndpointIn;

    private PendingIntent intent; //意图
    private UsbDeviceConnection conn = null;

    private OnFindListener listener;

    private int statue = USBInterface.usb_ok;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                statue = USBInterface.usb_permission_ok;
            } else {
                statue = USBInterface.usb_permission_fail;
            }*/
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            COMM_PRODUCT_ID = usbDevice.getProductId();
            COMM_VENDOR_ID = usbDevice.getVendorId();

            LogUtil.d("连接设备 -> productId: " + COMM_PRODUCT_ID + "  vendorId: " + COMM_VENDOR_ID);
            switch (intent.getAction()) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    connection();
                    break;
                case ACTION_USB_PERMISSION:
                    statue = USBInterface.usb_permission_ok;
                    break;

                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    isReading = false;
                    if (conn != null) { //关闭USB设备
                        conn.close();
                        conn = null;
                    }
                    break;
            }
        }
    };

    public static USBService getInstance(Context _context) {
        if (util == null) util = new USBService(_context);
        mContext = _context;
        return util;
    }

    private USBService(Context _context) {
        intent = PendingIntent.getBroadcast(_context, 0, new Intent(ACTION_USB_PERMISSION), 0);

    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public UsbManager getUsbManager() {
        return usbManager;
    }

    public boolean isTargetDevice(UsbDevice usbDevice) {
        return true;
    }

    /**
     * 找到自定设备
     */
    public UsbDevice getUsbDevice(int vendorId, int productId) {
        //1)创建usbManager
        if (usbManager == null)
            usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        //2)获取到所有设备 选择出满足的设备
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            Log.i(TAG, "vendorID--" + device.getVendorId() + "ProductId--" + device.getProductId());
            if (device.getVendorId() == vendorId && device.getProductId() == productId) {
                return device; // 获取USBDevice
            }
        }
        statue = USBInterface.usb_find_this_fail;
        return null;
    }

    /**
     * 查找本机所有的USB设备
     */
    public List<UsbDevice> getUsbDevices() {
        //1)创建usbManager
        if (usbManager == null)
            usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        //2)获取到所有设备 选择出满足的设备
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        //创建返回数据
        List<UsbDevice> lists = new ArrayList<>();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            Log.i(TAG, "vendorID--" + device.getVendorId() + "ProductId--" + device.getProductId());
            lists.add(device);
        }
        return lists;
    }


    /**
     * 根据指定的vendorId和productId连接USB设备
     */
    public int connection() {
        Log.e(TAG, "开始连接USB设备");
        int vendorId = COMM_VENDOR_ID;
        int productId = COMM_PRODUCT_ID;
        usbDevice = getUsbDevice(vendorId, productId);
        //3)查找设备接口
        if (usbDevice == null) {
            Log.e(TAG, "未找到目标设备，请确保供应商ID" + vendorId + "和产品ID" + productId + "是否配置正确");
            return statue;
        }
        UsbInterface usbInterface = null;
        for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
            //一个设备上面一般只有一个接口，有两个端点，分别接受和发送数据
            usbInterface = usbDevice.getInterface(i);
            break;
        }
        //4)获取usb设备的通信通道endpoint
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = usbInterface.getEndpoint(i);
            switch (ep.getType()) {
                case UsbConstants.USB_ENDPOINT_XFER_BULK://USB端口传输
                    if (UsbConstants.USB_DIR_OUT == ep.getDirection()) {//输出
                        epBulkOut = ep;
                        Log.e(TAG, "获取发送数据的端点");
                    } else {
                        epBulkIn = ep;
                        Log.e(TAG, "获取接受数据的端点");
                    }
                    break;
                case UsbConstants.USB_ENDPOINT_XFER_CONTROL://控制
                    epControl = ep;
                    Log.e(TAG, "find the ControlEndPoint:" + "index:" + i + "," + epControl.getEndpointNumber());
                    break;
                case UsbConstants.USB_ENDPOINT_XFER_INT://中断
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {//输出
                        epIntEndpointOut = ep;
                        Log.e(TAG, "find the InterruptEndpointOut:" + "index:" + i + "," + epIntEndpointOut.getEndpointNumber());
                    }
                    if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                        epIntEndpointIn = ep;
                        Log.e(TAG, "find the InterruptEndpointIn:" + "index:" + i + "," + epIntEndpointIn.getEndpointNumber());
                    }
                    break;
                default:
                    break;
            }
        }
        //5)打开conn连接通道
        if (usbManager.hasPermission(usbDevice)) {
            //有权限，那么打开
            conn = usbManager.openDevice(usbDevice);
        } else {
            usbManager.requestPermission(usbDevice, intent);
            if (usbManager.hasPermission(usbDevice)) { //权限获取成功
                conn = usbManager.openDevice(usbDevice);
            } else {
                Log.e(TAG, "没有权限");
                statue = USBInterface.usb_permission_fail;
                return statue;
            }
        }
        if (null == conn) {
            Log.e(TAG, "不能连接到设备");
            statue = USBInterface.usb_open_fail;
            return statue;
        }
        //打开设备
        if (conn.claimInterface(usbInterface, true)) {
            if (conn != null)// 到此你的android设备已经连上设备
                Log.i(TAG, "open设备成功！");
//            configUsb(115200);
            startReading();
            statue = USBInterface.usb_ok;
        } else {
            Log.i(TAG, "无法打开连接通道。");
            statue = USBInterface.usb_passway_fail;
            conn.close();
        }
        return statue;
    }

    private boolean isReading = false;

    private void startReading() {
        isReading = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isReading) {
                    synchronized (this) {
                        byte[] bytes = new byte[epBulkIn.getMaxPacketSize()];
                        //设置为0会堵塞, 10会导致丢包
                        int ret = conn.bulkTransfer(epBulkIn, bytes, bytes.length, 10);
                        if (ret > 0) {
                            if (!executors.isShutdown()) {
                                LogUtil.test("Receive - " + StringUtils.bytesToHex(bytes));
                            }
                        }
                    }

                }
            }

        }).start();
    }

    @Override
    public void send(byte[] bytes) {
        //LocalLog.test("Send - " + StringUtils.bytesToHex(bytes));
        if (conn == null || epBulkOut == null) return;
        if (conn.bulkTransfer(epBulkOut, bytes, bytes.length, 0) >= 0) {
            statue = USBInterface.usb_permission_ok;
        } else {
            statue = USBInterface.usb_permission_fail;
        }
    }

    @Override
    public void send(String str) {
        send(str.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void send(short address, short command) {
        send(Protocol.encode(address, command));
    }

    @Override
    public void imitateReceive(byte[] bytes) {
        LogUtil.test("Receive - " + StringUtils.bytesToHex(bytes));
    }

    @Override
    public boolean isConnected() {
        return conn != null;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public void startService() {
        if (conn != null)
            startReading();
    }

    @Override
    public void restartService() {

    }

    @Override
    public void stopService() {
        isReading = false;
    }

    @Override
    public void destroy() {
        isReading = false;
        if (conn != null) { //关闭USB设备
            conn.close();
            conn = null;
        }
        if (mContext != null && broadcastReceiver != null) {
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void setListener(DataReceiveListener mListener) {
        this.mListener = mListener;
    }

    private DataReceiveListener mListener;
    private ExecutorService executors = Executors.newSingleThreadExecutor();

    private ByteBuffer byteBuffer = ByteBuffer.allocate(500);

    private class ReceiveTask implements Runnable {

        private byte[] head;
        private byte[] tail;
        private int headLen, tailLen;
        private byte[] data;

        public ReceiveTask(byte[] head, byte[] data) {
            this(head, "".getBytes(), data);
        }

        public ReceiveTask(byte[] head, byte[] tail, byte[] data) {
            this.head = head;
            this.tail = tail;
            this.headLen = head.length;
            this.tailLen = tail.length;
            this.data = data;
        }

        private boolean endWith(Byte[] src, byte[] target) {
            if (target.length == 0) return false;
            if (src.length < target.length) return false;
            for (int i = 0; i < target.length; i++) {
                if (target[target.length - i - 1] != src[src.length - i - 1]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void run() {
            DataInputStream inputStream = null;
            int readLength = 0;
            try {
                if (byteBuffer.position() != 0) {
                    byteBuffer.put(data);
                    byteBuffer.flip();
                    data = new byte[byteBuffer.limit()];
                    byteBuffer.get(data);
                    byteBuffer.clear();
                }
                inputStream = new DataInputStream(new ByteArrayInputStream(data));
                int flag = -1;
                while ((flag = inputStream.read()) != -1) {
                    readLength++;
                    List<Byte> byteList = new ArrayList<>();
                    byteList.add((byte) flag);
                    int read = -1;
                    boolean hasHead = false;
                    while ((read = inputStream.read()) != -1) {
                        readLength++;
                        byteList.add((byte) read);
                        Byte[] bytes = byteList.toArray(new Byte[0]);
                        if (endWith(bytes, head)) {
                            hasHead = true;
                            break;
                        }
                    }
                    if (!hasHead) {
                        break;
                    }
                    int len = -1;
                    if ((len = inputStream.read()) != -1) {
                        if (len > 50)
                            return;
                        readLength++;
                        if (readLength + len > data.length) {
                            byteBuffer.put(Arrays.copyOfRange(data, readLength - 3, data.length));
                            break;
                        }
                        byte[] bytes = new byte[len];
                        inputStream.readFully(bytes);
                        readLength += len;

                        ReceivePacket packet = new ReceivePacket(bytes);
                        if (mListener != null)
                            mListener.onReceive(packet);
                    } else {
                        byteBuffer.put(Arrays.copyOfRange(data, data.length - 2, data.length));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 是否找到设备回调
     */
    public interface OnFindListener {
        void onFind(UsbDevice usbDevice, UsbManager usbManager);

        void onFail(String error);
    }

    /**
     * 设置波特率115200
     */
    private boolean configUsb(int paramInt) {
        byte[] arrayOfByte = new byte[8];
        conn.controlTransfer(192, 95, 0, 0, arrayOfByte, 8, 1000);
        conn.controlTransfer(64, 161, 0, 0, null, 0, 1000);
        long l1 = 1532620800 / paramInt;
        for (int i = 3; ; i--) {
            if ((l1 <= 65520L) || (i <= 0)) {
                long l2 = 65536L - l1;
                int j = (short) (int) (0xFF00 & l2 | i);
                int k = (short) (int) (0xFF & l2);
                conn.controlTransfer(64, 154, 4882, j, null, 0, 1000);
                conn.controlTransfer(64, 154, 3884, k, null, 0, 1000);
                conn.controlTransfer(192, 149, 9496, 0, arrayOfByte, 8, 1000);
                conn.controlTransfer(64, 154, 1304, 80, null, 0, 1000);
                conn.controlTransfer(64, 161, 20511, 55562, null, 0, 1000);
                conn.controlTransfer(64, 154, 4882, j, null, 0, 1000);
                conn.controlTransfer(64, 154, 3884, k, null, 0, 1000);
                conn.controlTransfer(64, 164, 0, 0, null, 0, 1000);
                return true;
            }
            l1 >>= 3;
        }
    }

}
