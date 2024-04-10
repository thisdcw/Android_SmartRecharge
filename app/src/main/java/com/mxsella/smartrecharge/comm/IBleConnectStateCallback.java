package com.mxsella.smartrecharge.comm;

public interface IBleConnectStateCallback {

    /**
     * 开始连接
     */
    void startConnect();

    /**
     * 连接成功
     */
    void connectSuccess();

    /**
     * 连接失败
     */
    void connectFailure(String msg);

    /**
     * 断开连接
     */
    void disConnect();
}
