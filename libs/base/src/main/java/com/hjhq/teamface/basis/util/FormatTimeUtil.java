package com.hjhq.teamface.basis.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 */
public class FormatTimeUtil {

    public static void main(String[] args) {
        System.out.println("当前时间：" + new SimpleDateFormat("yyyy/M/d HH:mm:ss").format(System.currentTimeMillis()));
        System.out.println("2016/2/1 05:05:00  显示为：" + getNewChatTime(1454666700000l));
        System.out.println("2017/2/1 05:05:00  显示为：" + getNewChatTime(1485983100000l));
        System.out.println("2017/2/4 12:05:00  显示为：" + getNewChatTime(1486181100000l));
        System.out.println("2017/2/5 10:10:00  显示为：" + getNewChatTime(1486260600000l));
        System.out.println("2017/2/5 13:12:00  显示为：" + getNewChatTime(1486271520000l));
        System.out.println("2017/2/6 14:05:00  显示为：" + getNewChatTime(1486361100000l));


    /*运行结果：
    当前时间：2017/2/9 14:36:36
    2016/2/1 05:05:00  显示为：2016年2月5日 晚上18:05
    2017/2/1 05:05:00  显示为：2月2日 凌晨05:05
    2017/2/4 12:05:00  显示为：2月4日 中午12:05
    2017/2/5 13:12:00  显示为：2月5日 早上10:10
    2017/2/5 13:12:00  显示为：2月5日 下午13:12
    2017/2/6 14:05:00  显示为：周一14:05*/
    }

    /**
     * 时间戳格式转换
     */
    public static String dayNames[] = {"周日 ", "周一 ", "周二 ", "周三 ", "周四 ", "周五 ", "周六 "};

    public static String getNewChatTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日 HH:mm";
        String yearTimeFormat = "yyyy年M月d日 HH:mm";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨 ";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上 ";
        } else if (hour == 12) {
            am_pm = "中午 ";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午 ";
        } else if (hour >= 18) {
            am_pm = "晚上 ";
        }
        timeFormat = "M月d日 " + am_pm + "HH:mm";
        yearTimeFormat = "yyyy年M月d日 " + am_pm + "HH:mm";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = "今天 " + getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + getHourAndMin(timesamp);
                        break;
                    case 2:
                        result = "前天 " + getHourAndMin(timesamp);
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getHourAndMin(timesamp);
                            } else {
                                result = getTime(timesamp, timeFormat);
                            }
                        } else {
                            result = getTime(timesamp, timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp, timeFormat);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat);
            }
        } else {
            result = getYearTime(timesamp, yearTimeFormat);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeFormat
     * @return
     */
    public static String getTime(long time, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }
}