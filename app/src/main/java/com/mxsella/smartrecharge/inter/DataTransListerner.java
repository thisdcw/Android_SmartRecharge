package com.mxsella.smartrecharge.inter;


public interface DataTransListerner {
    public enum ProtocolType {
        OTG,
        BLE
    }

    void onCmdMessage(int i, byte[] bArr, int i2, ProtocolType protocolType);

    void onImageData(byte[] bArr, ProtocolType protocolType);

}
