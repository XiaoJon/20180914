package com.hjhq.teamface.common.ui.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.view.calendar.GreenCalendarView;

import java.util.Calendar;
import java.util.Date;


/**
 * @author Administrator
 * @date 2017/12/18
 */

public class DateCalendarFragment extends Fragment {
    public final static String MIN_YEAR = "minYear";
    public final static String MAX_YEAR = "maxYear";
    public final static String CALENDAR = "calendar";

    private View rootView;
    private int minYear;
    private int maxYear;
    private DateTimeSelectPresenter.OnDateSelectListener onDateSelectListener;
    private Calendar mCalendar;

    public static DateCalendarFragment newInstance(int maxYear, int minYear, Calendar calendar) {

        DateCalendarFragment f = new DateCalendarFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(MAX_YEAR, maxYear);
        arguments.putInt(MIN_YEAR, minYear);
        arguments.putSerializable(CALENDAR, calendar);

        f.setArguments(arguments);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            minYear = bundle.getInt(MIN_YEAR);
            maxYear = bundle.getInt(MAX_YEAR);
            mCalendar = (Calendar) bundle.getSerializable(CALENDAR);
        } else {
            minYear = 1970;
            maxYear = 2050;
            mCalendar = Calendar.getInstance();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.date_calendar_fragment, container, false);
        initView();
        return rootView;
    }

    private void initView() {

        GreenCalendarView calendarView = (GreenCalendarView) rootView.findViewById(R.id.calendar_view);
        calendarView.setCheck(mCalendar.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, maxYear);
        calendarView.setMaxSelectDate(calendar.getTime());
        calendar.set(Calendar.YEAR, minYear);
        calendarView.setMinDate(calendar.getTime());

        calendarView.setDateChangeListener(new GreenCalendarView.OnDateChangedListener() {
            @Override
            public void onDateChanged(int year, int month, int day) {
                long selectDate = DateTimeUtil.strToLong(year + "-" + month + "-" + day, "yyyy-MM-dd") + 2 * 60 * 60 * 1000;
                Log.e("选择selectDate", "选择selectDate" + DateTimeUtil.fromTime(selectDate) + selectDate);

                calendarView.clearClick();
                calendarView.setCheck(new Date(selectDate));

                if (onDateSelectListener != null) {
                    mCalendar.set(year, month - 1, day);
                    onDateSelectListener.selectDate(mCalendar);
                }
            }

            @Override
            public void onMonthChanged(int year, int month) {
            }
        });
    }

    public void setOnDateSelectListener(DateTimeSelectPresenter.OnDateSelectListener onDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener;
    }
}
