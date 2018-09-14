package com.hjhq.teamface.project.ui.filter.weight.filter;

import android.support.annotation.Nullable;

import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.log.LogUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 自定义模块时间工具类
 * Created by Administrator on 2018/3/22.
 */

public class CustomTimeUtil {
    /**
     * 获取本月开始时间
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return long类型时间
     */
    private static long getMonthStartTime(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.set(year, month, day, hour, minute, second);
        return cal.getTimeInMillis();
    }

    private final static long MS_IN_A_SECOND = 1000L;
    private final static long MS_IN_A_MINUTE = MS_IN_A_SECOND * 60L;
    private final static long MS_IN_AN_HOUR = MS_IN_A_MINUTE * 60L;
    private final static long MS_IN_A_DAY = MS_IN_AN_HOUR * 24L;
    private final static long MS_IN_A_WEEK = MS_IN_A_DAY * 7L;
    private final static long MS_IN_A_MONTH = 1000 * 60 * 60 * 24 * 30L;

    /**
     * 根据传入的值获取时间段
     *
     * @return 获取时间段
     */
    @Nullable
    public static Map<String, Long> getTimeRange(int i) {
        long startTime = -1L;
        long endTime = -1L;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        boolean isLeapYear = (year % 4 == 0) && ((year % 100 != 0)) || (year % 400 == 0);
        LogUtil.e(year + "年" + (month + 1) + "月" + day + "日");
        switch (i) {
            case 0:
                //今天
                startTime = getMonthStartTime(year, month, day, 0, 0, 0);
                endTime = getMonthStartTime(year, month, day, 23, 59, 59);
                break;
            case 1:
                //昨天
                startTime = getMonthStartTime(year, month, day, 0, 0, 0) - MS_IN_A_DAY;
                endTime = getMonthStartTime(year, month, day, 0, 0, 0) - 1;
                break;
            case 2:
                //最近7天
                endTime = getMonthStartTime(year, month, day, 23, 59, 59);
                startTime = endTime - MS_IN_A_WEEK;
                break;
            case 3:
                //最近30天
                endTime = getMonthStartTime(year, month, day, 23, 59, 59);
                startTime = endTime - MS_IN_A_MONTH;
                break;
            case 4:
                //本月
                startTime = getMonthStartTime(year, month, 1, 0, 0, 0);
                endTime = getMonthStartTime(year, month, day, 23, 59, 59);
                break;
            case 5:
                //上月
                endTime = getMonthStartTime(year, month, 1, 0, 0, 0) - 1L;
                if (month == 0) {
                    startTime = getMonthStartTime(year - 1, 11, 1, 0, 0, 0);
                } else {
                    startTime = getMonthStartTime(year, month - 1, 1, 0, 0, 0);
                }
                break;
            case 6:
                //本季度
                if (month == 0 || month == 1 || month == 2) {
                    startTime = getMonthStartTime(year, 0, 1, 0, 0, 0);
                    if (isLeapYear) {
                        endTime = getMonthStartTime(year, 2, 29, 23, 59, 59);
                    } else {
                        endTime = getMonthStartTime(year, 2, 28, 23, 59, 59);
                    }
                } else if (month == 3 || month == 4 || month == 5) {
                    startTime = getMonthStartTime(year, 3, 1, 0, 0, 0);
                    endTime = getMonthStartTime(year, 5, 31, 23, 59, 59);
                } else if (month == 6 || month == 7 || month == 8) {
                    startTime = getMonthStartTime(year, 6, 1, 0, 0, 0);
                    endTime = getMonthStartTime(year, 8, 30, 23, 59, 59);
                } else if (month == 9 || month == 10 || month == 11) {
                    startTime = getMonthStartTime(year, 9, 1, 0, 0, 0);
                    endTime = getMonthStartTime(year, 11, 30, 23, 59, 59);
                }
                break;
            case 7:
                //上季度
                if (month == 0 || month == 1 || month == 2) {
                    startTime = getMonthStartTime(year - 1, 9, 1, 0, 0, 0);
                    endTime = getMonthStartTime(year - 1, 11, 31, 23, 59, 59);
                } else if (month == 3 || month == 4 || month == 5) {
                    startTime = getMonthStartTime(year, 0, 1, 0, 0, 0);
                    if (isLeapYear) {
                        endTime = getMonthStartTime(year, 2, 29, 23, 59, 59);
                    } else {
                        endTime = getMonthStartTime(year, 2, 28, 23, 59, 59);
                    }
                } else if (month == 6 || month == 7 || month == 8) {
                    startTime = getMonthStartTime(year, 3, 1, 0, 0, 0);
                    endTime = getMonthStartTime(year, 5, 30, 23, 59, 59);
                } else if (month == 9 || month == 10 || month == 11) {
                    startTime = getMonthStartTime(year, 6, 1, 0, 0, 0);
                    endTime = getMonthStartTime(year, 8, 30, 23, 59, 59);
                }
                break;
            default:
                break;
        }
        LogUtil.e("开始时间 = " + DateTimeUtil.longToStr(startTime, "yyyy-MM-dd HH:mm:ss"));
        LogUtil.e("结束时间 = " + DateTimeUtil.longToStr(endTime, "yyyy-MM-dd HH:mm:ss"));
        if (endTime == -1L || startTime == -1L) {
            return null;
        }
        Map<String, Long> json = new HashMap<>(2);
        json.put("startTime", startTime);
        json.put("endTime", endTime);

        return json;
    }
}
