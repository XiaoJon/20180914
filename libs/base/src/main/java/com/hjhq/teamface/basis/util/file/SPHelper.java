package com.hjhq.teamface.basis.util.file;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.database.Member;

import java.util.List;


/**
 * SP帮助类
 * 帮助处理SP业务，与业务逻辑耦合
 * Created by Administrator on 2018/2/1.
 */

public class SPHelper {
    private static Context context;

    public static void init(Application application) {
        context = application;
    }

    /**
     * 根据员工id来获取 保存常用联系人的SP名称
     *
     * @return 联系人的SharedPreferences 名称
     */
    private static String getContactsName() {
        return SPUtils.getString(context, AppConst.EMPLOYEE_ID);
    }

    /**
     * 获取最近联系人
     */
    public static List<Member> getRecentContacts() {
        String recentStr = SPUtils.getString(context, getContactsName(), AppConst.RECENTLY_CONTACTS);
        return new Gson().fromJson(recentStr, new TypeToken<List<Member>>() {
        }.getType());
    }

    /**
     * 保存最近联系人
     *
     * @param datalist
     * @param nullable
     * @param <T>
     */
    public static <T> boolean saveRecentContacts(List<T> datalist, boolean nullable) {
        return SPUtils.setDataList(context, getContactsName(), AppConst.RECENTLY_CONTACTS, datalist, nullable);
    }

    /**
     * 得到用户信息
     *
     * @param type : 类型
     */
    public static <T> T getUserInfo(Class<T> type) {
        return SPUtils.getObject(context, AppConst.USER_INFO, type);
    }

    /**
     * 保存用户信息
     *
     * @param value : 用户信息
     * @return 是否成功
     */
    public static boolean setUserInfo(Object value) {
        return SPUtils.setObject(context, AppConst.USER_INFO, value);
    }

    /**
     * 得到用户帐号
     */
    public static String getUserAccount() {
        return SPUtils.getString(context, AppConst.USER_ACCOUNT);
    }

    /**
     * 保存用户帐号
     *
     * @param value : 用户帐号
     * @return 是否成功
     */
    public static boolean setUserAccount(String value) {
        return SPUtils.setString(context, AppConst.USER_ACCOUNT, value);
    }

    /**
     * 得到用户密码
     */
    public static String getUserPassword() {
        return SPUtils.getString(context, AppConst.USER_PASSCODE);
    }

    /**
     * 保存用户密码
     *
     * @param value : 用户密码
     * @return 是否成功
     */
    public static boolean setUserPassword(String value) {
        return SPUtils.setString(context, AppConst.USER_PASSCODE, value);
    }

    /**
     * 得到用户ID
     */
    public static String getUserId() {
        return SPUtils.getString(context, AppConst.USER_ID);
    }

    /**
     * 保存用户ID
     *
     * @param value : 用户ID
     * @return 是否成功
     */
    public static boolean setUserId(String value) {
        return SPUtils.setString(context, AppConst.USER_ID, value);
    }

    /**
     * 保存聊天socket uri
     *
     * @param value
     * @return
     */
    public static boolean setImSocketUri(String value) {
        return SPUtils.setString(context, AppConst.SOCKET_URI, value);
    }

    /**
     * 获取Socket uri
     *
     * @return
     */
    public static String getImSocketUri() {
        return SPUtils.getString(context, AppConst.SOCKET_URI);
    }

    /**
     * 得到用户名称
     */
    public static String getUserName() {
        return SPUtils.getString(context, AppConst.USER_NAME);
    }

    /**
     * 保存用户名称
     *
     * @param value : 用户名称
     * @return 是否成功
     */
    public static boolean setUserName(String value) {
        return SPUtils.setString(context, AppConst.USER_NAME, value);
    }

    /**
     * 得到用户头像
     */
    public static String getUserAvatar() {
        return SPUtils.getString(context, AppConst.USER_AVATAR);
    }

    /**
     * 保存用户头像
     *
     * @param value : 用户头像
     * @return 是否成功
     */
    public static boolean setUserAvatar(String value) {
        return SPUtils.setString(context, AppConst.USER_AVATAR, value);
    }

