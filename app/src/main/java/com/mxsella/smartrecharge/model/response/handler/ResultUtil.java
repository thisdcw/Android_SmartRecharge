package com.mxsella.smartrecharge.model.response.handler;

import com.mxsella.smartrecharge.model.enums.ResultCode;

public class ResultUtil {

    public static <T> NetWorkResult<T> success(T data) {
        return new NetWorkResult<>(ResultCode.SUCCESS, data);
    }

    public static <T> NetWorkResult<T> success(T data, String message) {
        return new NetWorkResult<>(ResultCode.SUCCESS, data, message);
    }

    public static <T> NetWorkResult<T> failed(String message) {
        return new NetWorkResult<>(ResultCode.FAILED, message);
    }

    public static <T> NetWorkResult<T> error(String message) {
        return new NetWorkResult<>(ResultCode.ERROR, message);
    }

}
