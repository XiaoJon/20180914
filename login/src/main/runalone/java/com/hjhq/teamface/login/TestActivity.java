package com.hjhq.teamface.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.login.presenter.LoginActivity;

/**
 * Created by Administrator on 2018/4/17.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test_activity);
    }

    public void test(View view) {
        CommonUtil.startActivtiy(this, LoginActivity.class);
    }
}
