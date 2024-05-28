package com.mxsella.smartrecharge.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.mxsella.smartrecharge.common.Config;
import com.mxsella.smartrecharge.common.net.ApiResponseHandler;
import com.mxsella.smartrecharge.common.net.BaseResponse;
import com.mxsella.smartrecharge.common.net.NetConstants;
import com.mxsella.smartrecharge.common.net.NetworkRepository;
import com.mxsella.smartrecharge.model.InviteRecord;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;
import com.mxsella.smartrecharge.utils.LogUtil;

public class UserViewModel extends ViewModel {

    private final NetworkRepository net;

    private final MutableLiveData<Boolean> loginState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> verifyState = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<InviteRecord>> inviteRecordListResponse = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<User>> listChildrenUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> createCodeState = new MutableLiveData<>();

    public LiveData<ListResponse<User>> getListChildrenUser() {
        return listChildrenUser;
    }

    public LiveData<Boolean> getCreateCodeState() {
        return createCodeState;
    }

    public LiveData<ListResponse<InviteRecord>> getInviteRecordListResponse() {
        return inviteRecordListResponse;
    }

    public LiveData<Boolean> getRegisterState() {
        return registerState;
    }

    public LiveData<Boolean> getVerifyState() {
        return verifyState;
    }

    public LiveData<Boolean> getLoginState() {
        return loginState;
    }

    public String message = "";

    public UserViewModel() {
        net = new NetworkRepository();
    }


    public void getChildrenUser(int cur, int size) {
        net.getChildrenUser(cur, size, new ApiResponseHandler<BaseResponse<ListResponse<User>>>() {
            @Override
            public void onSuccess(BaseResponse<ListResponse<User>> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    listChildrenUser.setValue(data.getData());
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    public void createInviteCode(String subName) {
        String json = Config.getCurrentUser();
        User user = new Gson().fromJson(json, User.class);
        if (user == null) {
            createCodeState.setValue(false);
            return;
        }
        SubNameRequestBody subNameRequestBody = new SubNameRequestBody(subName);
        net.createInviteCode(subNameRequestBody, new ApiResponseHandler<BaseResponse<String>>() {
            @Override
            public void onSuccess(BaseResponse<String> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    createCodeState.setValue(true);
                } else {
                    createCodeState.setValue(false);
                }
            }

            @Override
            public void onError(Throwable error) {
                createCodeState.setValue(false);
            }
        });
    }

    public void getInviteCodeList(int cur, int size) {
        net.getInviteCodeList(cur, size, new ApiResponseHandler<BaseResponse<ListResponse<InviteRecord>>>() {
            @Override
            public void onSuccess(BaseResponse<ListResponse<InviteRecord>> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    LogUtil.d("data: " + data.getData());
                    inviteRecordListResponse.setValue(data.getData());
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    public void register(String telephone, String verifyCode, String inviteCode) {
        net.register(telephone, verifyCode, inviteCode, new ApiResponseHandler<BaseResponse<User>>() {
            @Override
            public void onSuccess(BaseResponse<User> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    registerState.setValue(true);
                } else {
                    registerState.setValue(false);
                }
                message = data.getMessage();
            }

            @Override
            public void onError(Throwable error) {
                registerState.setValue(false);
                message = error.getMessage();
            }
        });
    }

    public void getVerifyCode(String phone, String type) {
        net.getVerifyCode(phone, type, new ApiResponseHandler<BaseResponse<String>>() {
            @Override
            public void onSuccess(BaseResponse<String> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    verifyState.setValue(true);
                } else {
                    verifyState.setValue(false);
                }
            }

            @Override
            public void onError(Throwable error) {
                verifyState.setValue(false);
            }
        });
    }

    public void loginWithPwd(String telephone, String password) {
        net.loginPwd(telephone, password, new ApiResponseHandler<BaseResponse<User>>() {
            @Override
            public void onSuccess(BaseResponse<User> data) {
                LogUtil.d("登录成功" + data.getData().toString());
                if (data.getCode() == NetConstants.SUCCESS) {
                    loginState.setValue(true);
                    //保存登录信息
                    Config.saveUser(data.getData());
                } else {
                    loginState.setValue(false);
                }
                message = data.getMessage();
            }

            @Override
            public void onError(Throwable error) {
                LogUtil.d("登录失败");
                loginState.setValue(false);
                message = error.getMessage();
            }
        });
    }

    public void loginWhitCode(String telephone, String code) {
        net.loginVerify(telephone, code, new ApiResponseHandler<BaseResponse<User>>() {
            @Override
            public void onSuccess(BaseResponse<User> data) {
                if (data.getCode() == NetConstants.SUCCESS) {
                    loginState.setValue(true);
                } else {
                    loginState.setValue(false);
                }
                message = data.getMessage();
            }

            @Override
            public void onError(Throwable error) {
                loginState.setValue(false);
                message = error.getMessage();
            }
        });
    }
}

