package com.hjhq.teamface.basis;

/**
 * Created by Samda on 2017/2/24.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;


/**
 * Created by yu on 2016/11/25.
 */
public abstract class BaseFragment extends RxFragment implements View.OnClickListener {
    protected boolean firstLoad = true;
    protected boolean prepared = false;
    protected boolean visible = false;
    protected Context mContext;
    protected boolean eventBusUseble;

    public static final int NORMAL_STATE = 0;
    public static final int REFRESH_STATE = 1;
    public static final int LOAD_STATE = 2;

    /**
     * 当前布局
     */

    protected View rootView;
    RefWatcher refWatcher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // rootView = inflater.inflate(layout, container, false);
        this.mContext = getActivity();
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            int layout = getContentView();
            rootView = inflater.inflate(layout, container, false);
        }
        onSetContentViewNext(savedInstanceState);
        ButterKnife.bind(this, rootView);

        if (useEventBus()) {
            EventBusUtils.register(this);
        }
        //  refWatcher = MyApplication.getRefWatcher(getActivity());//leakcanary工具，在ondestory中检测内存泄漏
        return rootView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visible = isVisibleToUser;
        lazyLoad();
    }

    /**
     * only load data at first visible
     */
    private void lazyLoad() {
        if (visible && firstLoad && prepared) {
            firstLoad = false;
            initData();

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListener();
        prepared = true;
        lazyLoad();
    }

    public void showToast(String msg) {
        if (visible) {
            ToastUtils.showToast(getActivity(), msg);
        }
    }

    public void showToast(int msgid) {
        if (visible) {
            ToastUtils.showToast(getActivity(), getActivity().getString(msgid));
        }
    }

    /**
     * 某些第三方sdk需要在这做操作
     *
     * @param savedInstanceState
     */
    protected void onSetContentViewNext(Bundle savedInstanceState) {

    }

    /**
     * 是否使用eventBus,默认为使用(false)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return eventBusUseble;
    }

    public void setEventBusUseble(boolean useble) {
        this.eventBusUseble = useble;
    }

    /**
     * init view
     */
    protected abstract int getContentView();

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void setListener();


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);

        if (useEventBus()) {
            EventBusUtils.unregister(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refWatcher != null) {
            //// TODO: 2017/3/21  有时候refWatcher会报空指针异常，未查到原因
            //可能是refWatcher未初始化
            //leakcanary检查内存泄漏问题
            refWatcher.watch(this);
        }
    }

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


}
