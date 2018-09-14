package com.hjhq.teamface.common.view.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.common.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 日历控件
 *
 * @author huangyin
 */
@SuppressWarnings("deprecation")
public class GreenCalendar extends ViewFlipper implements
        GestureDetector.OnGestureListener {

    private Date minDate;
    /**
     * 灰色标记
     **/
    public static final int gray = 1;
    /**
     * 红色标记
     **/
    public static final int red = 2;

    /**
     * 区分标记颜色的字段
     **/
    // private int color = R.drawable.calendar_gray_bg_tag;

    public static final int COLOR_BG_WEEK_TITLE = Color.parseColor("#F2F2F2"); // 星期标题背景颜色
    //    public static final int COLOR_BG_WEEK_TITLE = Color.parseColor("#ffffffff"); // 星期标题背景颜色
    public static final int COLOR_TX_WEEK_TITLE = Color.parseColor("#ff323232"); // 星期标题文字颜色
    public static final int COLOR_TX_THIS_MONTH_DAY = Color
            .parseColor("#aa323232"); // 当前月日历数字颜色
    public static final int COLOR_TX_OTHER_MONTH_DAY = Color
            .parseColor("#ffcccccc"); // 其他月日历数字颜色
    public static final int COLOR_TX_THIS_DAY = Color.parseColor("#ff008000"); // 当天日历数字颜色
    public static final int COLOR_BG_THIS_DAY = Color.parseColor("#ffcccccc"); // 当天日历背景颜色

    // public static final int COLOR_BG_CALENDAR =
    // Color.parseColor("#ffeeeeee"); // 日历背景色
    public static final int COLOR_BG_CALENDAR = Color.TRANSPARENT;// 日历背景色

    private GestureDetector gd; // 手势监听器
    private Animation push_left_in; // 动画-左进
    private Animation push_left_out; // 动画-左出
    private Animation push_right_in; // 动画-右进
    private Animation push_right_out; // 动画-右出
    private Animation zoom_in; // 动画-右出
    private Animation zoom_out; // 动画-右出
    // add
    private Animation push_top_in; // 动画-左进
    private Animation push_top_out; // 动画-左出
    private Animation push_bottom_in; // 动画-右进
    private Animation push_bottom_out; // 动画-右出
    //
    private int ROWS_TOTAL = 6; // 日历的行数
    private int COLS_TOTAL = 7; // 日历的列数
    private String[][] dates = new String[6][7]; // 当前日历日期
    private float tb;

    private OnCalendarClickListener onCalendarClickListener; // 日历翻页回调
    private OnCalendarDateChangedListener onCalendarDateChangedListener; // 日历点击回调

    private String[] weekday = new String[]{"日", "一", "二", "三", "四", "五", "六"}; // 星期标题

    private int calendarYear; // 日历年份
    private int calendarCurrentYear; // 日历年份
    private int calendarMonth; // 日历月份
    private int calendarCurrentMonth; // 日历年份
    private Date thisday = new Date(); // 今天
    private Date calendarday; // 日历这个月第一天(1号)

    private LinearLayout firstCalendar; // 第一个日历
    private LinearLayout secondCalendar; // 第二个日历
    private LinearLayout currentCalendar; // 当前显示的日历
    private Date maxDay = null;
    private Map<String, Integer> marksMap = new HashMap<String, Integer>(); // 储存某个日子被标注(Integer
    // 为bitmap
    // res
    // id)
    private Map<String, Integer> dayBgColorMap = new HashMap<String, Integer>(); // 储存某个日子的背景色

    public GreenCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GreenCalendar(Context context) {
        super(context);
        init();
    }

    private void init() {

        setBackgroundColor(COLOR_BG_CALENDAR);
        // 实例化收拾监听器
        gd = new GestureDetector(this);
        // 初始化日历翻动动画
        push_left_in = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_left_in);
        push_left_out = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_left_out);
        push_right_in = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_right_in);
        push_right_out = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_right_out);
        // add
        push_top_in = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_top_in);
        push_top_out = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_top_out);
        push_bottom_in = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_bottom_in);
        push_bottom_out = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_bottom_out);
        zoom_in = AnimationUtils.loadAnimation(getContext(), R.anim.zoomin);
        zoom_out = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);
        //
        push_left_in.setDuration(400);
        push_left_out.setDuration(400);
        push_right_in.setDuration(400);
        push_right_out.setDuration(400);
        // 初始化第一个日历
        firstCalendar = new LinearLayout(getContext());
        firstCalendar.setOrientation(LinearLayout.VERTICAL);
        firstCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 初始化第二个日历
        secondCalendar = new LinearLayout(getContext());
        secondCalendar.setOrientation(LinearLayout.VERTICAL);
        secondCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 设置默认日历为第一个日历
        currentCalendar = firstCalendar;
        // 加入ViewFlipper
        addView(firstCalendar);
        addView(secondCalendar);
        // 绘制线条框架
        drawFrame(firstCalendar);
        drawFrame(secondCalendar);
        // 设置日历上的日子(1号)
        initCuttentDate();
        getDefaultMaxDate();
        // 填充展示日历
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    private void initCuttentDate() {
        calendarYear = thisday.getYear() + 1900;
        calendarCurrentYear = thisday.getYear() + 1900;
        calendarMonth = thisday.getMonth();
        calendarCurrentMonth = thisday.getMonth();
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
    }

    public void getDefaultMaxDate() {
        maxDay = new Date(System.currentTimeMillis());
    }

    private void drawFrame(LinearLayout oneCalendar) {

        // 添加周的线性布局
        LinearLayout title = new LinearLayout(getContext());
//        title.setBackgroundColor(COLOR_BG_WEEK_TITLE);
        title.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(-1, 0,
                0.5f);
        Resources res = getResources();
        tb = res.getDimension(R.dimen.historyscore_tb);
        layout.setMargins(0, 0, 0, (int) (tb * 1.2));
        title.setLayoutParams(layout);
        title.setGravity(Gravity.CENTER_VERTICAL);
        oneCalendar.addView(title);

        // 添加周末TextView
        for (int i = 0; i < COLS_TOTAL; i++) {
            TextView view = new TextView(getContext());
            view.setGravity(Gravity.CENTER);
            view.setText(weekday[i]);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    DeviceUtils.dpToPixel(getContext(), 16));
            view.setTextColor(COLOR_TX_WEEK_TITLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1));
            title.addView(view);

            //周六周日颜色特殊设置
            if (i == 0 || i == 6) {
                view.setTextColor(ContextCompat.getColor(getContext(), R.color.main_green));
            }
        }

        // 添加日期布局
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 7f));
        oneCalendar.addView(content);

        // 添加日期TextView
        for (int i = 0; i < ROWS_TOTAL; i++) {
            //添加每一个行布局
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 0, 1));
            content.addView(row);
            // 绘制日历上的列
            for (int j = 0; j < COLS_TOTAL; j++) {
                //绘制每一个行布局的格子
                final RelativeLayout col = new RelativeLayout(getContext());
                col.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LayoutParams.MATCH_PARENT, 1));
                // col.setBackgroundResource(R.drawable.calendar_day_bg);
                row.addView(col);
                // 给每一个日子加上监听
                // addClicker(col);
            }
        }
    }

    public void addClicker(RelativeLayout col) {
        col.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = (ViewGroup) v.getParent();
                int row2 = 0, col2 = 0;

                // 获取列坐标
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (v.equals(parent.getChildAt(i))) {
                        col2 = i;
                        break;
                    }
                }
                // 获取行坐标
                ViewGroup pparent = (ViewGroup) parent.getParent();
                for (int i = 0; i < pparent.getChildCount(); i++) {
                    if (parent.equals(pparent.getChildAt(i))) {
                        row2 = i;
                        break;
                    }
                }
                if (onCalendarClickListener != null) {
                    onCalendarClickListener.onCalendarClick(row2, col2,
                            dates[row2][col2]);
                }
            }
        });
    }

    /**
     * 填充日历(包含日期、标记、背景等)
     */
    private void setCalendarDate(int color) {
        // 根据日历的日子获取这一天是星期几
        int weekday = calendarday.getDay();
        // 每个月第一天
        int firstDay = 1;
        // 每个月中间号,根据循环会自动++
        int day = firstDay;
        // 每个月的最后一天
        int lastDay = getDateNum(calendarday.getYear(), calendarday.getMonth());
        // 下个月第一天
        int nextMonthDay = 1;
        int lastMonthDay = 1;

        // 填充每一个空格
        for (int i = 0; i < ROWS_TOTAL; i++) {
            for (int j = 0; j < COLS_TOTAL; j++) {
                // 这个月第一天不是礼拜天,则需要绘制上个月的剩余几天
                if (i == 0 && j == 0 && weekday != 0) {
                    int year = 0;
                    int month = 0;
                    int lastMonthDays = 0;
                    // 如果这个月是1月，上一个月就是去年的12月
                    if (calendarday.getMonth() == 0) {
                        year = calendarday.getYear() - 1;
                        month = Calendar.DECEMBER;
                    } else {
                        year = calendarday.getYear();
                        month = calendarday.getMonth() - 1;
                    }
                    // 上个月的最后一天是几号
                    lastMonthDays = getDateNum(year, month);
                    // 第一个格子展示的是几号
                    int firstShowDay = lastMonthDays - weekday + 1;
                    // 上月
                    for (int k = 0; k < weekday; k++) {
                        lastMonthDay = firstShowDay + k;
                        RelativeLayout group = getDateView(0, k);
                        group.setGravity(Gravity.CENTER);
                        TextView view = null;
                        if (group.getChildCount() > 0) {
                            view = (TextView) group.getChildAt(0);
                        } else {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    -1, -1);
                            view = new TextView(getContext());
                            view.setLayoutParams(params);
                            view.setGravity(Gravity.CENTER);
                            group.addView(view);
                        }
                        view.setText(Integer.toString(lastMonthDay));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                DeviceUtils.dpToPixel(getContext(), 16));
                        view.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        dates[0][k] = format(new Date(year, month, lastMonthDay));
                        // 设置日期背景色
                        Date d = new Date(year, month, lastMonthDay);
                        if (d.getTime() > maxDay.getTime()) {
                            view.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                            group.setOnClickListener(null);
                        } else {
                            addClicker(group);
                            if (dayBgColorMap.get(dates[0][k]) != null) {
                            } else {
                                view.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                        // 设置标记
                        setMarker(group, i, j, color);
                    }
                    j = weekday - 1;
                    // 这个月第一天是礼拜天，不用绘制上个月的日期，直接绘制这个月的日期
                } else {
                    RelativeLayout group = getDateView(i, j);
                    group.setGravity(Gravity.CENTER);
                    TextView view = null;
                    if (group.getChildCount() > 0) {
                        view = (TextView) group.getChildAt(0);
                    } else {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                -1, -1);
                        view = new TextView(getContext());
                        view.setLayoutParams(params);
                        view.setGravity(Gravity.CENTER);
                        group.addView(view);
                    }

                    // 本月
                    if (day <= lastDay) {
                        dates[i][j] = format(new Date(calendarday.getYear(),
                                calendarday.getMonth(), day));
                        view.setText(Integer.toString(day));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                DeviceUtils.dpToPixel(getContext(), 16));
                        // 当天
                        if (thisday.getDate() == day
                                && thisday.getMonth() == calendarday.getMonth()
                                && thisday.getYear() == calendarday.getYear()) {
                            view.setTextColor(COLOR_TX_WEEK_TITLE);
                            view.setBackgroundColor(Color.TRANSPARENT);
                        } else {
                            view.setTextColor(COLOR_TX_THIS_MONTH_DAY);
                            view.setBackgroundColor(Color.TRANSPARENT);
                        }
                        // 上面首先设置了一下默认的"当天"背景色，当有特殊需求时，才给当日填充背景色
                        // 设置日期背景色
                        //
                        Date d = new Date(calendarday.getYear(),
                                calendarday.getMonth(), day);
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                        if (d.getTime() > maxDay.getTime()) {
                            view.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                            group.setOnClickListener(null);
                        } else {
                            addClicker(group);
                            if (dayBgColorMap.get(dates[i][j]) != null) {
                                view.setTextColor(Color.WHITE);
                                view.setBackgroundResource(dayBgColorMap
                                        .get(dates[i][j]));
                            }
                        }
                        // 设置标记
                        setMarker(group, i, j, color);
                        day++;
                        // 下个月
                    } else {
                        if (calendarday.getMonth() == Calendar.DECEMBER) {
                            dates[i][j] = format(new Date(
                                    calendarday.getYear() + 1,
                                    Calendar.JANUARY, nextMonthDay));
                        } else {
                            dates[i][j] = format(new Date(
                                    calendarday.getYear(),
                                    calendarday.getMonth() + 1, nextMonthDay));
                        }
                        view.setText(Integer.toString(nextMonthDay));
                        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                DeviceUtils.dpToPixel(getContext(), 16));
                        view.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        // 设置日期背景色
                        if (dayBgColorMap.get(dates[i][j]) != null) {
                        } else {
                            view.setBackgroundColor(Color.TRANSPARENT);
                        }
                        // 设置标记
                        setMarker(group, i, j, color);
                        nextMonthDay++;
                    }

                    if (minDate != null) {
                        if (DateTimeUtil.getTimeByStringFromat(DateTimeUtil.DateSdf, DateTimeUtil.dateToString(minDate), dates[i][j]) == 1) {
                            view.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        }
                    }

                }
            }
        }
    }

    public void setMinDate(Date date) {
        minDate = date;
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * onClick接口回调
     */
    public interface OnCalendarClickListener {
        void onCalendarClick(int row, int col, String dateFormat);

        void onCalendarNorClick();
    }

    /**
     * ondateChange接口回调
     */
    public interface OnCalendarDateChangedListener {
        void onCalendarDateChanged(int year, int month);
    }

    /**
     * 根据具体的某年某月，展示一个日历
     *
     * @param year
     * @param month
     */
    public void showCalendar(int year, int month) {
        calendarYear = year;
        calendarMonth = month - 1;
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 根据具体的某年某月，展示一个日历
     */
    public void showCalendar(Date date) {
        calendarday = date;
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 根据当前月，展示一个日历
     */
    public void showCalendar() {
        Date now = new Date();
        calendarYear = now.getYear() + 1900;
        calendarMonth = now.getMonth();
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 下一月日历
     */
    public synchronized void nextMonth() {
        // 改变日历上下顺序
        if (currentCalendar == firstCalendar) {
            currentCalendar = secondCalendar;
        } else {
            currentCalendar = firstCalendar;
        }
        // 设置动画
        setInAnimation(push_left_in);
        setOutAnimation(push_left_out);
        // 改变日历日期
        if (calendarMonth == Calendar.DECEMBER) {
            calendarYear++;
            calendarMonth = Calendar.JANUARY;
        } else {
            calendarMonth++;
        }
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        // 填充日历
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
        // 下翻到下一月
        showNext();
        // 回调
        if (onCalendarDateChangedListener != null) {
            onCalendarDateChangedListener.onCalendarDateChanged(calendarYear,
                    calendarMonth + 1);
        }
    }

    /**
     * 下一月日历
     */
    public synchronized void nextYear(Animation in, Animation out) {
        // 改变日历上下顺序
        if (currentCalendar == firstCalendar) {
            currentCalendar = secondCalendar;
        } else {
            currentCalendar = firstCalendar;
        }
        // 设置动画
        setInAnimation(in);
        setOutAnimation(out);
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        // 填充日历
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
        // 下翻到下一月
        showNext();
        // 回调
        if (onCalendarDateChangedListener != null) {
            onCalendarDateChangedListener.onCalendarDateChanged(calendarYear,
                    calendarMonth + 1);
        }
    }

    public synchronized void toNow() {
        // 改变日历上下顺序
        if (currentCalendar == firstCalendar) {
            currentCalendar = secondCalendar;
        } else {
            currentCalendar = firstCalendar;
        }
        // 设置动画
        // setInAnimation(zoom_in);
        // setOutAnimation(zoom_out);
        calendarYear = calendarCurrentYear;
        calendarMonth = calendarCurrentMonth;
        // // 改变日历日期
        // if (calendarMonth == Calendar.DECEMBER) {
        // calendarYear++;
        // calendarMonth = Calendar.JANUARY;
        // } else {
        // calendarMonth++;
        // }
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        // 填充日历
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
        // 下翻到下一月
        showNext();
        // 回调
        if (onCalendarDateChangedListener != null) {
            onCalendarDateChangedListener.onCalendarDateChanged(calendarYear,
                    calendarMonth + 1);
        }
    }

    /**
     * 上一月日历
     */
    public synchronized void lastMonth() {
        if (currentCalendar == firstCalendar) {
            currentCalendar = secondCalendar;
        } else {
            currentCalendar = firstCalendar;
        }
        setInAnimation(push_right_in);
        setOutAnimation(push_right_out);
        if (calendarMonth == Calendar.JANUARY) {
            calendarYear--;
            calendarMonth = Calendar.DECEMBER;
        } else {
            calendarMonth--;
        }
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
        showPrevious();
        if (onCalendarDateChangedListener != null) {
            onCalendarDateChangedListener.onCalendarDateChanged(calendarYear,
                    calendarMonth + 1);
        }
    }

    /**
     * 获取日历当前年份
     */
    public int getCalendarYear() {
        return calendarday.getYear() + 1900;
    }

    /**
     * 获取日历当前月份
     */
    public int getCalendarMonth() {
        return calendarday.getMonth() + 1;
    }

    /**
     * 在日历上做一个标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMark(Date date, int id) {
        addMark(format(date), id);
    }

    /**
     * 在日历上做一个标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    void addMark(String date, int id) {
        marksMap.put(date, id);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 在日历上做一组标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMarks(Date[] date, int id) {
        for (int i = 0; i < date.length; i++) {
            marksMap.put(format(date[i]), id);
        }
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 在日历上做一组标记
     *
     * @param date 日期
     *             .getDate() bitmap res id
     */
    public void addMarks(List<String> date, int id, int color) {
        for (int i = 0; i < date.size(); i++) {

            // 选中的日期不标记
            if (!checkDate.equals(date.get(i))) {
                marksMap.put(date.get(i), id);
            }

        }
        setCalendarDate(color);
    }

    /**
     * 移除日历上的标记
     */
    public void removeMark(Date date) {
        removeMark(format(date));
    }

    /**
     * 移除日历上的标记
     */
    public void removeMark(String date) {
        marksMap.remove(date);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 移除日历上的所有标记
     */
    public void removeAllMarks() {
        marksMap.clear();
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 设置日历具体某个日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(Date date, int color) {
        setCalendarDayBgColor(format(date), color);
    }

    private String checkDate;

    /**
     * 设置日历具体某个日期的背景色
     *
     * @param date
     * @param color
     */
    void setCalendarDayBgColor(String date, int color) {
        checkDate = date;
        dayBgColorMap.put(date, color);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 设置日历一组日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDaysBgColor(List<String> date, int color) {
        for (int i = 0; i < date.size(); i++) {
            dayBgColorMap.put(date.get(i), color);
        }
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 设置日历一组日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(String[] date, int color) {
        for (int i = 0; i < date.length; i++) {
            dayBgColorMap.put(date[i], color);
        }
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param date
     */
    public void removeCalendarDayBgColor(Date date) {
        removeCalendarDayBgColor(format(date));
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param date
     */
    public void removeCalendarDayBgColor(String date) {
        dayBgColorMap.remove(date);
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 移除日历具体某个日期的背景色
     */
    public void removeAllBgColor() {
        dayBgColorMap.clear();
        setCalendarDate(R.drawable.calendar_gray_bg_tag);
    }

    /**
     * 根据行列号获得包装每一个日子的LinearLayout
     *
     * @param row
     * @param col
     * @return
     */
    public String getDate(int row, int col) {
        return dates[row][col];
    }

    /**
     * 某天是否被标记了
     *
     * @return
     */
    public boolean hasMarked(String date) {
        return marksMap.get(date) == null ? false : true;
    }

    /**
     * 清除所有标记以及背景
     */
    public void clearAll() {
        marksMap.clear();
        dayBgColorMap.clear();
    }

    /***********************************************
     *
     * private methods
     *
     **********************************************/
    // 设置标记
    private void setMarker(RelativeLayout group, int i, int j, int color) {
        int childCount = group.getChildCount();
        if (marksMap.get(dates[i][j]) != null) {
            if (childCount < 2) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (int) (tb * 0.8), (int) (tb * 0.8));

                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                // params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                // 垂直居中
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.setMargins(0, 0, 0, (int) (tb * 0.2));
                // params.setMargins(
                // group.getWidth() / 2 - TimeWheelUtils.px2dip(getContext(), 10),
                // 2, 0, 0);
                ImageView markView = new ImageView(getContext());
                markView.setImageResource(marksMap.get(dates[i][j]));
                markView.setBackgroundResource(color);

                markView.setLayoutParams(params);
                group.addView(markView);
            }
        } else {
            if (childCount > 1) {
                group.removeView(group.getChildAt(1));
            }
        }

    }

    /**
     * 计算某年某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    private int getDateNum(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year + 1900);
        time.set(Calendar.MONTH, month);
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据行列号获得包装每一个日子的LinearLayout
     *
     * @param row
     * @param col
     * @return
     */
    private RelativeLayout getDateView(int row, int col) {
        return (RelativeLayout) ((LinearLayout) ((LinearLayout) currentCalendar
                .getChildAt(1)).getChildAt(row)).getChildAt(col);
    }

    /**
     * 将Date转化成字符串->2013-3-3
     */
    private String format(Date d) {
        return addZero(d.getYear() + 1900, 4) + "-"
                + addZero(d.getMonth() + 1, 2) + "-" + addZero(d.getDate(), 2);
    }

    // 2或4
    private static String addZero(int i, int count) {
        if (count == 2) {
            if (i < 10) {
                return "0" + i;
            }
        } else if (count == 4) {
            if (i < 10) {
                return "000" + i;
            } else if (i < 100 && i > 10) {
                return "00" + i;
            } else if (i < 1000 && i > 100) {
                return "0" + i;
            }
        }
        return "" + i;
    }

    /***********************************************
     *
     * Override methods
     *
     **********************************************/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gd != null) {
            if (gd.onTouchEvent(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gd.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // Left
        if (e1.getX() - e2.getX() > 100) {
            nextMonth();
        }
        // Right
        if (e1.getX() - e2.getX() < -100) {
            lastMonth();
        }

        // 2016-03-15 根据bug:1039 【日程中心】日历控件修改 去掉年份的切换
        // // Up
        // if (e1.getY() - e2.getY() > 100) {
        // calendarYear++;
        // nextYear(push_bottom_in, push_bottom_out);
        // }
        // // Down
        // else if (e1.getY() - e2.getY() < -100) {
        // calendarYear--;
        // nextYear(push_top_in, push_top_out);
        //
        // }

        return false;
    }

    /***********************************************
     *
     * get/set methods
     *
     **********************************************/

    public OnCalendarClickListener getOnCalendarClickListener() {
        return onCalendarClickListener;
    }

    public void setOnCalendarClickListener(
            OnCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    public OnCalendarDateChangedListener getOnCalendarDateChangedListener() {
        return onCalendarDateChangedListener;
    }

    public void setOnCalendarDateChangedListener(
            OnCalendarDateChangedListener onCalendarDateChangedListener) {
        this.onCalendarDateChangedListener = onCalendarDateChangedListener;
    }

    public Date getThisday() {
        return thisday;
    }

    public void setThisday(Date thisday) {
        this.thisday = thisday;
    }

    public Map<String, Integer> getDayBgColorMap() {
        return dayBgColorMap;
    }

    public void setDayBgColorMap(Map<String, Integer> dayBgColorMap) {
        this.dayBgColorMap = dayBgColorMap;
    }

    public boolean isDateBefore(String date1, String date2) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(date1).before(df.parse(date2));
        } catch (ParseException e) {
            return false;
        }
    }

    public void setMaxDay(Date d) {
        this.maxDay = d;
        setCalendarDate(R.drawable.calendar_gray_bg_tag);

    }
}