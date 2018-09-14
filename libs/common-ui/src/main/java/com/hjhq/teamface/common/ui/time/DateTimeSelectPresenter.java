package com.hjhq.teamface.common.ui.time;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.Gravity;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentAdapter;
import com.hjhq.teamface.common.CommonModel;
import com.hjhq.teamface.common.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 时间日期选择
 *
 * @author Administrator
 * @date 2017/12/19
 */

public class DateTimeSelectPresenter extends ActivityPresenter<DateTimeSelectDelegate, CommonModel> {
    public final static String FORMAT = "format";
    public final static String CALENDAR = "calendar";

    public static final String YYYY = "yyyy";//年
    public static final String YYYY_MM = "yyyy-MM";//年月
    public static final String YYYY_MM_DD = "yyyy-MM-dd";//年月日
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";//年月日时
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";//年月日时分
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";//年月日时分秒
    //时间格式
    private String format = YYYY_MM_DD;

    public static final int VISIBLE_ITEMS = 7;
    public static final int MAX_YEAR = 2050;
    public static final int MIN_YEAR = 1970;

    private Calendar mCalendar;
    List<Fragment> fragments = new ArrayList<>();
    private DateCalendarFragment dateCalendarFragment;
    private TimeWheelFragment timeWheelFragment;

    private long minTimeMillis;
    private long maxTimeMillis;
    private String remind = "";


    public interface OnDateSelectListener {
        void selectDate(Calendar calendar);
    }

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            format = getIntent().getStringExtra(FORMAT);
            mCalendar = (Calendar) getIntent().getSerializableExtra(CALENDAR);
            minTimeMillis = getIntent().getLongExtra(Constants.DATA_TAG1, 0L);
            remind = getIntent().getStringExtra(Constants.DATA_TAG2);
            maxTimeMillis = getIntent().getLongExtra(Constants.DATA_TAG3, 0L);
        }
    }

    @Override
    public void init() {
        viewDelegate.setFormat(format);
        viewDelegate.setCalendar(mCalendar);
        if (isDate()) {
            viewDelegate.showDateLayout();
            switch (format) {
                case YYYY_MM_DD:
                    viewDelegate.initDay();
                case YYYY_MM:
                    viewDelegate.initMonth();
                case YYYY:
                    viewDelegate.initYear();
                    break;
            }
        } else {
            viewDelegate.showTimeLayout();
            initCalendar();
        }
    }

    /**
     * 判断是否是日期
     *
     * @return
     */
    private boolean isDate() {
        switch (format) {
            case YYYY_MM_DD_HH_MM_SS:
            case YYYY_MM_DD_HH_MM:
            case YYYY_MM_DD_HH:
                return false;
            default:
                return true;
        }

    }


    /**
     * 初始化时间
     */
    public void initCalendar() {
        fragments.add(dateCalendarFragment = DateCalendarFragment.newInstance(MAX_YEAR, MIN_YEAR, mCalendar));
        fragments.add(timeWheelFragment = TimeWheelFragment.newInstance(format, mCalendar));
        dateCalendarFragment.setOnDateSelectListener(calendar -> viewDelegate.updateTime(calendar));
        timeWheelFragment.setOnDateSelectListener(calendar -> viewDelegate.updateTime(calendar));

        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewDelegate.setAdapter(mAdapter, commonNavigatorAdapter);
    }


    CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
            colorTransitionPagerTitleView.setNormalColor(ContextCompat.getColor(mContext, R.color.black_17));
            colorTransitionPagerTitleView.setGravity(Gravity.CENTER);
            TextPaint paint = colorTransitionPagerTitleView.getPaint();
            paint.setFakeBoldText(true);
            colorTransitionPagerTitleView.setSelectedColor(ContextCompat.getColor(mContext, R.color.black_17));
            colorTransitionPagerTitleView.setTextSize(14);
            StringBuilder title = new StringBuilder();
            if (index == 0) {
                title.append(mCalendar.get(Calendar.YEAR))
                        .append("年")
                        .append((mCalendar.get(Calendar.MONTH) + 1))
                        .append("月")
                        .append(mCalendar.get(Calendar.DAY_OF_MONTH))
                        .append("日");
            } else {
                switch (format) {
                    case YYYY_MM_DD_HH_MM_SS:
                        title.append(":").append(mCalendar.get(Calendar.SECOND));
                    case YYYY_MM_DD_HH_MM:
                        title.insert(0, mCalendar.get(Calendar.MINUTE)).insert(0, ":");
                    case YYYY_MM_DD_HH:
                        title.insert(0, mCalendar.get(Calendar.HOUR_OF_DAY));
                }
            }

            colorTransitionPagerTitleView.setText(title);
            colorTransitionPagerTitleView.setOnClickListener(v -> viewDelegate.setCurrentItem(index));
            viewDelegate.addTitleView(colorTransitionPagerTitleView);
            return colorTransitionPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            return indicator;
        }
    };

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.get(R.id.tv_date_confirm).setOnClickListener(v -> confirmDate());
        viewDelegate.get(R.id.tv_confirm).setOnClickListener(v -> confirmDate());
        viewDelegate.get(R.id.tv_clean).setOnClickListener(v -> cleanDate());
        viewDelegate.get(R.id.tv_date_clean).setOnClickListener(v -> cleanDate());
    }

    private void cleanDate() {
        setResult(Constants.CLEAR_RESULT_CODE);
        finish();
    }

    /**
     * 确定时间
     */
    private void confirmDate() {
        if (minTimeMillis > 0 && mCalendar.getTimeInMillis() < minTimeMillis) {
            ToastUtils.showToast(mContext, remind + "");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(CALENDAR, mCalendar);
        setResult(RESULT_OK, intent);
        finish();
    }
}
