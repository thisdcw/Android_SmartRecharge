package com.mxsella.smartrecharge.common.net.service;

import com.mxsella.smartrecharge.model.domain.RechargeCode;
import com.mxsella.smartrecharge.model.domain.UserHistory;
import com.mxsella.smartrecharge.model.request.ModifyUserRequestBody;
import com.mxsella.smartrecharge.model.request.UseRechargeCodeRequest;
import com.mxsella.smartrecharge.model.response.BaseResponse;
import com.mxsella.smartrecharge.model.domain.ApplyTimes;
import com.mxsella.smartrecharge.model.domain.Brand;
import com.mxsella.smartrecharge.model.domain.Device;
import com.mxsella.smartrecharge.model.domain.InviteRecord;
import com.mxsella.smartrecharge.model.domain.TestUser;
import com.mxsella.smartrecharge.model.domain.User;
import com.mxsella.smartrecharge.model.request.ApplyTimesRequestBody;
import com.mxsella.smartrecharge.model.request.DealApplyRequestBody;
import com.mxsella.smartrecharge.model.request.DeviceRechargeRequestBody;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.request.UpdateDeviceBrandRequestBody;
import com.mxsella.smartrecharge.model.request.UserRechargeRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://mmk.mxsella.com/ManagementSystem/api/v1/";

    @GET("users/{user}/repos")
    Observable<BaseResponse<TestUser>> listR(@Path("user") String user);

    /**
     * 获取验证码
     *
     * @param telephone 手机号
     * @return
     */
    @GET("user/send_verify_code")
    Observable<BaseResponse<String>> getVerifyCode(@Query("telephone") String telephone, @Query("type") String type);

    /**
     * 用户登录
     *
     * @param telephone 手机号
     * @param password  密码
     * @return
     */
    @POST("user/login")
    Observable<BaseResponse<User>> loginPwd(@Query("telephone") String telephone, @Query("password") String password);

    /**
     * 检查当前手机号是否已注册
     *
     * @param telephone 手机号
     * @return
     */
    @GET("user/check_telephone")
    Observable<BaseResponse<String>> checkTelephone(@Query("telephone") String telephone);

    /**
     * 生成邀请码
     *
     * @param subNameRequestBody 请求体
     * @return
     */
    @POST("invite_code/create")
    Observable<BaseResponse<String>> createInviteCode(@Body SubNameRequestBody subNameRequestBody);

    /**
     * 获取生成邀请码历史
     *
     * @return
     */
    @POST("invite_code/list/{current}/{size}")
    Observable<BaseResponse<ListResponse<InviteRecord>>> getInviteHistory(@Path("current") int current, @Path("size") int size);

    /**
     * 邀请码注册
     *
     * @param telephone  手机号
     * @param verifyCode 验证码
     * @param inviteCode 邀请码
     * @return
     */
    @POST("user/register")
    Observable<BaseResponse<User>> register(@Query("telephone") String telephone, @Query("verifyCode") String verifyCode, @Query("inviteCode") String inviteCode);


    /**
     * 验证码登录
     *
     * @param telephone  手机号
     * @param verifyCode 验证码
     * @return
     */
    @POST("user/login_verify")
    Observable<BaseResponse<User>> loginVerify(@Query("telephone") String telephone, @Query("verifyCode") String verifyCode);

    /**
     * 获取子级用户
     *
     * @return
     */
    @POST("user/sub/list/{current}/{size}")
    Observable<BaseResponse<ListResponse<User>>> getChildrenUser(@Path("current") int current, @Path("size") int size);

    /**
     * 验证码修改密码
     *
     * @param telephone
     * @param verify
     * @param password
     * @return
     */
    @POST("user/password/verify_change")
    Observable<BaseResponse<ListResponse<String>>> changePasswordWithCode(@Query("telephone") String telephone, @Query("verifyCode") String verify, @Query("password") String password);


    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @POST("user/password/change")
    Observable<BaseResponse<ListResponse<String>>> changePassword(@Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    /**
     * 修改用户信息
     *
     * @param userRequestBody
     * @return
     */
    @POST("user/user_info/change")
    Observable<BaseResponse<User>> changeUser(@Body ModifyUserRequestBody userRequestBody);


    /**
     * 修改下级信息
     *
     * @param userRequestBody
     * @return
     */
    @POST("user/user_info/sub_name/change")
    Observable<BaseResponse<User>> changeSub(@Body ModifyUserRequestBody userRequestBody);
    //================================================================================================================================


    /**
     * 获取用户次数历史
     *
     * @param current
     * @param size
     * @param productName
     * @return
     */
    @POST("device/times/history/list/{current}/{size}")
    Observable<ListResponse<UserHistory>> getHistoryList(@Path("current") int current,
                                                         @Path("size") int size,
                                                         @Query("productName") String productName);


    /**
     * 获取用户充值码列表
     *
     * @param current
     * @param size
     * @param productName
     * @return
     */
    @POST("device/times/recharge_code/list/{current}/{size}")
    Observable<ListResponse<RechargeCode>> getRechargeCodeList(@Path("current") int current,
                                                               @Path("size") int size,
                                                               @Query("productName") String productName);


    /**
     * 使用充值码
     *
     * @param useRechargeCodeRequest
     * @return
     */
    Observable<BaseResponse<String>> useRechargeCode(@Body UseRechargeCodeRequest useRechargeCodeRequest);

    /**
     * 添加设备
     *
     * @param productName 产品名
     * @param deviceId    设备id
     * @return
     */
    @POST("device/device/add")
    Observable<BaseResponse<String>> addDevice(@Query("productName") String productName, @Query("deviceId") String deviceId);

    /**
     * 获取设备列表
     *
     * @return
     */
    @POST("device/device/list/{current}/{size}")
    Observable<BaseResponse<ListResponse<Device>>> getDeviceList(@Path("current") int current, @Path("size") int size, @Query("productName") String productName);

    /**
     * 更新设备厂商
     *
     * @param updateDeviceBrandRequestBody
     * @return
     */
    @POST("device/device/update_brand")
    Observable<BaseResponse<Brand>> updateBrand(@Body UpdateDeviceBrandRequestBody updateDeviceBrandRequestBody);

    /**
     * 获取用户产品剩余次数
     *
     * @param productName
     * @return
     */
    @GET("device/times/get")
    Observable<BaseResponse<Integer>> getUserTimes(@Query("productName") String productName);

    /**
     * 充值用户次数
     *
     * @param userRechargeRequestBody
     * @return
     */
    @POST("device/times/recharge/user")
    Observable<BaseResponse<Integer>> userRecharge(@Body UserRechargeRequestBody userRechargeRequestBody);

    /**
     * 充值设备次数
     *
     * @param deviceRechargeRequestBody
     * @return
     */
    @POST("device/times/recharge/device")
    Observable<BaseResponse<Integer>> deviceRecharge(@Body DeviceRechargeRequestBody deviceRechargeRequestBody);

    /**
     * 充值次数申请
     *
     * @param applyTimesRequestBody
     * @return
     */
    @POST("device/times/recharge/apply")
    Observable<BaseResponse<ApplyTimes>> applyTimes(@Body ApplyTimesRequestBody applyTimesRequestBody);

    /**
     * 处理申请
     *
     * @param dealApplyRequestBody
     * @return
     */
    @POST("device/times/recharge/deal_apply")
    Observable<BaseResponse<ApplyTimes>> dealApply(@Body DealApplyRequestBody dealApplyRequestBody);

    /**
     * 获取充值次数申请
     *
     * @param current
     * @param size
     * @param productName
     * @return
     */
    @POST("device/times/recharge/apply_list/{current}/{size}")
    Observable<BaseResponse<ListResponse<ApplyTimes>>> getApplyList(@Path("current") int current, @Path("size") int size, @Query("productName") String productName);

    /**
     * 获取子级充值申请记录
     *
     * @param current
     * @param size
     * @param productName
     * @return
     */
    @POST("device/times/recharge/sub_apply_list/{current}/{size}")
    Observable<BaseResponse<ListResponse<ApplyTimes>>> getChildApplyList(@Path("current") int current, @Path("size") int size, @Query("productName") String productName);
}
