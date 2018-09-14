package com.hjhq.teamface.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hjhq.teamface.attendance.presenter.AttendanceActivity;
import com.hjhq.teamface.common.utils.CommonUtil;

/**
 * @author Administrator
 * @date 2018/3/13
 */

public class AttendanceTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_test_activity);
    }

    public void test(View view) {
        CommonUtil.startActivtiy(this, AttendanceActivity.class);
        //UIRouter.getInstance().openUri(this, "DDComp://login/loin", null);
    }
}
