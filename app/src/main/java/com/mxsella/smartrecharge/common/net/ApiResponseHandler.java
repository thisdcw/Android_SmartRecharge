package com.mxsella.smartrecharge.common.net;

public interface ApiResponseHandler<T> {
    void onSuccess(T data);

    void onError(Throwable error);
}
