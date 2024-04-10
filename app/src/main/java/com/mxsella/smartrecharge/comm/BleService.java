package com.mxsella.smartrecharge.comm;

import android.app.Application;
import android.bluetooth.BluetoothGatt;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.mxsella.smartrecharge.comm.executor.SendSerialExecutor;
import com.mxsella.smartrecharge.comm.executor.SerialExecutor;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BleService implements ICommunicateService {

    private static final String TAG = "BleService";

    private final BleManager manager = BleManager.getInstance();
    public static final String SERVICE_UUID_STR = "0000FFE0-0000-1000-8000-00805F9B34FB";
    public static final String READ_CHARACTERIZE_STR = "0000FFE2-0000-1000-8000-00805F9B34FB";
    public static final String WRITE_CHARACTERIZE_STR = "0000FFE1-0000-1000-8000-00805F9B34FB";
    public static final UUID SERVICE_UUID = UUID.fromString(SERVICE_UUID_STR);
    public static final UUID READ_UUID = UUID.fromString(READ_CHARACTERIZE_STR);
    public static final UUID WRITE_UUID = UUID.fromString(WRITE_CHARACTERIZE_STR);
    private BleDevice curDevice;
    private List<DataReceiveListener> listenerList = new ArrayList<>();
    private ExecutorService executors = Executors.newSingleThreadExecutor();
    private ExecutorService sendExecutors = Executors.newSingleThreadExecutor();
    private ExecutorService handShakeExecutors = Executors.newSingleThreadExecutor();
    private SerialExecutor serialExecutor = new SerialExecutor(executors);
    private SendSerialExecutor sendSerialExecutor = new SendSerialExecutor(sendExecutors);
    private String curMac;
    private IBleConnectStateCallback connectStateCallback;
    private boolean ifHandShake = false;

    private final BleNotifyCallback notifyCallback = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess() {
        }

        @Override
        public void onNotifyFailure(BleException exception) {
        }

        @Override
        public void onCharacteristicChanged(byte[] bytes) {
            LogUtil.test("Receive => " + StringUtils.bytesToHex(bytes));
            serialExecutor.execute(new ReceiveTask(Protocol.int2Bytes(Protocol.HEAD), bytes));
        }
    };

    //模拟蓝牙数据
    public void imitateReceive(byte[] bytes) {
        LogUtil.test("Imitate => " + StringUtils.bytesToHex(bytes));
        serialExecutor.execute(new ReceiveTask(Protocol.int2Bytes(Protocol.HEAD), bytes));
    }

    private final BleGattCallback connectCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            if (connectStateCallback != null) {
                connectStateCallback.startConnect();
            }
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            if (connectStateCallback != null) {
                connectStateCallback.connectFailure(exception.getDescription());
            }
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            BleService.getInstance().setCurDevice(bleDevice);
            startService();

