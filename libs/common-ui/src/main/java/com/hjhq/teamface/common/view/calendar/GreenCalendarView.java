package com.hjhq.teamface.common.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.common.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zl
 */
public class GreenCalendarView extends LinearLayout {

    public static final String FORMAT = "yyyy-MM-dd";

    private GreenCalendar calendar;

    private OnDateChangedListener dateChangeListener;

    private Date minDate;


    public GreenCalendarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    /**
     * 设置标记列表 List<String> list = new ArrayList<String>(); java.util.Date d =
     * new java.util.Date(System.currentTimeMillis()); SimpleDateFormat ft = new
     * SimpleDateFormat("yyyy-MM-dd"); list.add(ft.format(d));
     * list.add("2014-10-02"); list.add("2014-10-31");
     *
     * @param list
     */
    public void setMarkList(List<String> list) {
        calendar.addMarks(list, 0, R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 设置未读标记小红点
     *
     * @param list
     */
    public void setNoReadMarkList(List<Long> list) {

        ArrayList<String> date = new ArrayList<String>();
        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                date.add(DateTimeUtil.longToStr(list.get(i),
                        "yyyy-MM-dd HH:mm:ss"));
            }
        }

        calendar.addMarks(date, 0, R.drawable.calendar_bg_tag);
    }

    /**
     * 设置可选最大日期
     *
     * @param date
     */
    public void setMaxSelectDate(Date date) {
        calendar.setMaxDay(date);
    }

    /**
     * 设置可选最大日期
     *
     * @param date
     */
    public void setMinDate(Date date) {
        minDate = date;
        calendar.setMinDate(date);
    }

    /**
     * 格式 yyyy-MM-dd
     *
     * @param date
     */
    public void setMaxSelectDate(String date) {

        SimpleDateFormat ft1 = new SimpleDateFormat(FORMAT);
        // 最大可以选日期
        Date maxDate = null;
        try {
            maxDate = ft1.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            maxDate = new Date();
        }

        calendar.setMaxDay(maxDate);

    }

    public void clearClick() {
        calendar.removeAllBgColor();
    }

    public void clearMarks() {
        calendar.removeAllMarks();
    }

    public OnDateChangedListener getDateChangeListener() {
        return dateChangeListener;
    }

    public void setDateChangeListener(OnDateChangedListener dateChangeListener) {
        this.dateChangeListener = dateChangeListener;
    }

    public void setCheck(Date date) {
        calendar.setCalendarDayBgColor(date, R.drawable.calendar_bg_green_reactangle);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.calendar_gray_lay, null);
        this.addView(view);

        calendar = (GreenCalendar) view.findViewById(R.id.calendar);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) ScreenUtils.getScreenWidth(context);
        view.setLayoutParams(layoutParams);

        // 监听所选中的日期
        calendar.setOnCalendarClickListener(new GreenCalendar.OnCalendarClickListener() {

            @Override
            public void onCalendarClick(int row, int col, String dateFormat) {

                String date[] = dateFormat.split("-");

                int year = Integer.parseInt(date[0]);

                int month = Integer.parseInt(date[1]);

                int day = Integer.parseInt(date[2]);

                if (calendar.getCalendarMonth() - month == 1// 跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();

                } else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();

                } else {
                    if (minDate != null) {//小于最小时间
                        Calendar c = Calendar.getInstance();
                        c.set(year, month - 1, day);
                        if (minDate.getTime() > c.getTimeInMillis()) {
                            return;
                        }
                    }
                    calendar.removeAllBgColor();
                    calendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_bg_green_reactangle);

                    if (null != dateChangeListener) {
                        dateChangeListener.onDateChanged(year, month, day);
                    }

                    // // 最后返回给全局 date
                    // date = dateFormat;
                    // Log.e("Date", date);
                    // Toast.makeText(getContext(), "可选" + date,
                    // Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCalendarNorClick() {
                // Toast.makeText(getContext(), "不可选" + date,
                // Toast.LENGTH_SHORT)
                // .show();
            }
        });

        // 监听当前月份
        calendar.setOnCalendarDateChangedListener(new GreenCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                if (null != dateChangeListener) {
                    dateChangeListener.onMonthChanged(year, month);
                }

            }
        });

    }

    /**
     * 根据具体的某年某月，展示一个日历
     *
     * @param year
     * @param month
     */
    public void showCalendar(int year, int month) {
        calendar.showCalendar(year, month);
    }

    public GreenCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public GreenCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public void setThisday(Date thisday) {
        calendar.setThisday(thisday);
    }

    /**
     * ondateChange接口回调
     */
    public interface OnDateChangedListener {
        void onDateChanged(int year, int month, int day);

        void onMonthChanged(int year, int month);
    }

}
