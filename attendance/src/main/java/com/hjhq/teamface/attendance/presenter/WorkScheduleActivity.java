package com.hjhq.teamface.attendance.presenter;

import android.view.View;

import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AddWorkScheduleDelegate;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/add_work_schedule", desc = "添加班次")
public class WorkScheduleActivity extends ActivityPresenter<AddWorkScheduleDelegate, AttendanceModel> implements View.OnClickListener {



    @Override
    public void init() {
        initView();
    }

    private void initView() {



    }


    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
    }


    @Override
    public void onClick(View v) {

    }
}
