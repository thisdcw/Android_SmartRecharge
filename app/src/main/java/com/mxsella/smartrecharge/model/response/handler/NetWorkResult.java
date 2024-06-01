package com.mxsella.smartrecharge.model.response.handler;

import com.mxsella.smartrecharge.model.enums.ResultCode;

public class NetWorkResult<T> {

    private ResultCode resultCode;

    private T data;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NetWorkResult(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.data = null;
        this.message = resultCode.getMessage();
    }

    public NetWorkResult(ResultCode resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public NetWorkResult(ResultCode resultCode, T data, String message) {
        this.resultCode = resultCode;
        this.data = data;
        this.message = message;
    }

    public NetWorkResult(ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.data = null;
        this.message = message;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
