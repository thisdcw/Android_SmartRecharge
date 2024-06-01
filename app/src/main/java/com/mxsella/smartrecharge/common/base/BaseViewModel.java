package com.mxsella.smartrecharge.common.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.BaseResponse;
import com.mxsella.smartrecharge.model.response.handler.NetWorkResult;
import com.mxsella.smartrecharge.model.response.handler.ResultUtil;

public class BaseViewModel extends ViewModel {

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
                liveData.setValue(ResultUtil.error(error.getMessage()));
            }
        };
    }
}
