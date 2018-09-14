package com.hjhq.teamface.attendance.presenter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AttendanceCalendarAdapter;
import com.hjhq.teamface.attendance.adapter.TodayAttendanceAdapter;
import com.hjhq.teamface.attendance.bean.AttendanceDataBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.ViewMonthlyDataDelegate;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/view_monthly_data", desc = "打卡月历")
public class MonthlyDataActivity extends ActivityPresenter<ViewMonthlyDataDelegate, AttendanceModel> implements View.OnClickListener {
    AttendanceCalendarAdapter mAdapter;
    TodayAttendanceAdapter mAdaapter2;
    List<AttendanceDataBean> dataList = new ArrayList<>();
    int currnetMonth;
    int currentDay;
    int dayOfWeek;
    int dayNumOfMonth;

    @Override
    public void init() {
        viewDelegate.setTitle(R.string.attendance_monthly_data_title);
        viewDelegate.get(R.id.rl_header).setVisibility(View.GONE);
        viewDelegate.get(R.id.tv_detail).setVisibility(View.GONE);
        viewDelegate.get(R.id.rl_data).setVisibility(View.VISIBLE);
        viewDelegate.setSubTitle("考勤组:产品中心组");
        initCalendar();
        initView();

    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        //calendar.set(2018,5,13);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayNumOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Log.e("一周的第几天", calendar.get(Calendar.DAY_OF_WEEK) + "");

        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 1) {
            dayOfWeek = dayOfWeek + 6;
        } else {
            dayOfWeek = dayOfWeek - 1;
        }
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currnetMonth = calendar.get(Calendar.MONTH) + 1;
    }

    private void initView() {
        final Calendar calendar = Calendar.getInstance();
        int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
        int monthDay = DateTimeUtil.getCurrentMonthDay(calendar);
        for (int i = 0; i < dayOfWeek - 1; i++) {
            AttendanceDataBean bean = new AttendanceDataBean();
            bean.setDate("");
            bean.setSelected(false);
            bean.setState(4);
            bean.setToday(false);
            dataList.add(bean);

        }
        for (int i = 0; i < monthDay; i++) {
            AttendanceDataBean bean = new AttendanceDataBean();
            bean.setDate(i + 1 + "");
            bean.setSelected(false);
            bean.setState(i % 3);
            bean.setToday(i == dayNum);
            dataList.add(bean);
        }
        mAdapter = new AttendanceCalendarAdapter(dataList);
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        mAdaapter2 = new TodayAttendanceAdapter(list);
        viewDelegate.setAdapter2(mAdaapter2);
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setItemClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (dataList.get(position).getState() == 4) {
                    return;
                }
                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setSelected(false);
                }
                dataList.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        });

    }


    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
    }


    @Override
    public void onClick(View v) {

    }
}
