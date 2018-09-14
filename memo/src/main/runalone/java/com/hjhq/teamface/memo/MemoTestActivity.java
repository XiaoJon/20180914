package com.hjhq.teamface.memo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * @author Administrator
 * @date 2018/3/13
 */

public class MemoTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_test_activity);
    }

    public void test(View view) {
        UIRouter.getInstance().openUri(this, "DDComp://login/login", null);
    }
}
