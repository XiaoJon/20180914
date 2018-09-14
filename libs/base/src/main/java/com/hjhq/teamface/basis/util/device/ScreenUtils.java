package com.hjhq.teamface.basis.util.device;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 屏幕工具类
 * Created by Lin on 2016/7/18.
 */
public class ScreenUtils {
    private ScreenUtils() {
    }


    /**
     * 屏幕高度
     *
     * @param context 任意上下文
     */
    public static float getScreenHeight(Context context) {
        return DeviceUtils.getDisplayMetrics(context).heightPixels;
    }

    /**
     * 屏幕宽度
     *
     * @param context 任意上下文
     */
    public static float getScreenWidth(Context context) {
        return DeviceUtils.getDisplayMetrics(context).widthPixels;
    }

    /**
     * 是否是横屏
     *
     * @param context 任意上下文
     */
    public static boolean isLandscape(Context context) {
        boolean flag;
        if (context.getResources().getConfiguration().orientation == 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /**
     * 是否是竖屏
     *
     * @param context 任意上下文
     */
    public static boolean isPortrait(Context context) {
        boolean flag = true;
        if (context.getResources().getConfiguration().orientation != 1)
            flag = false;
        return flag;
    }

    /**
     * 屏幕变暗
     *
     * @param activity 活动界面
     */
    public static void letScreenGray(Activity activity) {
        if (activity instanceof RxAppCompatActivity) {
            Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(((RxAppCompatActivity) activity)
                            .bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(i -> updateGray(activity, .5f));
        }
    }

    /**
     * 屏幕变亮
     *
     * @param activity 活动界面
     */
    public static void letScreenLight(Activity activity) {
        if (activity instanceof RxAppCompatActivity)
            Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(((RxAppCompatActivity) activity)
                            .bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(i -> updateGray(activity, 1f));
    }

    /**
     * 更改评论亮度
     *
     * @param activity 活动界面
     * @param alpha    透明度
     */
    private static void updateGray(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;        // 变亮只需将.3f改成1即可
        activity.getWindow().setAttributes(lp);
    }

}
