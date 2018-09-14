/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hjhq.teamface.basis.zygote;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hjhq.teamface.basis.BuildConfig;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.ToolMenu;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.MenuUtil;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.squareup.leakcanary.RefWatcher;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;


/**
 * Presenter base class for Activity
 * Presenter层的实现基类
 *
 * @param <T> View delegate class type
 */
public abstract class ActivityPresenter<T extends IDelegate, X extends IModel> extends RxAppCompatActivity {
    protected T viewDelegate;
    protected X model;
    protected ActivityPresenter mContext;
    private RefWatcher refWatcher;
    //加载状态
    public static final int NORMAL_STATE = 0;
    public static final int REFRESH_STATE = 1;
    public static final int LOAD_STATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create(savedInstanceState);
        mContext = this;
        getDelegateView();
        getModel();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            try {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        viewDelegate.create(getLayoutInflater(), null, savedInstanceState);
        setContentView(viewDelegate.getRootView());
        translucentstatus();

        viewDelegate.initWidget();
        init();
        bindEvenListener();

        if (useEventBus()) {
            EventBusUtils.register(this);//eventbus注册
        }
//        refWatcher = MyApplication.getRefWatcher(this);//leakcanary工具，在ondestory中检测内存泄漏

    }

    public abstract void init();

    public void create(Bundle savedInstanceState) {
    }

    protected void bindEvenListener() {
    }

    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewDelegate.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewDelegate == null) {
            getDelegateView();
        }
    }

    protected void getDelegateView() {
        try {
            Class<? extends ActivityPresenter> clazz;
            if (getClass().getSuperclass() == ActivityPresenter.class) {
                clazz = this.getClass();
            } else {
                clazz = (Class<? extends ActivityPresenter>) getClass().getSuperclass();
            }
            Class<T> mPresenterClass = (Class) ((ParameterizedType) (clazz
                    .getGenericSuperclass())).getActualTypeArguments()[0];

            viewDelegate = mPresenterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    public void getModel() {
        try {
            Class<? extends ActivityPresenter> clazz;
            if (getClass().getSuperclass() == ActivityPresenter.class) {
                clazz = this.getClass();
            } else {
                clazz = (Class<? extends ActivityPresenter>) getClass().getSuperclass();
            }

            Class<X> mModelClass = (Class) ((ParameterizedType) (clazz
                    .getGenericSuperclass())).getActualTypeArguments()[1];

            model = mModelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create IModel error");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewDelegate == null) {
            getDelegateView();
        }
        List<ToolMenu> menus = viewDelegate.getMenus();
        for (int i = 0; i < menus.size(); i++) {
            ToolMenu toolMenu = menus.get(i);
            String title = toolMenu.getTitle();
            MenuItem item = menu.add(0, i, i, title);
            if (TextUtils.isEmpty(title)) {
                item.setIcon(toolMenu.getIcon());
            }
            MenuUtil.setActionBarText(this, ColorUtils.resToColor(this, toolMenu.getTitleColor()));
            item.setShowAsAction(Menu.FLAG_ALWAYS_PERFORM_CLOSE);
        }
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (ToolMenu toolmenu : viewDelegate.getMenus()) {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (toolmenu.getId() == item.getItemId()) {
                    item.setVisible(toolmenu.isShow());
                    break;
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        return rxPermissions;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewDelegate.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxManager.$(hashCode()).clear();
        if (useEventBus()) {
            EventBusUtils.unregister(this);
        }
        //leakcanary检查内存泄漏问题
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
        viewDelegate.close();
        viewDelegate = null;
    }


    HashMap<Integer, OnActivityResult> resultHashMap = new HashMap<>();

    public void setOnActivityResult(Integer request, OnActivityResult onActivityResult) {
        resultHashMap.put(request, onActivityResult);
    }


    public interface OnActivityResult {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Integer next : resultHashMap.keySet()) {
            if (requestCode == next) {
                resultHashMap.get(next).onActivityResult(requestCode, resultCode, data);
                resultHashMap.remove(next);
                return;
            }
        }
        //对于Fragment使用FragmentActivity 的startActivityForResult 方法启动的通过回调返回
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments == null) {
            return;
        }

        for (Fragment frag : fragments) {
            if (frag != null) {
                handleResult(frag, requestCode, resultCode, data);
            }
        }
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
        }
    }


    /**
     * 透明状态栏
     */
    protected void translucentstatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColorNoTranslucent(this, ColorUtils.resToColor(this, R.color.white));

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
        }
        ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), true);
        }
    }
}
