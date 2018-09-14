package com.hjhq.teamface.basis.network.callback;

import retrofit2.Callback;

/**
 * @author Administrator
 * @date 2017/12/11
 */

public abstract class RetrofitCallback<T> implements Callback<T> {
    public abstract void onLoading(long total, long progress);
}