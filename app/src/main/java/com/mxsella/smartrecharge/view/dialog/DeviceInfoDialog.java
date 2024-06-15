package com.mxsella.smartrecharge.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxsella.smartrecharge.R;
import com.mxsella.smartrecharge.common.base.BaseDialog;
import com.mxsella.smartrecharge.databinding.DialogDeviceInfoBinding;
import com.mxsella.smartrecharge.inter.DialogClickListener;
import com.mxsella.smartrecharge.model.domain.Device;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.UserEnum;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

import org.apache.commons.lang3.StringUtils;

public class DeviceInfoDialog extends BaseDialog<DialogDeviceInfoBinding> {


    private String deviceId;
    private Integer productId;
    private String brand;
    private String agent;
    private String store;

    private final UserViewModel userViewModel = new UserViewModel();

    private DialogClickListener unbindListener;

    public DialogClickListener getUnbindListener() {
        return unbindListener;
    }

    public void setUnbindListener(DialogClickListener unbindListener) {
        this.unbindListener = unbindListener;
    }

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
        this.brand = device.getBrandName();
        this.agent = device.getAgentName();
        this.store = device.getStoreName();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(context, R.style.MyDialog);
    }

    @Override
    public void initEventAndData() {

        binding.unbind.setOnClickListener(v -> {
            if (unbindListener != null) {
                unbindListener.onConfirm();
            }
            dismiss();
        });
        initUI();
    }

    private void initUI() {
        binding.deviceId.setText(getString(R.string.device_info_device_id, getDeviceId()));
        binding.productId.setText(getString(R.string.device_info_product_id, String.valueOf(getProductId())));

        if (!StringUtils.isAnyBlank(getBrand())) {
            binding.unbind.setVisibility(View.VISIBLE);
            binding.brand.setVisibility(View.VISIBLE);
            binding.brand.setText(getString(R.string.device_info_brand, getBrand()));
        }
        if (!StringUtils.isAnyBlank(getAgent())) {
            binding.agent.setVisibility(View.VISIBLE);
            binding.agent.setText(getString(R.string.device_info_agent, getAgent()));
        }
        if (!StringUtils.isAnyBlank(getStore())) {
            binding.store.setVisibility(View.VISIBLE);
            binding.store.setText(getString(R.string.device_info_store, getStore()));
        }
        User user = userViewModel.getCurrentUser();
        if (user.getRole().equals(UserEnum.STORE.getRole())){
            binding.unbind.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_device_info;
    }
}