//            handShakeExecutors.execute(handShakeTimer);

            if (connectStateCallback != null) {
                connectStateCallback.connectSuccess();
            }
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            setIfHandShake(false);
            BleService.getInstance().setCurDevice(null);

            if (connectStateCallback != null) {
                connectStateCallback.disConnect();
            }
        }
    };

    public static BleService getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final BleService INSTANCE = new BleService();
    }

    private BleService() {
    }

    public void init(Application application) {
        manager.init(application);
        manager.enableLog(true)
                .setReConnectCount(2, 5000)
                .setOperateTimeout(5000);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(new UUID[]{SERVICE_UUID})
//                .setAutoConnect(true) // 是否自动连接
                .setScanTimeOut(3000)
                .build();
        manager.initScanRule(scanRuleConfig);
    }

    public void setConnectStateCallback(IBleConnectStateCallback connectStateCallback) {
        this.connectStateCallback = connectStateCallback;
    }

    public void setCurDevice(BleDevice curDevice) {
        this.curDevice = curDevice;
        if (curDevice != null) {
            this.curMac = curDevice.getMac();
        }
    }

    public BleDevice getCurDevice() {
        return curDevice;
    }

    @Override
    public void send(byte[] bytes) {
        sendSerialExecutor.execute(() -> {
            if (bytes != null && Config.isDebug)
                LogUtil.d("Send" + StringUtils.bytesToHex(bytes));
            if (curDevice != null && bytes != null) {
                LogUtil.d("Send" + StringUtils.bytesToHex(bytes));
                manager.write(curDevice, SERVICE_UUID_STR, WRITE_CHARACTERIZE_STR, bytes, new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        LogUtil.d("写入成功");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        LogUtil.e("写入失败");
                    }
                });
            }
        });
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
    public boolean isConnected() {
        return isEnable() && curDevice != null && BleManager.getInstance().isConnected(curDevice);
    }

    public void connect(String mac, IBleConnectStateCallback callback) {
        if (mac == null) {
            return;
        }
        if (isConnected()) {
            return;
        }
        this.connectStateCallback = callback;
        BleManager.getInstance().connect(mac, connectCallback);
    }

    @Override
    public boolean isEnable() {
        return manager.isBlueEnable();
    }

    @Override
    public void startService() {
        if (isConnected()) {
            LogUtil.d("开启服务");
            BleManager.getInstance().notify(curDevice, SERVICE_UUID_STR, READ_CHARACTERIZE_STR, notifyCallback);
        }
    }

    @Override
    public void restartService() {
    }

    @Override
    public void stopService() {
        if (curDevice != null) {
            BleManager.getInstance().removeNotifyCallback(curDevice, READ_CHARACTERIZE_STR);
        }
    }

    @Override
    public void destroy() {
        manager.disconnectAllDevice();
        manager.destroy();
    }

    @Override
    public void setListener(DataReceiveListener mListener) {
        addListener(mListener);
    }

    private ByteBuffer byteBuffer = ByteBuffer.allocate(200);

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
            LogUtil.test("收到 => "+ Arrays.toString(data));
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
                    while ((read = inputStream.read()) != -1) {
                        readLength++;
                        byteList.add((byte) read);
                        Byte[] bytes = byteList.toArray(new Byte[0]);
                        if (endWith(bytes, head)) break;
                    }
                    int address;
                    if ((address = inputStream.read()) != -1) {
                        readLength++;
                        int len;
                        if ((len = inputStream.read()) != -1) {
                            readLength++;
                            if (readLength + len + 1 > data.length) {
                                byteBuffer.put(Arrays.copyOfRange(data, readLength - 2, data.length));
                                break;
                            }
                            byte[] bytes = new byte[2 + len + 1];
                            bytes[0] = (byte) address;
                            bytes[1] = (byte) len;
                            inputStream.readFully(bytes, 2, len + 1);
                            readLength += (len + 1);
                            int sum = address + len;
                            int key = Config.getSecretKey();
                            for (int i = 2; i < bytes.length - 1; i++) {
                                if (bytes[0] == (byte) Protocol.ADDR_INFO) {
                                    sum = bytes[i] + key + sum;
                                    bytes[i] += key;
                                } else if (bytes[0] == (byte) Protocol.ADDR_PAY) {
                                    sum = bytes[i] + key + sum;
                                    bytes[i] += key;
                                } else {
                                    sum += bytes[i];
                                }
                            }
                            if ((byte) sum == bytes[bytes.length - 1]) {
                                ReceivePacket packet = new ReceivePacket(bytes);
                                for (int i = listenerList.size() - 1; i >= 0; i--) {
                                    if (listenerList.get(i) != null) {
                                        listenerList.get(i).onReceive(packet);
                                    }
                                }
                            }
                        }
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

    public void setIfHandShake(boolean ifHandShake) {
        this.ifHandShake = ifHandShake;
    }

    private Runnable handShakeTimer = () -> {
        while (isConnected()) {
            try {
                Thread.sleep(1000);
            } catch (Exception ignore) {

            }
            //握手
//            if (ifHandShake)
//                send(Protocol.ADDR_HAND_SHAKE, Protocol.COMMAND_NULL);
        }
    };

    public void disconnect() {
        if (curDevice != null) {
            BleManager.getInstance().disconnect(curDevice);
        }
    }
    public void reconnect() {
        if (curDevice != null) {
            BleManager.getInstance().connect(curDevice.getMac(), connectCallback);
        }
    }

    public void addListener(DataReceiveListener mListener) {
        this.listenerList.add(mListener);
    }

    public void removeListener(DataReceiveListener mListener) {
        this.listenerList.remove(mListener);
    }
}
