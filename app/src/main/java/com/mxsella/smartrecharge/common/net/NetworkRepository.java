package com.mxsella.smartrecharge.common.net;

import com.mxsella.smartrecharge.model.User;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NetworkRepository {
    private final ApiService apiService;

    public NetworkRepository() {
        apiService = RetrofitClient.getInstance();
    }

    public void fetchUser(String user, ApiResponseHandler<List<BaseResponse<User>>> handler) {
        apiService.listR(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BaseResponse<User>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 可以在这里处理一些onSubscribe逻辑，比如显示加载动画
                    }

                    @Override
                    public void onNext(List<BaseResponse<User>> baseResponses) {
                        handler.onSuccess(baseResponses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        // 可以在这里处理一些onComplete逻辑，比如隐藏加载动画
                    }
                });
    }
}
