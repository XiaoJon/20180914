package com.hjhq.teamface.basis.network.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.StatusBarUtil;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * 全局loading
 *
 * @author Administrator
 * @date 2018/4/2
 */

public class DialogProgressActivity extends RxAppCompatActivity {
    /**
     * 计数器
     */
    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count++;
        LogUtil.d(count + "onCreate+++++++++");
        setContentView(R.layout.dialog_loading_two);
        translucentStatus();

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                count--;
                LogUtil.d(count + "CLOSE---------");
                if (count <= 0) {
                    finish();
                }
            }
        }, new IntentFilter(Constants.PROGRESS_ACTION_CLOSE));

        Observable
                .timer(30, TimeUnit.SECONDS)
                .compose(bindToLifecycle())
                .subscribe(v -> finish());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        count++;
        LogUtil.d(count + "onNewIntent+++++++++");
        super.onNewIntent(intent);
    }

    protected void translucentStatus() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
