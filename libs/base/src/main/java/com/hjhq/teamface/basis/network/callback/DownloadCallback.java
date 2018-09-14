package com.hjhq.teamface.basis.network.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DownloadCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccess()) {
            onSuccess(call, response);
        } else {
            onFailure(call, new Throwable(response.message()));
        }
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    //用于进度的回调
    public abstract void onLoading(long total, long progress);
}
