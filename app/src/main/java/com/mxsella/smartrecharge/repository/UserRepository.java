package com.mxsella.smartrecharge.repository;

import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.model.domain.ChildUser;
import com.mxsella.smartrecharge.model.request.ModifySubRequestBody;
import com.mxsella.smartrecharge.model.request.ModifyUserRequestBody;
import com.mxsella.smartrecharge.model.response.BaseResponse;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.model.domain.InviteRecord;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;

import retrofit2.http.Body;
import retrofit2.http.Query;

public class UserRepository extends NetworkRepository {

    public UserRepository(HandlerLoading handlerLoading) {
        super(handlerLoading);
    }

    public void getUserInfo(ApiResponseHandler<BaseResponse<User>> handler) {
        subscribeWithHandler(apiService.getUserInfo(), handler);
    }

    public void changeSub(ModifySubRequestBody userRequestBody, ApiResponseHandler<BaseResponse<ChildUser>> handler) {
        subscribeWithHandler(apiService.changeSub(userRequestBody), handler);
    }

    public void changeUserinfo(ModifyUserRequestBody userRequestBody, ApiResponseHandler<BaseResponse<User>> handler) {
        subscribeWithHandler(apiService.changeUserinfo(userRequestBody), handler);
    }

    public void changePassword(String oldPassword, String newPassword, ApiResponseHandler<BaseResponse<String>> handler) {
        subscribeWithHandler(apiService.changePassword(oldPassword, newPassword), handler);
    }

    public void changePasswordWithCode(String telephone, String verify, String password, ApiResponseHandler<BaseResponse<String>> handler) {
        subscribeWithHandler(apiService.changePasswordWithCode(telephone, verify, password), handler);
    }

    public void getChildrenUser(int cur, int size, String productName, ApiResponseHandler<BaseResponse<ListResponse<ChildUser>>> handler) {
        subscribeWithHandler(apiService.getChildrenUser(cur, size, productName), handler);
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
