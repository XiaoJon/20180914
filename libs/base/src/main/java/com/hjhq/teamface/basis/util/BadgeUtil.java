package com.hjhq.teamface.basis.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.constants.MobileBrand;
import com.hjhq.teamface.basis.constants.MsgConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 * Describe：
 */

public class BadgeUtil {
    public static NotificationManager mNotificationManager;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBadge(Context context, int number) {
        Log.e("制造商", Build.MANUFACTURER);
        Log.e("产品品牌", Build.BRAND);
        Log.e("设备", Build.DEVICE);

        switch (Build.BRAND.toLowerCase()) {
            case MobileBrand.HUAWEI:
                setBadgeOfHuawei(context, number);
                break;
            case MobileBrand.XIAOMI:
                setBadgeOfXiaomi(context, number, getNotification(context, "", ""));
                break;
            case MobileBrand.OPPO:
                setBadgeOfOPPO(context, number);
                break;
            case MobileBrand.VIVO:
                setBadgeOfVIVO(context, number);
                break;
            case MobileBrand.SAMSUNG:
            case MobileBrand.LG:
            case MobileBrand.LGE:
                setBadgeOfVIVO(context, number);
                break;
            case MobileBrand.SONY:
                setBadgeOfSony(context, getNotification(context, "", ""), MsgConstant.BADGE_NOTIFICATION_ID, number);
                break;
            case MobileBrand.HUAWEI_HONOR:
            case MobileBrand.HUAWEI_NOVA:
                setBadgeOfHuawei(context, number);
                break;
            case MobileBrand.HTC:
                setBadgeOfHTC(context, getNotification(context, "", ""), MsgConstant.BADGE_NOTIFICATION_ID, number);
                break;
            case MobileBrand.LENOVO_ZUK:
                setBadgeOfZUK(context, getNotification(context, "", ""), MsgConstant.BADGE_NOTIFICATION_ID, number);
                break;
        }
    }

    /**
     * 华为显示角标
     *
     * @param context
     * @param number
     */
    private static void setBadgeOfHuawei(Context context, int number) {
        try {
            if (number < 0) number = 0;
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * VIVO显示角标
     *
     * @param context
     * @param number
     */
    private static void setBadgeOfVIVO(Context context, int number) {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
            intent.putExtra("className", launchClassName);
            intent.putExtra("notificationNum", number);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ZUK显示角标数
     *
     * @param context
     * @param notification
     * @param NOTIFI_ID
     * @param num
     */
    private static void setBadgeOfZUK(Context context, Notification notification, int NOTIFI_ID, int num) {
        final Uri CONTENT_URI = Uri.parse("content://com.android.badge/badge");
        try {
            Bundle extra = new Bundle();
            extra.putInt("app_badge_count", num);
            context.getContentResolver().call(CONTENT_URI, "setAppBadgeCount", null, extra);

            NotificationManager notifyMgr = (NotificationManager) (context.getSystemService(Activity.NOTIFICATION_SERVICE));
            if (num != 0) notifyMgr.notify(NOTIFI_ID, notification);
            else notifyMgr.cancel(NOTIFI_ID);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ZUK" + " Badge error", "set Badge failed");
        }

    }

    /**
     * NOVA显示角标数
     *
     * @param context
     * @param notification
     * @param NOTIFI_ID
     * @param num
     */
    private static void setBadgeOfNOVA(Context context, Notification notification, int NOTIFI_ID, int num) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tag", context.getPackageName() + "/" + getLauncherClassName(context));
            contentValues.put("count", num);
            context.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"), contentValues);

            NotificationManager notifyMgr = getNotificationManager(context);
            if (num != 0) notifyMgr.notify(NOTIFI_ID, notification);
            else notifyMgr.cancel(NOTIFI_ID);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("NOVA" + " Badge error", "set Badge failed");
        }
    }

    /**
     * HTC显示角标数
     *
     * @param context
     * @param notification
     * @param NOTIFI_ID
     * @param num
     */
    private static void setBadgeOfHTC(Context context, Notification notification, int NOTIFI_ID, int num) {

        try {
            Intent intent1 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
            intent1.putExtra("com.htc.launcher.extra.COMPONENT", context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().flattenToShortString());
            intent1.putExtra("com.htc.launcher.extra.COUNT", num);

            Intent intent = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            intent.putExtra("packagename", context.getPackageName());
            intent.putExtra("count", num);

            if (canResolveBroadcast(context, intent1) || canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent1);
                context.sendBroadcast(intent);
            } else {
                Log.e("HTC" + " Badge error", "unable to resolve intent: " + intent.toString());
            }
            NotificationManager notifyMgr = getNotificationManager(context);
            if (num != 0) notifyMgr.notify(NOTIFI_ID, notification);
            else notifyMgr.cancel(NOTIFI_ID);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTC" + " Badge error", "set Badge failed");
        }

    }

