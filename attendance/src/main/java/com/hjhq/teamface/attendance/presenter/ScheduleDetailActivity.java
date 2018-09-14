package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AttendanceCalendarAdapter;
import com.hjhq.teamface.attendance.adapter.TodayAttendanceAdapter;
import com.hjhq.teamface.attendance.bean.AttendanceDataBean;
import com.hjhq.teamface.attendance.bean.AttendanceDetailListBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.ViewMonthlyDataDelegate;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/view_monthly_schedule", desc = "排班详情")
public class ScheduleDetailActivity extends ActivityPresenter<ViewMonthlyDataDelegate, AttendanceModel> implements View.OnClickListener {
    AttendanceCalendarAdapter mAdapter;
    TodayAttendanceAdapter mAdaapter2;
    List<AttendanceDataBean> dataList = new ArrayList<>();
    int currnetMonth;
    int currentDay;
    int dayOfWeek;
    int dayNumOfMonth;
    Member mMember;
    Calendar monthCalendar;
    Calendar currentCalendar;

    @Override
    public void init() {
        mMember = new Member();
        mMember.setName(SPHelper.getUserName());
        mMember.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
        viewDelegate.get(R.id.rl_header).setVisibility(View.VISIBLE);
        viewDelegate.get(R.id.tv_detail).setVisibility(View.VISIBLE);
        viewDelegate.get(R.id.rl_data).setVisibility(View.GONE);
        viewDelegate.setSubTitle("考勤班次");
        initCalendar();

    }

    /**
     * 初始化日期
     */
    private void initCalendar() {
        currentCalendar = Calendar.getInstance();
        monthCalendar = Calendar.getInstance();
        monthCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        viewDelegate.setMenuTexts(currentCalendar.get(Calendar.YEAR) + "." + (currentCalendar.get(Calendar.MONTH) + 1));
        monthCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
        monthCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        formatData();
    }

    /**
     * 格式数据
     */
    private void formatData() {
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        dayNumOfMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("一周的第几天1", dayNumOfMonth + "");
        dayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK);
        Log.e("一周的第几天2", dayOfWeek + "");
        if (dayOfWeek == 1) {
            dayOfWeek = dayOfWeek + 6;
        } else {
            dayOfWeek = dayOfWeek - 1;
        }
        Log.e("一周的第几天3", dayOfWeek + "");
        viewDelegate.setMenuTexts(monthCalendar.get(Calendar.YEAR) + "." + (monthCalendar.get(Calendar.MONTH) + 1));

        currentDay = monthCalendar.get(Calendar.DAY_OF_MONTH);
        currnetMonth = monthCalendar.get(Calendar.MONTH);

        int dayNum = monthCalendar.get(Calendar.DAY_OF_MONTH);
        int monthDay = DateTimeUtil.getCurrentMonthDay(monthCalendar);
        dataList.clear();
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
            bean.setDate((i + 1) + "");
            bean.setSelected(false);
            bean.setState(i % 3);
            if (isSameMonth()) {
                bean.setToday(i == dayNum);
            } else {
                bean.setToday(false);
            }
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
                viewDelegate.setDetailTexts((currnetMonth + 1) + "月" + dataList.get(position).getDate() + "日");
                mAdapter.notifyDataSetChanged();
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        });

    }

    /**
     * 判断是否是当前月
     *
     * @return
     */
    private boolean isSameMonth() {
        return (currentCalendar.get(Calendar.YEAR) == monthCalendar.get(Calendar.YEAR)
                && currentCalendar.get(Calendar.MONTH) == monthCalendar.get(Calendar.MONTH));
    }


    @Override
    protected void bindEvenListener() {
        //选择人员
        viewDelegate.get(R.id.rl_header).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList<Member> list = new ArrayList<Member>();
            mMember.setCheck(true);
            list.add(mMember);
            bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, list);
            CommonUtil.startActivtiyForResult(ScheduleDetailActivity.this,
                    SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        });
        //选择月份
        viewDelegate.get(R.id.tv_month).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, monthCalendar);
            bundle.putString(DateTimeSelectPresenter.FORMAT, DateTimeSelectPresenter.YYYY_MM);
            CommonUtil.startActivtiyForResult(mContext, DateTimeSelectPresenter.class, Constants.REQUEST_CODE2, bundle);
        });
        //返回
        viewDelegate.get(R.id.rl_back).setOnClickListener(v -> {
            finish();
        });

        super.bindEvenListener();
    }

    /**
     * 获取网络数据
     */
    protected void getNetData() {
        model.getAttendanceDetail(mContext, monthCalendar.getTimeInMillis(), mMember.getId(),
                new ProgressSubscriber<AttendanceDetailListBean>(mContext, true) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(AttendanceDetailListBean bean) {
                        super.onNext(bean);

                    }

                });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                if (data != null) {
                    List<Member> list = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                    if (list != null && list.size() > 0) {
                        mMember = list.get(0);
                        viewDelegate.setMember(mMember);
                        getNetData();
                    }
                }


                break;
            case Constants.REQUEST_CODE2:
                //选择月份
                if (data != null) {
                    Log.e("一周的第几天--前", monthCalendar.get(Calendar.MONTH) + 1 + "月");
                    monthCalendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                    Log.e("一周的第几天--后", monthCalendar.get(Calendar.MONTH) + 1 + "月");
                    formatData();
                    getNetData();
                }

                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
