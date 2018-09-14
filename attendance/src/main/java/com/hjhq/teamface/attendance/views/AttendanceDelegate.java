package com.hjhq.teamface.attendance.views;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AttendanceDelegate extends AppDelegate {


    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_main_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.attendance_daka);
        setRightMenuIcons(R.drawable.attendance_range_icon);
        showMenu();
    }

}
