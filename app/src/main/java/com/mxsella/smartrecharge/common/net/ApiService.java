package com.mxsella.smartrecharge.common.net;

import com.mxsella.smartrecharge.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    String BASE_URL = "http://mmk.mxsella.com/Mxsella/api/v1/";

    @GET("users/{user}/repos")
    Observable<List<BaseResponse<User>>> listR(@Path("user") String user);

}