    /**
     * MIUI显示角标数
     *
     * @param context
     * @param number
     * @param notification
     */
    public static void setBadgeOfXiaomi(Context context, int number, Notification notification) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getNotificationManager(context).notify(MsgConstant.BADGE_NOTIFICATION_ID, notification);
    }

    /**
     * OPPO显示角标数
     *
     * @param context
     * @param number
     */
    private static void setBadgeOfOPPO(Context context, int number) {
        try {
            if (number == 0) {
                number = -1;
            }
            Intent intent2 = new Intent("com.oppo.unsettledevent");
            intent2.putExtra("pakeageName", context.getPackageName());
            intent2.putExtra("number", number);
            intent2.putExtra("upgradeNumber", number);
            if (canResolveBroadcast(context, intent2)) {
                context.sendBroadcast(intent2);
            } else {
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("app_badge_count", number);
                    context.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", null, extras);
                } catch (Throwable th) {
                    Log.e("OPPO" + " Badge error", "unable to resolve intent: " + intent2.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("OPPO" + " Badge error", "set Badge failed");
        }

    }

    /**
     * 生成通知
     *
     * @param context
     * @param title
     * @param content
     * @return
     */
    private static Notification getNotification(Context context, String title, String content) {
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle("")
                    .setContentText("")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new Notification.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(R.mipmap.ic_launcher)
                    /**通知首次出现在通知栏，带上升动画效果的**/
                    .setTicker("")
                    /**点击跳转**/
                    /**设置通知的标题**/
                    .setContentTitle(title)
                    /**设置通知的内容**/
                    .setContentText(content)
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    /**设置该通知优先级**/
                    /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                    .setOngoing(false)
                    .build();
        }
        return notification;
    }

    /**
     * Sony显示角标数
     *
     * @param context
     * @param notification
     * @param NOTIFI_ID
     * @param num
     */
    private static void setBadgeOfSony(Context context, Notification notification, int NOTIFI_ID, int num) {
        String numString = "";
        String activityName = getLauncherClassName(context);
        if (activityName == null) {
            return;
        }
        Intent localIntent = new Intent();
        boolean isShow = true;
        if (num < 1) {
            numString = "";
            isShow = false;
        } else if (num > 99) {
            numString = "99";
        }
        try {
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);
            localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", activityName);
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", numString);
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());
            context.sendBroadcast(localIntent);

            NotificationManager notifyMgr = getNotificationManager(context);
            if (num != 0) notifyMgr.notify(NOTIFI_ID, notification);
            else notifyMgr.cancel(NOTIFI_ID);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SONY" + " Badge error", "set Badge failed");
        }
    }

    /**
     * Samsung&&LG显示角标数
     *
     * @param context
     * @param notification
     * @param NOTIFI_ID
     * @param num
     */
    private static void setBadgeOfSamsung(Context context, Notification notification, int NOTIFI_ID, int num) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", num);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);


            NotificationManager notifyMgr = getNotificationManager(context);
            if (num != 0) notifyMgr.notify(NOTIFI_ID, notification);
            else notifyMgr.cancel(NOTIFI_ID);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SAMSUNG" + " Badge error", "set Badge failed");
        }
    }


    /**
     * 获取启动器类名
     *
     * @param context
     * @return
     */
    private static String getLauncherClassName(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();

    }

    /**
     * 获取通知管理器
     *
     * @param context
     * @return
     */
    private static NotificationManager getNotificationManager(Context context) {
        if (mNotificationManager != null) {
            return mNotificationManager;
        }
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(MsgConstant.MESSAGE_CHANNEL, channelName, importance);
            channelName = "推送消息";
            importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(MsgConstant.PUSH_CHANNEL, channelName, importance);
        }
        return mNotificationManager;
    }

    /**
     * 检查广播
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }

    /**
     * 创建通知通道
     *
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        mNotificationManager.createNotificationChannel(channel);
    }
}
