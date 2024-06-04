package com.mxsella.smartrecharge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseViewModel;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.model.response.handler.NetWorkResult;
import com.mxsella.smartrecharge.model.response.handler.ResultUtil;
import com.mxsella.smartrecharge.repository.DeviceRepository;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.Brand;
import com.mxsella.smartrecharge.model.domain.Device;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.utils.LogUtil;

public class DeviceViewModel extends BaseViewModel {
    private final DeviceRepository net;
    private final MutableLiveData<NetWorkResult<ListResponse<Device>>> deviceList = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<String>> addDeviceResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<Brand>> updateBrandResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<Integer>> getUserTimesResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<Integer>> userRechargeResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<Integer>> deviceRechargeResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<ApplyTimes>> applyTimesResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<ApplyTimes>> dealApplyResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<ListResponse<ApplyTimes>>> getApplyListResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<ListResponse<ApplyTimes>>> getChildApplyListResult = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> loadingSate = new MutableLiveData<>();

    public LiveData<Boolean> getLoadingSate() {
        return loadingSate;
    }
    public MutableLiveData<NetWorkResult<Brand>> getUpdateBrandResult() {
        return updateBrandResult;
    }

    public MutableLiveData<NetWorkResult<Integer>> getDeviceRechargeResult() {
        return deviceRechargeResult;
    }

    public LiveData<NetWorkResult<ListResponse<ApplyTimes>>> getGetChildApplyListResult() {
        return getChildApplyListResult;
    }

    public LiveData<NetWorkResult<ListResponse<ApplyTimes>>> getGetApplyListResult() {
        return getApplyListResult;
    }

    public LiveData<NetWorkResult<ApplyTimes>> getDealApplyResult() {
        return dealApplyResult;
    }

    public LiveData<NetWorkResult<ApplyTimes>> getApplyTimesResult() {
        return applyTimesResult;
    }

    public LiveData<NetWorkResult<Integer>> getUserRechargeResult() {
        return userRechargeResult;
    }

    public LiveData<NetWorkResult<Integer>> getGetUserTimesResult() {
        return getUserTimesResult;
    }

    public LiveData<NetWorkResult<String>> getAddDeviceResult() {
        return addDeviceResult;
    }

    public LiveData<NetWorkResult<ListResponse<Device>>> getDeviceList() {
        return deviceList;
    }


    public DeviceViewModel() {
        net = new DeviceRepository(new HandlerLoading() {
            @Override
            public void loading() {
                LogUtil.d("加载开始");
                loadingSate.setValue(true);
            }

            @Override
            public void complete() {
                LogUtil.d("加载完成");
                loadingSate.setValue(false);
            }
        });
    }

    public String getProductName() {
        String productName = Config.getProductName();
        return productName.isEmpty() ? null : productName;
    }

    public void getChildApplyList(int cur, int size) {
        String productName = getProductName();
        net.getChildApplyList(cur, size, productName, createHandler(getChildApplyListResult));
    }

    public void getApplyList(int cur, int size) {
        String productName = getProductName();
        net.getApplyList(cur, size, productName, createHandler(getApplyListResult));
    }

    public void dealApply(String applyId, boolean pass) {
        String productName = Config.getProductName();
        if (productName.isEmpty()) {
            dealApplyResult.setValue(ResultUtil.error("请先设置当前管理的产品"));
        }
        net.dealApply(productName, applyId, pass, createHandler(dealApplyResult));
    }

    public void applyTimes(int times) {
        String name = Config.getProductName();
        if (name.isEmpty()) {
            applyTimesResult.setValue(ResultUtil.error("请先设置当前管理的产品"));
        }
        net.applyTimes(name, times, createHandler(applyTimesResult));
    }

    public void deviceRecharge(String targetDeviceId, Integer times) {
        String productName = getProductName();
        net.deviceRecharge(productName, targetDeviceId, times, createHandler(deviceRechargeResult));
    }

    public void userRecharge(String targetUid, Integer times) {
        String productName = getProductName();
        net.userRecharge(productName, targetUid, times, createHandler(userRechargeResult));
    }

    public void getUserTimes() {
        String productName = getProductName();
        net.getUserTimes(productName, createHandler(getUserTimesResult));
    }

    public void updateBrand(String deviceId, String targetUid) {
        String productName = getProductName();
        net.updateBrand(productName, deviceId, targetUid, createHandler(updateBrandResult));
    }

    public void addDevice(String deviceId) {
        String productName = getProductName();
        net.addDevice(productName, deviceId, createHandler(addDeviceResult));
    }

    public void getDeviceList(int cur, int size) {
        String productName = getProductName();
        net.getDeviceList(cur, size, productName, createHandler(deviceList));
    }
}
