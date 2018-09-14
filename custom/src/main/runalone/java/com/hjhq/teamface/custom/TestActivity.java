package com.hjhq.teamface.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hjhq.teamface.basis.AppConst;
import com.hjhq.teamface.basis.util.file.SPUtils;
import com.luojilab.component.componentlib.router.ui.UIRouter;


/**
 * @author Administrator
 * @date 2018/3/13
 */

public class TestActivity extends AppCompatActivity {
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwic3ViIjoiMSIsImF1ZCI6IjIiLCJpc3MiOiIxMDAwMSIsImlhdCI6MTUyMjMxOTE3OH0.ZSS0MLw4FVzWG2iTDTX_AsMxGx9-A4o49quSVEKl6O8";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_test_activity);
    }

    public void test(View view) {
        SPUtils.setString(this, AppConst.TOKEN_KEY, TOKEN);
        UIRouter.getInstance().openUri(this, "DDComp://project/appModule", null);
    }
}