    /**
     * 得到公司ID
     */
    public static String getCompanyId() {
        return SPUtils.getString(context, AppConst.COMPANY_ID);
    }

    /**
     * 保存公司ID
     *
     * @return 是否成功
     */
    public static boolean setCompanyId(String value) {
        return SPUtils.setString(context, AppConst.COMPANY_ID, value);
    }


    /**
     * 得到部门ID
     */
    public static String getDepartmentId() {
        return SPUtils.getString(context, AppConst.DEPARTMENT_ID);
    }

    /**
     * 保存部门ID
     *
     * @return 是否成功
     */
    public static boolean setDepartmentId(String value) {
        return SPUtils.setString(context, AppConst.DEPARTMENT_ID, value);
    }

    /**
     * 得到员工ID
     */
    public static String getEmployeeId() {
        return SPUtils.getString(context, AppConst.EMPLOYEE_ID);
    }

    /**
     * 保存员工ID
     *
     * @param value : 员工ID
     * @return 是否成功
     */
    public static boolean setEmployeeId(String value) {
        return SPUtils.setString(context, AppConst.EMPLOYEE_ID, value);
    }

    /**
     * 得到token
     */
    public static String getToken() {
        return SPUtils.getString(context, AppConst.TOKEN_KEY);
    }

    /**
     * 保存token
     *
     * @param value : token
     * @return 是否成功
     */
    public static boolean setToken(String value) {
        return SPUtils.setString(context, AppConst.TOKEN_KEY, value);
    }


    /**
     * 得到当前公司名称
     */
    public static String getCompanyName() {
        return SPUtils.getString(context, AppConst.LATELY_COMPANY_NAME);
    }

    /**
     * 设置当前公司名称
     *
     * @param value : 公司名称
     * @return 是否成功
     */
    public static boolean setCompanyName(String value) {
        return SPUtils.setString(context, AppConst.LATELY_COMPANY_NAME, value);
    }

    /**
     * 得到当前设备IMEI
     */
    public static String getImei() {
        return SPUtils.getString(context, AppConst.IMEI);
    }

    /**
     * 设置当前设备IMEI
     *
     * @param value : 当前设备IMEI
     * @return 是否成功
     */
    public static boolean setImei(String value) {
        return SPUtils.setString(context, AppConst.IMEI, value);
    }


    /**
     * 得到当前部门名称
     */
    public static String getDepartmentName() {
        return SPUtils.getString(context, AppConst.MAIN_DEPARTMENT_NAME);
    }

    /**
     * 设置当前部门名称
     *
     * @return 是否成功
     */
    public static boolean setDepartmentName(String value) {
        return SPUtils.setString(context, AppConst.MAIN_DEPARTMENT_NAME, value);
    }

    /**
     * 是否已经登录
     */
    public static boolean isLoginBefore() {
        return SPUtils.getBoolean(context, AppConst.IS_LOGIN_BEFORE);
    }

    /**
     * 设置是否登录
     *
     * @param value : 是否登录
     */
    public static boolean setLoginBefore(boolean value) {
        return SPUtils.setBoolean(context, AppConst.IS_LOGIN_BEFORE, value);
    }


    /**
     * 得到程序崩溃时间
     */
    public static long getCrashTime() {
        return SPUtils.getLong(context, AppConst.CRASH_TIME);
    }

    /**
     * 设置程序崩溃时间
     *
     * @param time : 崩溃时间
     */
    public static boolean setCrashTime(long time) {
        return SPUtils.setLong(context, AppConst.CRASH_TIME, time);
    }

    /**
     * 非WiFi网络时文件大小限制
     *
     * @return
     */
    public static boolean getSizeLimitState() {
        return getSizeLimitState(false);

    }

    /**
     * 非WiFi网络时文件大小限制
     *
     * @return
     */
    public static boolean getSizeLimitState(boolean def) {
        return SPUtils.getBoolean(context, getContactsName(), AppConst.FILE_SIZE_LIMIT, def);

    }

    /**
     * 保存文件大小限制状态
     *
     * @param b
     * @return
     */
    public static boolean setSizeLimitState(boolean b) {
        return SPUtils.setBoolean(context, getContactsName(), AppConst.FILE_SIZE_LIMIT, b);

    }

}
