package com.hjhq.teamface.basis.rxbus;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 * 只用于当前界面数据传递
 * <p>
 * Activity需要在onDetroys()中clean，避免内存泄漏
 * Created by baixiaokang on 16/4/28.
 */
public class RxManager {
    private static Map<Object, RxManager> instance = new HashMap<>();

    public static synchronized RxManager $(Object tag) {
        RxManager rxManager = instance.get(tag);
        if (null == rxManager) {
            rxManager = new RxManager();
            instance.put(tag,rxManager);
        }
        return rxManager;
    }

    private RxManager() {
    }

    public RxBus mRxBus = RxBus.$();
    private Map<Object, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者


    /**
     * 注册
     *
     * @param eventName
     * @param action1
     */
    public void on(Object eventName, Action1<Object> action1) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, (e) -> e.printStackTrace()));
    }

    public void onSticky(Object eventName, Action1<Object> action1) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, (e) -> e.printStackTrace()));
        mRxBus.onSticky(eventName, (Subject) mObservable);
    }

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    /**
     * 清除
     */
    public void clearAll() {
        for (Object next : instance.keySet()) {
            instance.get(next).clear();
        }
        instance.clear();
    }

    public void clear() {
        mCompositeSubscription.clear();// 清除订阅
        for (Map.Entry<Object, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

    public void postDelayed(Object tag, Object content, long time) {
        mRxBus.postDelayed(tag, content, time);
    }
}
