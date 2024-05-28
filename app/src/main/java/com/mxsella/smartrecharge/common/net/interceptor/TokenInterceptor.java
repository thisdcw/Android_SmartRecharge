package com.mxsella.smartrecharge.common.net.interceptor;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.utils.LogUtil;
import com.mxsella.smartrecharge.viewmodel.UserViewModel;

import java.io.IOException;

import okhttp3.Headers;
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
        //添加头部token和uid
        builder.addHeader("X-Token", getToken());
        builder.addHeader("uid", getUid());


        Request newRequest = builder.build();
        Headers headers = newRequest.headers();
        LogUtil.d("headers: " + headers);
        return chain.proceed(newRequest);
    }


    //获取用户token
    private String getToken() {
        User user = getCurrentUser();
        if (user == null) {
            return "";
        }
        return user.getToken();
    }

    private String getUid() {
        User user = getCurrentUser();
        if (user == null) {
            return "";
        }
        return user.getUid();
    }

    public User getCurrentUser() {
        String json = Config.getCurrentUser();
        User user = new Gson().fromJson(json, User.class);
//        LogUtil.d("user -> " + user.toString());
        return user;
    }
}
