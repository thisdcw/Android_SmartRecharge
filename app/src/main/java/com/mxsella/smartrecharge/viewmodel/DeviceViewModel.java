package com.mxsella.smartrecharge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.common.net.BaseResponse;
import com.mxsella.smartrecharge.common.net.NetConstants;
import com.mxsella.smartrecharge.common.net.NetworkRepository;
import com.mxsella.smartrecharge.model.Device;
import com.mxsella.smartrecharge.model.response.ListResponse;

public class DeviceViewModel extends ViewModel {
    private final NetworkRepository net;

    private final MutableLiveData<ListResponse<Device>> deviceList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addDeviceSate = new MutableLiveData<>();

    public LiveData<Boolean> getAddDeviceSate() {
        return addDeviceSate;
    }

    public LiveData<ListResponse<Device>> getDeviceList() {
        return deviceList;
    }

    public String message = "";

    public DeviceViewModel() {
        net = new NetworkRepository();
    }

    public void addDevice(String productName, String deviceId) {
        net.addDevice(productName, deviceId, new ApiResponseHandler<BaseResponse<String>>() {
            @Override
            public void onSuccess(BaseResponse<String> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    addDeviceSate.setValue(true);
                } else {
                    addDeviceSate.setValue(false);
                }
            }

            @Override
            public void onError(Throwable error) {
                addDeviceSate.setValue(false);
            }
        });
    }

    public void getDeviceList(String productName, int cur, int size) {
        net.getDeviceList(cur, size, productName, new ApiResponseHandler<BaseResponse<ListResponse<Device>>>() {
            @Override
            public void onSuccess(BaseResponse<ListResponse<Device>> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    deviceList.setValue(data.getData());
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
