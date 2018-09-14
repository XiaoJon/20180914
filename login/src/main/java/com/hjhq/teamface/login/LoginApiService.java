package com.hjhq.teamface.login;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PasswrodSetBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.login.bean.LoginResponseBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/9/28.
 * Describe：
 */

public interface LoginApiService {
    /**
     * 获取验证码
     *
     * @param map //0通用 1注册 2改密
     */

    @Headers({"Content-Type: application/json"})
    @POST("user/sendSmsCode")
    Observable<BaseBean> sendSmsCode(@Body Map<String, String> map);

    /**
     * 验证验证码
     *
     * @param map
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/verifySmsCode")
    Observable<BaseBean> verifySmsCode(@Body Map<String, String> map);

    /**
     * 用户注册
     *
     * @param map
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/userRegister")
    Observable<BaseBean> register(@Body Map<String, String> map);

    /**
     * 忘记密码密码
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/modifyPwd")
    Observable<LoginResponseBean> modifyPwd(@Body Map<String, String> map);


    /**
     * 修改密码
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/editPassWord")
    Observable<BaseBean> editPassWord(@Body Map<String, String> map);
    /**
     * 修改手机号
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/modifyPhone")
    Observable<BaseBean> editPhone(@Body Map<String, String> map);

    /**
     * 扫码登录
     *
     * @param map
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/valiLogin")
    Observable<BaseBean> valiLogin(@Body Map<String, String> map);

    /**
     * 登录
     *
     * @param map
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/login")
    Observable<LoginResponseBean> login(@Body Map<String, String> map);


    /**
     * 登录后获取个人信息
     */
    @GET("user/queryInfo")
    Observable<UserInfoBean> queryInfo();


    /**
     * 获取最近登录公司密码策略
     */
    @GET("user/getCompanySet")
    Observable<PasswrodSetBean> getCompanySet(@Query("phone") String phone);
}
