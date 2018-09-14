package com.hjhq.teamface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;

import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.EventConstant;
import com.hjhq.teamface.basis.constants.MsgConstant;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.ProgressToast;
import com.hjhq.teamface.basis.util.AppManager;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.utils.AuthHelper;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.im.IM;
import com.hjhq.teamface.oa.login.logic.SettingHelper;
import com.hjhq.teamface.oa.main.MainActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import okhttp3.OkHttpClient;

/**
 * App模块的Application
 *
 * @author Administrator
 * @date 2017/2/15
 */

public class MyApplication extends MultiDexApplication {
    public static MyApplication context;
    private RefWatcher refWatcher;

    public static MyApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化腾讯X5WebView
        QbSdk.initX5Environment(this, null);
        //初始化上传/下载/工具类
        initOkHttp();
        //注册路由
        UIRouter.getInstance().registerUI("app");
        UIRouter.getInstance().registerUI("attendance");
        context = this;
        EventBusUtils.register(context);
        SPHelper.init(context);
        AppManager.init(context);
        IM.getInstance().initContext(context);
        JShareInterface.setDebugMode(true);
        PlatformConfig platformConfig = new PlatformConfig()
                .setWechat("wx3b6eafad547be6b1", "4468ea996f8d8ba440e5eb5938e82af7");
        JShareInterface.init(this, platformConfig);
        //内存泄漏
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            refWatcher = LeakCanary.install(this);
        }
        //bugly异常崩溃报告
        CrashReport.initCrashReport(getApplicationContext(), "782a1846bb", true);
        //异常捕获器
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

         /*Home键是系统事件，只能通过广播监听*/
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //其它设备登录
        filter.addAction(MsgConstant.LOGIN_ON_OTHER_DEVICES);
        //被系统强制离线
        filter.addAction(MsgConstant.BE_OFFLINE_BY_SYSTEM);
        //被PC端强制离线
        filter.addAction(MsgConstant.BE_OFFLINE_BY_PC_CLIENT);
        //企信账号不存在/异常
        filter.addAction(MsgConstant.IM_ACCOUNT_NOT_EXIST);
        //数据库异常
        filter.addAction(MsgConstant.IM_DATABASE_ERROR);
        registerReceiver(receiver, filter);
    }

    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * HomeKey监听事件广播接收器
     * 关闭window
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MsgConstant.LOGIN_ON_OTHER_DEVICES:
                    //账号在其它设备登录
                    //ToastUtils.showToast(MyApplication.this, "当前账号已在其他手机登录，如不是本人操作，请修改密码");
                    SettingHelper.logout(MsgConstant.TYPE_LOGIN_ON_OTHER_DEVICE);
                    break;
                case MsgConstant.BE_OFFLINE_BY_SYSTEM:
                    //被系统强制离线
                    //ToastUtils.showToast(MyApplication.this, "当前会话被后台退出，请重新登录");
                    SettingHelper.logout(MsgConstant.TYPE_FORCE_OFFLINE_BY_SYSTEM);
                    break;
                case MsgConstant.BE_OFFLINE_BY_PC_CLIENT:
                    //被PC端强制离线
                    //ToastUtils.showToast(MyApplication.this, "当前会话被PC端退出，请重新登录");
                    SettingHelper.logout(MsgConstant.TYPE_FORCE_OFFLINE_BY_PC);
                    break;
                case MsgConstant.IM_ACCOUNT_NOT_EXIST:
                    //企信账号不存在
                    //ToastUtils.showToast(MyApplication.this, "账户异常");
                    SettingHelper.logout(MsgConstant.TYPE_IM_ACCOUNT_ERROR);
                    break;
                case MsgConstant.IM_DATABASE_ERROR:
                    IM.getInstance().init(context);
                    IM.getInstance().setupDatabase();
                    break;
            }
            ProgressToast.getInstance().closeWindowView();
        }
    };

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageBean event) {
        int code = event.getCode();
        switch (code) {
            case HttpResultFunc.LOGOUT:
                SettingHelper.logout(MsgConstant.TYPE_LOGIN_ON_OTHER_DEVICE);
                break;
            case HttpResultFunc.NOT_PERMISSION:
                AuthHelper.authMap.clear();
                break;
            case EventConstant.LOGIN_TAG:
                CommonUtil.startActivtiyNewTask(context, MainActivity.class);
                break;
            default:
                break;
        }
    }

}
