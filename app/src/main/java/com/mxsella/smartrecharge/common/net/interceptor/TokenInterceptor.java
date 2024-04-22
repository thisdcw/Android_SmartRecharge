package com.mxsella.smartrecharge.common.net.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Content-Type", "application/json");
        //添加头部token
//        builder.addHeader("Token", getToken());
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }


    //获取用户token
    private String getToken() {
        return "";
    }
}
