package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogDeviceInfoBinding;
import com.mxsella.smartrecharge.model.domain.Device;

public class DeviceInfoDialog extends BaseDialog<DialogDeviceInfoBinding> {


    private String deviceId;
    private Integer productId;
    private String brand;
    private String agent;
    private String store;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceInfoDialog(Device device) {
        this.deviceId = device.getDeviceId();
        this.productId = device.getProductId();
        this.brand = device.getBrand();
        this.agent = device.getAgent();
        this.store = device.getStore();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Override
    public void initEventAndData() {
        binding.deviceId.setText(getString(R.string.device_info_device_id, getDeviceId()));
        binding.productId.setText(getString(R.string.device_info_product_id, String.valueOf(getProductId())));
        binding.brand.setText(getString(R.string.device_info_brand, (getBrand().isEmpty() ? "暂无" : getBrand())));
        binding.agent.setText(getString(R.string.device_info_agent, (getAgent().isEmpty() ? "暂无" : getAgent())));
        binding.store.setText(getString(R.string.device_info_store, (getStore().isEmpty() ? "暂无" : getStore())));
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_device_info;
    }
}
