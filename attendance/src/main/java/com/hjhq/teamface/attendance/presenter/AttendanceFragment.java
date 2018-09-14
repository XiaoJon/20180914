package com.hjhq.teamface.attendance.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.TodayAttendanceAdapter;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AttendanceFragmentDelegate;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AttendanceFragment extends FragmentPresenter<AttendanceFragmentDelegate, AttendanceModel> {
    private ScheduledExecutorService scheduledExecutorService;
    private Runnable progressRunnable;
    private TodayAttendanceAdapter mTodayAttendanceAdapter;
    private List<String> dataList = new ArrayList<>();

    static AttendanceFragment newInstance(String tag) {
        AttendanceFragment myFragment = new AttendanceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, tag);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();


    }

    @Override
    protected void init() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                viewDelegate.setTimeAndAction(1, DateTimeUtil.longToStr(System.currentTimeMillis(), "HH:mm:ss") + "  下班打卡");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 500, TimeUnit.MILLISECONDS);
        refreshData();
        mTodayAttendanceAdapter = new TodayAttendanceAdapter(dataList);
        View footer = getLayoutInflater().inflate(R.layout.attendance_daka_footer, null);
        mTodayAttendanceAdapter.setFooterView(footer);
        viewDelegate.setAdapter(mTodayAttendanceAdapter);
        viewDelegate.setItemClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        });
    }


    public void refreshData() {
        //state = Constants.REFRESH_STATE;
        getNetData();

    }


    public void getNetData() {
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
    }


    @Override
    protected void bindEvenListener() {
        viewDelegate.get(R.id.action_btn2).setOnClickListener(v -> {
            CommonUtil.startActivtiy(getActivity(), AddWorkScheduleActivity.class);
        });
        super.bindEvenListener();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            refreshData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
