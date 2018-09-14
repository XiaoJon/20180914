package com.hjhq.teamface.basis.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.android.BuildConfig;
import rx.functions.Action1;

/**
 * 系统功能工具类
 * Created by Administrator on 2018/2/7.
 */

public class SystemFuncUtils {

    /**
     * 复制文本到系统剪贴板
     *
     * @param context 上下文
     * @param text    文本
     */
    public static void copyTextToClipboard(Context context, String text) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        if (Build.VERSION.SDK_INT < 23) {
            android.text.ClipboardManager cm23 = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm23.setText(text);
        } else {
            ClipboardManager cm = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            //text是内容
            ClipData myClip = ClipData.newPlainText("text", text);
            cm.setPrimaryClip(myClip);
        }
    }


    /**
     * 拨打电话
     *
     * @param context
     * @param number
     */
    public static void callPhone(Activity context, String number) {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestCallPermissions(context, number);
            } else {
                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(call);
            }
        } else {
            Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(call);
        }
    }

    /**
     * 判断请求电话功能权限
     *
     * @param context
     * @param number
     */
    public static void requestCallPermissions(final Activity context, final String number) {


        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions.request(
                android.Manifest.permission.CALL_PHONE

        ).subscribe(aBoolean -> {
            if (aBoolean) {
                callPhone(context, number);
            } else {
                ToastUtils.showError(context, "必须获得必要的权限才能打电话！");
            }
        });
    }


    /**
     * 判断请求功能权限
     *
     * @param activity
     */
    public static void requestPermissions(Activity activity, String permission, Action1<Boolean> onNext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.setLogging(BuildConfig.DEBUG);
                rxPermissions.request(permission).subscribe(onNext);
            } else {
                onNext.call(true);
            }
        } else {
            onNext.call(true);
        }
    }

    /**
     * 方法描述：判断某一Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service的全路径： 包名 + service的类名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取软件版本
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * 判断Activity是否处于栈顶
     *
     * @return true在栈顶false不在栈顶
     */
    public static boolean isTopActivity(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(activity.getClass().getName());
    }
}
