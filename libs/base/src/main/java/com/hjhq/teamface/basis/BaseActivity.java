package com.hjhq.teamface.basis;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.RefWatcher;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/6.
 * <p/>
 * 应用中所有activity的基类
 */
public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener {

    protected LayoutInflater inflater;

    protected final int DEFAULT = 0;

    RefWatcher refWatcher;

    private boolean useEventBus = false;

    public float mDensity;
    public int mDensityDpi;
    public int mAvatarSize;
    public int mWidth;
    public int mHeight;
    public float mRatio;
    protected RxAppCompatActivity mContext;

    public static final int NORMAL_STATE = 0;
    public static final int REFRESH_STATE = 1;
    public static final int LOAD_STATE = 2;
    private String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.e("Activity名字", "" + this.getClass().getName());
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mRatio = Math.min((float) mWidth / 720, (float) mHeight / 1280);
        mAvatarSize = (int) (50 * mDensity);

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        inflater = getLayoutInflater();

        mContext = this;
        setContentView(getContentView());

        translucentstatus();

        onSetContentViewNext(savedInstanceState);
        if (!isTitleActivity()) {
            ButterKnife.bind(this);
        }
        initView();
        initData();
        setListener();

        if (useEventBus()) {
            EventBusUtils.register(this);//eventbus注册
        }
        //  refWatcher = MyApplication.getRefWatcher(this);//leakcanary工具，在ondestory中检测内存泄漏

    }

    protected void translucentstatus() {
        if (this.getClass().getName().equals("com.hjhq.teamface.oa.main.MainActivity")) {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StatusBarUtil.setColorNoTranslucent(this, Color.WHITE);
                // StatusBarUtil.setColorNoTranslucent(this, ColorUtils.resToColor(this, R.color.white));
            } else {
                StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
            }
            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
            //不预留空间
            if (mContentView.getChildAt(0) != null) {
                ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), true);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    /**
     * 某些第三方sdk需要在这做操作
     *
     * @param savedInstanceState
     */
    protected void onSetContentViewNext(Bundle savedInstanceState) {
    }


    protected abstract int getContentView();


    /**
     * 设置整个activity的背景图片,在setContentView方法后调用才有效果
     *
     * @param resId
     */
    protected void setBackground(int resId) {
        View childView;
        ViewGroup contentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView.getChildCount() > 0) {
            childView = contentView.getChildAt(0);
            childView.setBackgroundResource(resId);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    protected void setBackgroundColor(int color) {
        View childView;
        ViewGroup contentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView.getChildCount() > 0) {
            childView = contentView.getChildAt(0);
            childView.setBackgroundColor(getResources().getColor(color));
        }
    }

    public void showToast(String msg) {
        if (!isFinishing()) {
            ToastUtils.showToast(mContext, msg);
        }
    }

    public void showToast(int msgid) {
        if (!isFinishing()) {
            ToastUtils.showToast(mContext, mContext.getString(msgid));
        }
    }

    public RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(Constants.IS_DEBUG);
        return rxPermissions;
    }

    /**
     * 是否有标题栏，如果有，需要在子类中执行butterknife的绑定
     *
     * @return
     */
    protected boolean isTitleActivity() {
        return false;
    }

    public boolean isUseEventBus() {
        return useEventBus;
    }

    public void setUseEventBus(boolean useEventBus) {
        this.useEventBus = useEventBus;
    }

    protected boolean useEventBus() {
        return useEventBus;
    }//是否使用eventbus

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (useEventBus()) {
            EventBusUtils.unregister(this);
        }
        //leakcanary检查内存泄漏问题
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
    }

    protected abstract void initView();

    protected abstract void setListener();

    protected abstract void initData();

    public void setOnClicks(View.OnClickListener listener, View... views) {
        if (views != null) {
            for (View view : views) {
                view.setOnClickListener(listener);
            }
        }
    }

    public void setOnClicks(View... views) {
        setOnClicks(this, views);
    }

    public void showLog(String log) {
        Logger.e(log);
    }


    @SuppressWarnings("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments == null)
            return;

        for (Fragment frag : fragments)
            if (frag != null)
                handleResult(frag, requestCode, resultCode, data);

        return;

    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressWarnings("RestrictedApi")
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

}
