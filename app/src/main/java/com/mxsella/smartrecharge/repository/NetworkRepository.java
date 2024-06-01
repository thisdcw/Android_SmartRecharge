package com.mxsella.smartrecharge.repository;

import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.common.net.RetrofitClient;
import com.mxsella.smartrecharge.common.net.service.ApiService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 网络请求仓库
 */
public class NetworkRepository {
    public final ApiService apiService;

    public NetworkRepository(HandlerLoading handlerLoading) {
        apiService = RetrofitClient.getInstance();
        this.handlerLoading = handlerLoading;
    }

    public HandlerLoading handlerLoading;

    protected <T> void subscribeWithHandler(Observable<T> observable, ApiResponseHandler<T> handler) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        handlerLoading.loading();
                    }

                    @Override
                    public void onNext(@NonNull T response) {
                        handler.onSuccess(response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                        handlerLoading.complete();
                    }

                    @Override
                    public void onComplete() {
                        handlerLoading.complete();
                    }
                });
    }
}
