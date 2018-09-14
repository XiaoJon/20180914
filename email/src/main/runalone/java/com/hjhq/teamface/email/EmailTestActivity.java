package com.hjhq.teamface.email;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.email.presenter.EmailActivity3;

/**
 * @author Administrator
 * @date 2018/3/13
 */

public class EmailTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_test_activity);
    }

    public void test(View view) {
        //UIRouter.getInstance().openUri(this, "DDComp://email/email", null);
        CommonUtil.startActivtiy(this, EmailActivity3.class);
    }
}
