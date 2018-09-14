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
@RouteNode(path = "/view_attendance_member", desc = "打卡人数")
public class AttendanceNumActivity extends ActivityPresenter<ViewAttendanceNumDelegate, AttendanceModel> implements View.OnClickListener {
    List<Fragment> mFragmentList = new ArrayList<>(2);
    String[] titleArray = new String[]{"打卡人数(12)", "未打卡人数(2)"};

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        mFragmentList.add(AttendanceDataFragment.newInstance(1));
        mFragmentList.add(AttendanceDataFragment.newInstance(2));
        viewDelegate.initNavigator(titleArray, mFragmentList);
        viewDelegate.setTitle(R.string.attendance_view_member_num);
        viewDelegate.setRightMenuTexts("2018.09.12");
    }


    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
    }


    @Override
    public void onClick(View v) {

    }
}
