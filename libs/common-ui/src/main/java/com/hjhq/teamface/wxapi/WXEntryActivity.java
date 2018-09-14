package com.hjhq.teamface.wxapi;


import android.content.Intent;
import android.os.Bundle;

import cn.jiguang.share.wechat.WeChatHandleActivity;

/**
 * Created by Administrator on 2018/8/6.
 * Describe：
 */

public class WXEntryActivity extends WeChatHandleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
        } else {
            super.onCreate(getIntent().getExtras());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
