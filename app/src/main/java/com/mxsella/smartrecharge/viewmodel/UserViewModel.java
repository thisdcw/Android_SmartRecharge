package com.mxsella.smartrecharge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.base.BaseViewModel;
import com.mxsella.smartrecharge.common.net.HandlerLoading;
import com.mxsella.smartrecharge.model.domain.InviteRecord;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.request.ModifySubRequestBody;
import com.mxsella.smartrecharge.model.request.ModifyUserRequestBody;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.model.response.handler.NetWorkResult;
import com.mxsella.smartrecharge.model.response.handler.ResultUtil;
import com.mxsella.smartrecharge.repository.UserRepository;
import com.mxsella.smartrecharge.utils.MD5Utils;

public class UserViewModel extends BaseViewModel {

    private final UserRepository net;
    private final MutableLiveData<NetWorkResult<User>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<User>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<String>> verifyResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<ListResponse<InviteRecord>>> inviteCodeList = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<String>> createCodeResult = new MutableLiveData<>();
    private final MutableLiveData<NetWorkResult<ListResponse<User>>> listChildUserResult = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> loadingSate = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<String>> verifyChangePwd = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<String>> changePwd = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<User>> changeUserInfo = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<User>> changeSubInfo = new MutableLiveData<>();
    protected final MutableLiveData<NetWorkResult<User>> userInfoResult = new MutableLiveData<>();

    public LiveData<NetWorkResult<User>> getUserInfoResult() {
        return userInfoResult;
    }

    public LiveData<NetWorkResult<User>> getChangeSubInfo() {
        return changeSubInfo;
    }

    public MutableLiveData<NetWorkResult<User>> getChangeUserInfo() {
        return changeUserInfo;
    }

    public LiveData<NetWorkResult<String>> getChangePwd() {
        return changePwd;
    }

    public LiveData<NetWorkResult<String>> getVerifyChangePwd() {
        return verifyChangePwd;
    }

    public LiveData<Boolean> getLoadingSate() {
        return loadingSate;
    }

    public LiveData<NetWorkResult<ListResponse<User>>> getListChildUserResult() {
        return listChildUserResult;
    }

    public LiveData<NetWorkResult<String>> getCreateCodeResult() {
        return createCodeResult;
    }

    public LiveData<NetWorkResult<ListResponse<InviteRecord>>> getInviteCodeList() {
        return inviteCodeList;
    }

    public LiveData<NetWorkResult<User>> getRegisterResult() {
        return registerResult;
    }

    public LiveData<NetWorkResult<String>> getVerifyResult() {
        return verifyResult;
    }

    public LiveData<NetWorkResult<User>> getLoginResult() {
        return loginResult;
    }


    public UserViewModel() {
        net = new UserRepository(new HandlerLoading() {
            @Override
            public void loading() {
                loadingSate.setValue(true);
            }

            @Override
            public void complete() {
                loadingSate.setValue(false);
            }
        });
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public User getCurrentUser() {
        String json = Config.getCurrentUser();
        return new Gson().fromJson(json, User.class);
    }

    public void getUserInfo() {
        net.getUserInfo(createHandler(userInfoResult));
    }

    public void changeSub(String subUid, String subName) {
        ModifySubRequestBody subRequestBody = new ModifySubRequestBody(subUid, subName);
        net.changeSub(subRequestBody, createHandler(changeSubInfo));
    }

    /**
     * 修改用户信息
     *
     * @param username
     * @param avatar
     */
    public void changeUserinfo(String username, String avatar) {
        ModifyUserRequestBody modifyUserRequestBody = new ModifyUserRequestBody(username, avatar);
        net.changeUserinfo(modifyUserRequestBody, createHandler(changeUserInfo));
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     */
    public void changePassword(String oldPassword, String newPassword) {
        net.changePassword(MD5Utils.md5(oldPassword), MD5Utils.md5(newPassword), createHandler(changePwd));
    }

    /**
     * 验证码修改密码
     *
     * @param telephone
     * @param verify
     * @param password
     */
    public void changePasswordWithCode(String telephone, String verify, String password) {
        net.changePasswordWithCode(telephone, verify, MD5Utils.md5(password), createHandler(verifyChangePwd));
    }

    /**
     * 获取子级用户列表
     *
     * @param cur
     * @param size
     */
    public void getChildUser(int cur, int size) {
        net.getChildrenUser(cur, size, createHandler(listChildUserResult));
    }

    /**
     * 创建验证码
     *
     * @param subName
     */
    public void createInviteCode(String subName) {
        String json = Config.getCurrentUser();
        User user = new Gson().fromJson(json, User.class);
        if (user == null) {
            createCodeResult.setValue(ResultUtil.error("空用户"));
            return;
        }
        SubNameRequestBody subNameRequestBody = new SubNameRequestBody(subName);
        net.createInviteCode(subNameRequestBody, createHandler(createCodeResult));
    }

    /**
     * 获取邀请码列表
     *
     * @param cur
     * @param size
     */
    public void getInviteCodeList(int cur, int size) {
        net.getInviteCodeList(cur, size, createHandler(inviteCodeList));
    }

    /**
     * 注册
     *
     * @param telephone
     * @param verifyCode
     * @param inviteCode
     */
    public void register(String telephone, String verifyCode, String inviteCode) {
        net.register(telephone, verifyCode, inviteCode, createHandler(registerResult));
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param type
     */
    public void getVerifyCode(String phone, String type) {
        net.getVerifyCode(phone, type, createHandler(verifyResult));
    }

    /**
     * 密码登录
     *
     * @param telephone
     * @param password
     */
    public void loginWithPwd(String telephone, String password) {
        net.loginPwd(telephone, MD5Utils.md5(password), createHandler(loginResult));
    }

    /**
     * 验证码登录
     *
     * @param telephone
     * @param code
     */
    public void loginWhitCode(String telephone, String code) {
        net.loginVerify(telephone, code, createHandler(loginResult));
    }
}

