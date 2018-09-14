package com.hjhq.teamface.oa.login.logic;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjhq.teamface.MyApplication;
import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.IMState;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.file.FormatFileSizeUtil;
import com.hjhq.teamface.basis.util.file.JYFileHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.hjhq.teamface.bean.GetAuthBean;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.im.activity.TeamMessageFragment;
import com.hjhq.teamface.im.adapter.ConversationListController;
import com.hjhq.teamface.oa.login.ui.SplashActivity;
import com.hjhq.teamface.util.CommonUtil;

import java.io.File;
import java.util.List;

/**
 * 设置帮助类
 */
public class SettingHelper {


    /**
     * 清理缓存
     */
    public static boolean clearCache() {
        File file = JYFileHelper.creatSDDir(Constants.PATH_ROOT);
        return JYFileHelper.delDir(file, false);
    }

    /**
     * 获取缓存大小
     */
    public static String getCacheSize() {
        File file = JYFileHelper.creatSDDir(Constants.PATH_ROOT);
        long totalSizeOfFilesInDir = JYFileHelper.getTotalSizeOfFilesInDir(file);
        return FormatFileSizeUtil.formatFileSize(totalSizeOfFilesInDir);
    }


    public static boolean checkPwd(String pwd) {
        boolean isOK = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() >= 6) {
            isOK = true;
        }
        return isOK;
    }

    public static boolean checkPhone(String phone) {
        boolean isOK = false;
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            isOK = true;
        }
        return isOK;
    }


    /**
     * 退出登录
     */
    public static void logout(int... type) {
        //企信退出登录
        IM.getInstance().logout();
        IMState.setImOnlineState(false);
        IMState.setImCanLogin(false);
        IMState.setImConnectLlOk(false);

        //清除内存中会话列表的缓存
        TeamMessageFragment.clear();
        //清除通知栏提醒消息
        ConversationListController.cleanNotify();
        //将登录标志位改为false
        SPHelper.setLoginBefore(false);
        //清空sp
        SPUtils.remove(MyApplication.getInstance(), AppConst.TOKEN_KEY);
        SPUtils.remove(MyApplication.getInstance(), AppConst.USER_PASSCODE);
        SPUtils.remove(MyApplication.getInstance(), AppConst.USER_ID);
        SPUtils.remove(MyApplication.getInstance(), AppConst.EMPLOYEE_ID);
        SPUtils.remove(MyApplication.getInstance(), AppConst.COMPANY_ID);
        SPUtils.remove(MyApplication.getInstance(), AppConst.USER_INFO);
        SPUtils.cleanCache();

        ApiManager.clear();
       /* //跳转到登录界面
        UIRouter.getInstance().openUri(AppManager.getAppManager().currentActivity(), "DDComp://login/login", null);*/
        //跳转到欢迎界面
        AppManager.getAppManager().finishNotLastActivity();
        final Activity activity = AppManager.getAppManager().currentActivity();
        Bundle bundle = new Bundle();
        if (type != null && type.length > 0) {
            bundle.putInt(Constants.DATA_TAG1, type[0]);
        } else {
            bundle.putInt(Constants.DATA_TAG1, 0);
        }
        CommonUtil.startActivtiy(activity, SplashActivity.class, bundle);
        activity.finish();
    }

    /**
     * 权限检测
     *
     * @param permssionId 权限
     * @return 是否具有权限
     */
    public static boolean checkPermssion(int permssionId) {
        String strJson = SPUtils.getString(MyApplication.getInstance(), AppConst.AUTH);
        List<GetAuthBean.DataBean.RolePermssionsBean> dataList = new Gson().fromJson(strJson, new TypeToken<List<GetAuthBean.DataBean.RolePermssionsBean>>() {
        }.getType());

        if (dataList != null) {
            for (GetAuthBean.DataBean.RolePermssionsBean bean : dataList) {
                if (permssionId == bean.getCode()) {
                    return true;
                }
            }
        }
        return false;
    }
}
