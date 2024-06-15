package com.mxsella.smartrecharge.common.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.BaseResponse;
import com.mxsella.smartrecharge.model.response.handler.NetWorkResult;
import com.mxsella.smartrecharge.model.response.handler.ResultUtil;
import com.mxsella.smartrecharge.ui.activity.LoginActivity;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.utils.ToastUtils;

public class BaseViewModel extends ViewModel {
    public String getProductName() {
        String productName = Config.getProductName();
        if (productName.isEmpty()) {
            ToastUtils.showToast("请先设置当前管理产品");
            return "";
        }
        return productName;
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public User getCurrentUser() {
        String json = Config.getCurrentUser();
        return new Gson().fromJson(json, User.class);
    }

    protected <T> ApiResponseHandler<BaseResponse<T>> createHandler(MutableLiveData<NetWorkResult<T>> liveData) {
        return new ApiResponseHandler<BaseResponse<T>>() {
            @Override
            public void onSuccess(BaseResponse<T> data) {
                if (data.getCode() == ResultCode.SUCCESS.getCode()) {
                    liveData.setValue(ResultUtil.success(data.getData(), data.getMessage()));
                } else {
                    liveData.setValue(ResultUtil.failed(data.getMessage()));
                }
            }

            @Override
            public void onError(Throwable error) {
                LogUtil.e(error.getMessage());
                liveData.setValue(ResultUtil.error(error.getMessage()));
            }
        };
    }
}
