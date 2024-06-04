package com.mxsella.smartrecharge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseViewModel;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.model.domain.RechargeCode;
import com.mxsella.smartrecharge.model.domain.UserHistory;
import com.mxsella.smartrecharge.model.request.UseRechargeCodeRequest;
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
    protected final MutableLiveData<NetWorkResult<ListResponse<UserHistory>>> getUserTimesHistoryListResult = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<ListResponse<RechargeCode>>> getRechargeCodeListResult = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<String>> useRechargeCodeResult = new MutableLiveData<>();

    public MutableLiveData<NetWorkResult<String>> getUseRechargeCodeResult() {
        return useRechargeCodeResult;
    }

    public LiveData<NetWorkResult<ListResponse<RechargeCode>>> getGetRechargeCodeListResult() {
        return getRechargeCodeListResult;
    }

    public LiveData<NetWorkResult<ListResponse<UserHistory>>> getGetUserTimesHistoryListResult() {
        return getUserTimesHistoryListResult;
    }

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

    /**
     * 使用充值码
     *
     * @param historyId
     */
    public void useRechargeCode(String historyId) {
        String productName = getProductName();
        UseRechargeCodeRequest useRechargeCodeRequest = new UseRechargeCodeRequest(productName, historyId);
        net.useRechargeCode(useRechargeCodeRequest, createHandler(useRechargeCodeResult));
    }

    /**
     * 获取充值码列表
     *
     * @param current
     * @param size
     */
    public void getRechargeCodeList(int current, int size) {
        String productName = getProductName();
        net.getRechargeCodeList(current, size, productName, createHandler(getRechargeCodeListResult));
    }

    /**
     * 获取用户次数历史
     *
     * @param current
     * @param size
     */
    public void getUserTimesHistoryList(int current, int size) {
        String productName = getProductName();
        net.getUserTimesHistoryList(current, size, productName, createHandler(getUserTimesHistoryListResult));
    }

    /**
     * 获取申请列表
     *
     * @param cur
     * @param size
     */
    public void getChildApplyList(int cur, int size) {
        String productName = getProductName();
        net.getChildApplyList(cur, size, productName, createHandler(getChildApplyListResult));
    }

    /**
     * 获取请求列表
     *
     * @param cur
     * @param size
     */
    public void getApplyList(int cur, int size) {
        String productName = getProductName();
        net.getApplyList(cur, size, productName, createHandler(getApplyListResult));
    }

    /**
     * 处理请求
     *
     * @param applyId
     * @param pass
     */
    public void dealApply(String applyId, boolean pass) {
        String productName = Config.getProductName();
        if (productName.isEmpty()) {
            dealApplyResult.setValue(ResultUtil.error("请先设置当前管理的产品"));
        }
        net.dealApply(productName, applyId, pass, createHandler(dealApplyResult));
    }

    /**
     * 请求次数
     *
     * @param times
     */
    public void applyTimes(int times) {
        String name = Config.getProductName();
        if (name.isEmpty()) {
            applyTimesResult.setValue(ResultUtil.error("请先设置当前管理的产品"));
        }
        net.applyTimes(name, times, createHandler(applyTimesResult));
    }

    /**
     * 充值设备次数
     *
     * @param targetDeviceId
     * @param times
     */
    public void deviceRecharge(String targetDeviceId, Integer times) {
        String productName = getProductName();
        net.deviceRecharge(productName, targetDeviceId, times, createHandler(deviceRechargeResult));
    }

    /**
     * 充值用户次数
     *
     * @param targetUid
     * @param times
     */
    public void userRecharge(String targetUid, Integer times) {
        String productName = getProductName();
        net.userRecharge(productName, targetUid, times, createHandler(userRechargeResult));
    }

    /**
     * 获取用户次数
     */
    public void getUserTimes() {
        String productName = getProductName();
        net.getUserTimes(productName, createHandler(getUserTimesResult));
    }

    /**
     * 更新品牌商
     *
     * @param deviceId
     * @param targetUid
     */
    public void updateBrand(String deviceId, String targetUid) {
        String productName = getProductName();
        net.updateBrand(productName, deviceId, targetUid, createHandler(updateBrandResult));
    }

    /**
     * 添加设备
     *
     * @param deviceId
     */
    public void addDevice(String deviceId) {
        String productName = getProductName();
        net.addDevice(productName, deviceId, createHandler(addDeviceResult));
    }

    /**
     * 获取设备列表
     *
     * @param cur
     * @param size
     */
    public void getDeviceList(int cur, int size) {
        String productName = getProductName();
        net.getDeviceList(cur, size, productName, createHandler(deviceList));
    }
}
