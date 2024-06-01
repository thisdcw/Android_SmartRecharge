package com.mxsella.smartrecharge.repository;

import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.model.response.BaseResponse;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.model.domain.InviteRecord;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;

public class UserRepository extends NetworkRepository {

    public UserRepository(HandlerLoading handlerLoading) {
        super(handlerLoading);
    }

    public void getChildrenUser(int cur, int size, ApiResponseHandler<BaseResponse<ListResponse<User>>> handler) {
        subscribeWithHandler(apiService.getChildrenUser(cur, size), handler);
    }

    public void createInviteCode(SubNameRequestBody body, ApiResponseHandler<BaseResponse<String>> handler) {
        subscribeWithHandler(apiService.createInviteCode(body), handler);
    }

    public void getInviteCodeList(int cur, int size, ApiResponseHandler<BaseResponse<ListResponse<InviteRecord>>> handler) {
        subscribeWithHandler(apiService.getInviteHistory(cur, size), handler);
    }

    public void register(String phone, String verifyCode, String inviteCode, ApiResponseHandler<BaseResponse<User>> handler) {
        subscribeWithHandler(apiService.register(phone, verifyCode, inviteCode), handler);
    }

    public void getVerifyCode(String phone, String type, ApiResponseHandler<BaseResponse<String>> handler) {
        subscribeWithHandler(apiService.getVerifyCode(phone, type), handler);
    }

    public void loginVerify(String phone, String code, ApiResponseHandler<BaseResponse<User>> handler) {
        subscribeWithHandler(apiService.loginVerify(phone, code), handler);
    }

    public void loginPwd(String phone, String pwd, ApiResponseHandler<BaseResponse<User>> handler) {
        subscribeWithHandler(apiService.loginPwd(phone, pwd), handler);
    }

}
