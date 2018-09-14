package com.hjhq.teamface.common.ui.time;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

/**
 * Created by lx on 2017/9/4.
 */

public class DateTimeSelectDelegate extends AppDelegate {
    private View timeView;
    private View dateView;
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;
    private String format;
    private Calendar mCalendar;

    private List<ColorTransitionPagerTitleView> dateViews = new ArrayList<>(2);
    private int nowMonth;

    @Override
    public int getRootLayoutId() {
        return R.layout.date_time_select_activity;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        dateView = get(R.id.layout_date);
        timeView = get(R.id.layout_time);
        mViewPager = get(R.id.view_pager);
        mMagicIndicator = get(R.id.magic_indicator);
    }


    /**
     * 初始化年
     */
    public void initYear() {
        WheelView myearWheelView = get(R.id.year_wv);
        myearWheelView.setVisibility(View.VISIBLE);

        NumericWheelAdapter mYearAdapter = new NumericWheelAdapter(mContext, DateTimeSelectPresenter.MIN_YEAR, DateTimeSelectPresenter.MAX_YEAR);
        mYearAdapter.setItemResource(R.layout.item_wheel_text);
        mYearAdapter.setItemTextResource(R.id.tempValue);
        mYearAdapter.setLabel("年");

        myearWheelView.setVisibleItems(DateTimeSelectPresenter.VISIBLE_ITEMS);
        myearWheelView.setViewAdapter(mYearAdapter);
        myearWheelView.setCurrentItem(mCalendar.get(Calendar.YEAR) - DateTimeSelectPresenter.MIN_YEAR);
        myearWheelView.setCyclic(true);//是否可循环滑动
        myearWheelView.addChangingListener((wheel, oldValue, newValue) -> {
            mCalendar.set(Calendar.YEAR, newValue + DateTimeSelectPresenter.MIN_YEAR);
            if (format.equals(DateTimeSelectPresenter.YYYY_MM_DD) || format.equals(DateTimeSelectPresenter.YYYY_MM_DD_HH) || format.equals(DateTimeSelectPresenter.YYYY_MM_DD_HH_MM) || format.equals(DateTimeSelectPresenter.YYYY_MM_DD_HH_MM_SS)) {
                initDay();
            }
        });
    }


    /**
     * 月份
     */
    public void initMonth() {
        WheelView mMonthWheelView = get(R.id.month_wv);
        mMonthWheelView.setVisibility(View.VISIBLE);

        NumericWheelAdapter mMonthAdpter = new NumericWheelAdapter(mContext, 1, 12, "%02d");
        mMonthAdpter.setItemResource(R.layout.item_wheel_text);
        mMonthAdpter.setItemTextResource(R.id.tempValue);
        mMonthAdpter.setLabel("月");

        mMonthWheelView.setVisibleItems(DateTimeSelectPresenter.VISIBLE_ITEMS);
        mMonthWheelView.setViewAdapter(mMonthAdpter);
        mMonthWheelView.setCurrentItem(mCalendar.get(Calendar.MONTH));
        mMonthWheelView.setCyclic(true);
        mMonthWheelView.addChangingListener((wheel, oldValue, newValue) -> {
            nowMonth = newValue + 1;
            mCalendar.set(Calendar.MONTH, newValue);
            if (format.equals(DateTimeSelectPresenter.YYYY_MM_DD) || format.equals(DateTimeSelectPresenter.YYYY_MM_DD_HH) || format.equals(DateTimeSelectPresenter.YYYY_MM_DD_HH_MM) || format.equals(DateTimeSelectPresenter.YYYY_MM_DD_HH_MM_SS)) {
                initDay();
            }
        });
    }

    /**
     * 初始化日
     */
    public void initDay() {
        WheelView mDayWheelView = get(R.id.date_wv);
        if (mDayWheelView.getVisibility() == View.GONE) {
            mDayWheelView.addChangingListener((wheel, oldValue, newValue) -> {
                mCalendar.set(Calendar.DAY_OF_MONTH, newValue + 1);
            });
        }
        if (nowMonth == 0) {
            nowMonth = mCalendar.get(Calendar.MONTH) + 1;
        }
        NumericWheelAdapter mDayAdapter = new NumericWheelAdapter(mContext, 1, getDay(mCalendar.get(Calendar.YEAR), nowMonth), "%02d");
        mDayAdapter.setItemResource(R.layout.item_wheel_text);
        mDayAdapter.setItemTextResource(R.id.tempValue);
        mDayAdapter.setLabel("日");

        mDayWheelView.setVisibility(View.VISIBLE);
        mDayWheelView.setVisibleItems(DateTimeSelectPresenter.VISIBLE_ITEMS);
        mDayWheelView.setViewAdapter(mDayAdapter);
        mDayWheelView.setCurrentItem(mCalendar.get(Calendar.DAY_OF_MONTH) - 1);
        mDayWheelView.setCyclic(true);
    }

    /**
     * 根据年月获得 这个月总共有几天
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day;
        boolean flag;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            flag = true;
        } else {
            flag = false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     * 更新时间
     */
    public void updateTime(Calendar calendar) {
        this.mCalendar = calendar;
        ColorTransitionPagerTitleView dateTitleView = dateViews.get(0);
        ColorTransitionPagerTitleView timeTitleView = dateViews.get(1);

        String date = mCalendar.get(Calendar.YEAR) + "年" + (mCalendar.get(Calendar.MONTH) + 1) + "月" + mCalendar.get(Calendar.DAY_OF_MONTH) + "日";
        StringBuilder time = new StringBuilder();
        switch (format) {
            case DateTimeSelectPresenter.YYYY_MM_DD_HH_MM_SS:
                time.append(":").append(mCalendar.get(Calendar.SECOND));
            case DateTimeSelectPresenter.YYYY_MM_DD_HH_MM:
                int minute = mCalendar.get(Calendar.MINUTE);
                time.insert(0, minute < 10 ? "0" + minute : minute).insert(0, ":");
            case DateTimeSelectPresenter.YYYY_MM_DD_HH:
                time.insert(0, mCalendar.get(Calendar.HOUR_OF_DAY));
        }
        Log.e("年月日", mCalendar.get(Calendar.MONTH) + "月" + mCalendar.get(Calendar.DAY_OF_MONTH) + "日");
        TextUtil.setText(dateTitleView, date);
        TextUtil.setText(timeTitleView, time.toString());
    }

    public void showDateLayout() {
        dateView.setVisibility(View.VISIBLE);
        timeView.setVisibility(View.GONE);
    }

    public void showTimeLayout() {
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.height = (int) ScreenUtils.getScreenWidth(mContext);
        mViewPager.setLayoutParams(layoutParams);

        timeView.setVisibility(View.VISIBLE);
        dateView.setVisibility(View.GONE);
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setCalendar(Calendar mCalendar) {
        this.mCalendar = mCalendar;
    }

    public void setAdapter(FragmentAdapter mAdapter, CommonNavigatorAdapter commonNavigatorAdapter) {
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    /**
     * 日期和时间切换
     *
     * @param index
     */
    public void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index);
    }

    /**
     * 添加日期和时间
     *
     * @param titleView
     */
    public void addTitleView(ColorTransitionPagerTitleView titleView) {
        dateViews.add(titleView);
    }
}