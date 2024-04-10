package com.mxsella.smartrecharge.entity;

public class BleDeviceInfo {
    private String deviceName;
    private String deviceAddress;
    private boolean connectState = false;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public boolean getConnectState() {
        return connectState;
    }

    public void setConnectState(boolean connectState) {
        this.connectState = connectState;
    }

    @Override
    public String toString() {
        return "BleDeviceInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceAddress='" + deviceAddress + '\'' +
                ", connectState=" + connectState +
                '}';
    }

    public static BleDeviceInfo parse(String input) {
        BleDeviceInfo bleDeviceInfo = new BleDeviceInfo();
        String[] parts = input.split(",");

        for (String part : parts) {
            String[] keyValue = part.split("=");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case "deviceName":
                    bleDeviceInfo.setDeviceName(value);
                    break;
                case "deviceAddress":
                    bleDeviceInfo.setDeviceAddress(value);
                    break;
                case "connectState":
                    bleDeviceInfo.setConnectState(Boolean.parseBoolean(value));
                    break;
                default:
                    // Handle unknown keys or other cases
                    break;
            }
        }

        return bleDeviceInfo;
    }
}
