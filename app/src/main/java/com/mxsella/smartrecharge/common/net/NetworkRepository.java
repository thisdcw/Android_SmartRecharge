package com.mxsella.smartrecharge.common.net;

import com.mxsella.smartrecharge.common.net.service.ApiService;
import com.mxsella.smartrecharge.model.Device;
import com.mxsella.smartrecharge.model.InviteRecord;
import com.mxsella.smartrecharge.model.TestUser;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 网络请求仓库
 */
public class NetworkRepository {
    private final ApiService apiService;

    public NetworkRepository() {
        apiService = RetrofitClient.getInstance();
    }

    public void fetchUser(String user, ApiResponseHandler<BaseResponse<TestUser>> handler) {
        apiService.listR(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<TestUser>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 可以在这里处理一些onSubscribe逻辑，比如显示加载动画
                    }

                    @Override
                    public void onNext(BaseResponse<TestUser> baseResponses) {
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

    public void addDevice(String productName, String deviceId, ApiResponseHandler<BaseResponse<String>> handler) {
        apiService.addDevice(productName, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<String> stringBaseResponse) {
                        handler.onSuccess(stringBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getDeviceList(int cur, int size, String productName, ApiResponseHandler<BaseResponse<ListResponse<Device>>> handler) {
        apiService.getDeviceList(cur, size, productName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ListResponse<Device>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<ListResponse<Device>> listResponseBaseResponse) {
                        handler.onSuccess(listResponseBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getChildrenUser(int cur, int size, ApiResponseHandler<BaseResponse<ListResponse<User>>> handler) {
        apiService.getChildrenUser(cur, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ListResponse<User>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<ListResponse<User>> listResponseBaseResponse) {
                        handler.onSuccess(listResponseBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void createInviteCode(SubNameRequestBody body, ApiResponseHandler<BaseResponse<String>> handler) {
        apiService.createInviteCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<String> stringBaseResponse) {
                        handler.onSuccess(stringBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getInviteCodeList(int cur, int size, ApiResponseHandler<BaseResponse<ListResponse<InviteRecord>>> handler) {
        apiService.getInviteHistory(cur, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ListResponse<InviteRecord>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<ListResponse<InviteRecord>> listBaseResponse) {
                        handler.onSuccess(listBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void register(String phone, String verifyCode, String inviteCode, ApiResponseHandler<BaseResponse<User>> handler) {
        apiService.register(phone, verifyCode, inviteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<User> stringBaseResponse) {
                        handler.onSuccess(stringBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getVerifyCode(String phone, String type, ApiResponseHandler<BaseResponse<String>> handler) {
        apiService.getVerifyCode(phone, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<String> stringBaseResponse) {
                        handler.onSuccess(stringBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loginVerify(String phone, String code, ApiResponseHandler<BaseResponse<User>> handler) {
        apiService.loginVerify(phone, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<User> userBaseResponse) {
                        handler.onSuccess(userBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loginPwd(String phone, String pwd, ApiResponseHandler<BaseResponse<User>> handler) {
        apiService.loginPwd(phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BaseResponse<User> userBaseResponse) {
                        handler.onSuccess(userBaseResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handler.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
