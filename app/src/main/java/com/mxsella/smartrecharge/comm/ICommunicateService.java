package com.mxsella.smartrecharge.comm;

public interface ICommunicateService {

    void send(byte[] bytes);
    void send(String str);
    void send(short address, short command);
    void imitateReceive(byte[] bytes);

    boolean isConnected();
    boolean isEnable();
    void startService();
    void restartService();
    void stopService();

    void destroy();

    void setListener(DataReceiveListener mListener);

    interface DataReceiveListener {
        void onReceive(ReceivePacket packet);
    }
}
