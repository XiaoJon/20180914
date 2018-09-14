package com.hjhq.teamface.common.ui.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.common.R;

import java.util.Calendar;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;


/**
 * 时间
 *
 * @author Administrator
 * @date 2017/12/18
 */

public class TimeWheelFragment extends Fragment {
    public final static String FORMAT = "format";
    public final static String CALENDAR = "calendar";


    private View rootView;
    private String format;
    private Calendar calendar;
    private DateTimeSelectPresenter.OnDateSelectListener onDateSelectListener;
    private int visibleItems = 7;

    public static TimeWheelFragment newInstance(String format, Calendar calendar) {

        TimeWheelFragment f = new TimeWheelFragment();
        Bundle arguments = new Bundle();

        arguments.putString(FORMAT, format);
        arguments.putSerializable(CALENDAR, calendar);
        f.setArguments(arguments);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            format = bundle.getString(FORMAT);
            calendar = (Calendar) bundle.getSerializable(CALENDAR);
        } else {
            format = DateTimeSelectPresenter.YYYY_MM_DD_HH_MM;
            calendar = Calendar.getInstance();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.time_wheel_fragment, container, false);
        init();
        return rootView;
    }

    private void init() {
        visibleItems = (int) (ScreenUtils.getScreenWidth(getContext()) / DeviceUtils.dpToPixel(getContext(), 35));
        switch (format) {
            case DateTimeSelectPresenter.YYYY_MM_DD_HH_MM_SS:
                initSecond();
            case DateTimeSelectPresenter.YYYY_MM_DD_HH_MM:
                initMinute();
            case DateTimeSelectPresenter.YYYY_MM_DD_HH:
                initHour();
                break;
        }
    }

    /**
     * 初始化 时
     */
    private void initHour() {
        WheelView mHourWheelView = (WheelView) rootView.findViewById(R.id.hour_wv);
        mHourWheelView.setVisibility(View.VISIBLE);

        NumericWheelAdapter mHourAdapter = new NumericWheelAdapter(getContext(), 0, 23, "%02d");
        mHourAdapter.setItemResource(R.layout.item_wheel_text);
        mHourAdapter.setItemTextResource(R.id.tempValue);
        mHourAdapter.setLabel("时");

        mHourWheelView.setVisibleItems(visibleItems);
        mHourWheelView.setViewAdapter(mHourAdapter);
        mHourWheelView.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
        mHourWheelView.setCyclic(true);//是否可循环滑动

        mHourWheelView.addChangingListener((wheel, oldValue, newValue) -> {
            if (onDateSelectListener != null) {
                calendar.set(Calendar.HOUR_OF_DAY, newValue);
                onDateSelectListener.selectDate(calendar);
            }
        });
    }

    /**
     * 初始化分钟
     */
    private void initMinute() {
        WheelView mMinuteWheelView = (WheelView) rootView.findViewById(R.id.minute_wv);
        mMinuteWheelView.setVisibility(View.VISIBLE);

        NumericWheelAdapter mMinuteAdapter = new NumericWheelAdapter(getContext(), 0, 59, "%02d");
        mMinuteAdapter.setItemResource(R.layout.item_wheel_text);
        mMinuteAdapter.setItemTextResource(R.id.tempValue);
        mMinuteAdapter.setLabel("分");

        mMinuteWheelView.setVisibleItems(visibleItems);
        mMinuteWheelView.setViewAdapter(mMinuteAdapter);
        mMinuteWheelView.setCurrentItem(calendar.get(Calendar.MINUTE));
        mMinuteWheelView.setCyclic(true);//是否可循环滑动
        mMinuteWheelView.addChangingListener((wheel, oldValue, newValue) -> {
            if (onDateSelectListener != null) {
                calendar.set(Calendar.MINUTE, newValue);
                onDateSelectListener.selectDate(calendar);
            }
        });
    }

    /**
     * 初始化秒
     */
    private void initSecond() {
        WheelView mSecondWheelView = (WheelView) rootView.findViewById(R.id.second_wv);
        mSecondWheelView.setVisibility(View.VISIBLE);

        NumericWheelAdapter mSecondAdapter = new NumericWheelAdapter(getContext(), 0, 59, "%02d");
        mSecondAdapter.setItemResource(R.layout.item_wheel_text);
        mSecondAdapter.setItemTextResource(R.id.tempValue);
        mSecondAdapter.setLabel("秒");

        mSecondWheelView.setVisibleItems(visibleItems);
        mSecondWheelView.setViewAdapter(mSecondAdapter);
        mSecondWheelView.setCurrentItem(calendar.get(Calendar.SECOND));
        mSecondWheelView.setCyclic(true);//是否可循环滑动

        mSecondWheelView.addChangingListener((wheel, oldValue, newValue) -> {
            if (onDateSelectListener != null) {
                calendar.set(Calendar.SECOND, newValue);
                onDateSelectListener.selectDate(calendar);
            }
        });
    }

    public void setOnDateSelectListener(DateTimeSelectPresenter.OnDateSelectListener onDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener;
    }
}
