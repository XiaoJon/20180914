package com.hjhq.teamface.basis.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppManager implements Application.ActivityLifecycleCallbacks {

    private static Stack<Activity> activityStack = new Stack<>();
    private static AppManager instance;
    public static Application application;

    private AppManager() {
    }


    /**
     * 初始化并监听application生命周期
     *
     * @param application
     * @return
     */
    public static AppManager init(Application application) {
        if (instance == null) {
            instance = getAppManager();
            AppManager.application = application;
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束其他Activity
     */
    public void finishOtherActivity(String cls) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (!activityStack.get(i).toString().equals(cls)) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 结束其他Activity
     */
    public void finishNotLastActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (i != size - 1) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        finishActivity(activity);
    }

    /**
     * 获取指定Activity
     *
     * @param className
     * @return
     */
    public Activity getActivity(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < activityStack.capacity(); i++) {
            if (activityStack.get(i).getClass().equals(clazz)) {
                return activityStack.get(i);
            }
        }
        return null;
    }

    /**
     * 重启应用
     */
    public static void restartApp() {
        Activity activity = null;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (i != size - 1) {
                activityStack.get(i).finish();
            } else {
                activity = activityStack.get(i);
            }
        }
        if (activity != null) {
            UIRouter.getInstance().openUri(activity, "DDComp://app/splash", null);
            activity.finish();
        }
    }
}

