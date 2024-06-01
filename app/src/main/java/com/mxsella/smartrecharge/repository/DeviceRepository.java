package com.mxsella.smartrecharge.repository;

import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.model.response.BaseResponse;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.Brand;
import com.mxsella.smartrecharge.model.domain.Device;
import com.mxsella.smartrecharge.model.request.ApplyTimesRequestBody;
import com.mxsella.smartrecharge.model.request.DealApplyRequestBody;
import com.mxsella.smartrecharge.model.request.DeviceRechargeRequestBody;
import com.mxsella.smartrecharge.model.request.UpdateDeviceBrandRequestBody;
import com.mxsella.smartrecharge.model.request.UserRechargeRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;

public class DeviceRepository extends NetworkRepository {

    public DeviceRepository(HandlerLoading handlerLoading) {
        super(handlerLoading);
    }

    public void getChildApplyList(int cur, int size, String productName, ApiResponseHandler<BaseResponse<ListResponse<ApplyTimes>>> handler) {
        subscribeWithHandler(apiService.getChildApplyList(cur, size, productName), handler);
    }

    public void getApplyList(int cur, int size, String productName, ApiResponseHandler<BaseResponse<ListResponse<ApplyTimes>>> handler) {
        subscribeWithHandler(apiService.getApplyList(cur, size, productName), handler);
    }

    public void dealApply(String productName, String applyId, boolean pass, ApiResponseHandler<BaseResponse<ApplyTimes>> handler) {
        DealApplyRequestBody dealApplyRequestBody = new DealApplyRequestBody(productName, applyId, pass);
        subscribeWithHandler(apiService.dealApply(dealApplyRequestBody), handler);
    }

    public void applyTimes(String productName, int times, ApiResponseHandler<BaseResponse<ApplyTimes>> handler) {
        ApplyTimesRequestBody applyTimesRequestBody = new ApplyTimesRequestBody(productName, times);
        subscribeWithHandler(apiService.applyTimes(applyTimesRequestBody), handler);
    }

    public void deviceRecharge(String productName, String targetDeviceId, Integer times, ApiResponseHandler<BaseResponse<Integer>> handler) {
        DeviceRechargeRequestBody deviceRechargeRequestBody = new DeviceRechargeRequestBody(productName, targetDeviceId, times);
        subscribeWithHandler(apiService.deviceRecharge(deviceRechargeRequestBody), handler);
    }

    public void userRecharge(String productName, String targetUid, Integer times, ApiResponseHandler<BaseResponse<Integer>> handler) {
        UserRechargeRequestBody userRechargeRequestBody = new UserRechargeRequestBody(productName, targetUid, times);
        subscribeWithHandler(apiService.userRecharge(userRechargeRequestBody), handler);
    }

    public void getUserTimes(String productName, ApiResponseHandler<BaseResponse<Integer>> handler) {
        subscribeWithHandler(apiService.getUserTimes(productName), handler);
    }

    public void updateBrand(String productName, String deviceId, String targetUid, ApiResponseHandler<BaseResponse<Brand>> handler) {
        UpdateDeviceBrandRequestBody updateDeviceBrandRequestBody = new UpdateDeviceBrandRequestBody(productName, deviceId, targetUid);
        subscribeWithHandler(apiService.updateBrand(updateDeviceBrandRequestBody), handler);
    }

    public void addDevice(String productName, String deviceId, ApiResponseHandler<BaseResponse<String>> handler) {
        subscribeWithHandler(apiService.addDevice(productName, deviceId), handler);
    }

    public void getDeviceList(int cur, int size, String productName, ApiResponseHandler<BaseResponse<ListResponse<Device>>> handler) {
        subscribeWithHandler(apiService.getDeviceList(cur, size, productName), handler);
    }
}