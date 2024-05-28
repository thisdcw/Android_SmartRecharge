package com.mxsella.smartrecharge.common.net.service;

import com.mxsella.smartrecharge.common.net.BaseResponse;
import com.mxsella.smartrecharge.model.Brand;
import com.mxsella.smartrecharge.model.Device;
import com.mxsella.smartrecharge.model.InviteRecord;
import com.mxsella.smartrecharge.model.TestUser;
import com.mxsella.smartrecharge.model.User;
import com.mxsella.smartrecharge.model.request.SubNameRequestBody;
import com.mxsella.smartrecharge.model.response.ListResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Observable<BaseResponse<ListResponse<Device>>> getDeviceList(@Path("current") int current, @Path("size") int size,@Query("productName") String productName);

    /**
     * 更新设备厂商
     *
     * @param productName
     * @param deviceId
     * @param targetUid
     * @return
     */
    @POST("device/device/update_brand")
    Observable<BaseResponse<Brand>> updateBrand(@Query("productName") String productName, @Query("deviceId") String deviceId, @Query("targetUid") String targetUid);
}
