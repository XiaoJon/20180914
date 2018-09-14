package com.hjhq.teamface.attendance.presenter;

import android.support.v4.app.Fragment;
import android.view.View;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.ViewAttendanceNumDelegate;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/view_attendance_range", desc = "排行榜")
public class AttendanceRangeActivity extends ActivityPresenter<ViewAttendanceNumDelegate, AttendanceModel> implements View.OnClickListener {
    List<Fragment> mFragmentList = new ArrayList<>(2);
    String[] titleArray = new String[]{"早到榜", "勤勉榜", "迟到榜"};

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        mFragmentList.add(AttendanceDataFragment.newInstance(1));
        mFragmentList.add(AttendanceDataFragment.newInstance(2));
        mFragmentList.add(AttendanceDataFragment.newInstance(3));
        viewDelegate.initNavigator(titleArray, mFragmentList);
        viewDelegate.setTitle(R.string.attendance_view_range_title);
        viewDelegate.setRightMenuIcons(R.drawable.attendance_group_icon);

    }


    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
    }


    @Override
    public void onClick(View v) {

    }
}
