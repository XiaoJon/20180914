package com.hjhq.teamface.basis;

import android.content.Context;

import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.SingleInstance;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lx on 2017/3/14.
 */

public class BaseLogic extends SingleInstance {

    protected static <T> void  toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
    
    protected void showToast(Context context,String text){
        ToastUtils.showToast(context,text);
    }
    
    
}
