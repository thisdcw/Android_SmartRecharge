package com.mxsella.smartrecharge.common.manager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.mxsella.smartrecharge.model.enums.ResultCode;
import com.mxsella.smartrecharge.model.response.handler.NetWorkResult;

import java.util.function.Consumer;


public class ObserverManager {
    private final LifecycleOwner lifecycleOwner;

    public ObserverManager(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    public <T> void observeResult(LiveData<NetWorkResult<T>> liveData, Consumer<T> onSuccess, Consumer<String> onFailure) {
        liveData.observe(lifecycleOwner, result -> {
            if (result.getResultCode() == ResultCode.SUCCESS) {
                onSuccess.accept(result.getData());
            } else {
                onFailure.accept(result.getMessage());
            }
        });
    }
}
