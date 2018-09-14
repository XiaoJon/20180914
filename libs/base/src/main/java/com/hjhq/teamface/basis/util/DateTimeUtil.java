package com.hjhq.teamface.basis.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author Administrator
 */
public class DateTimeUtil {
    public static SimpleDateFormat DateTime24h = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static SimpleDateFormat DateSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static SimpleDateFormat DateSdfYMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    /**
     * 设置每个阶段时间
     */
    private static final int SECONDS_OF_1MINUTE = 60;

    private static final int SECONDS_OF_30MINUTES = 30 * 60;

    private static final int SECONDS_OF_1HOUR = 60 * 60;

    private static final int SECONDS_OF_1DAY = 24 * 60 * 60;

    private static final int SECONDS_OF_15DAYS = SECONDS_OF_1DAY * 15;

    private static final int SECONDS_OF_30DAYS = SECONDS_OF_1DAY * 30;

    private static final int SECONDS_OF_6MONTHS = SECONDS_OF_30DAYS * 6;

    private static final int SECONDS_OF_1YEAR = SECONDS_OF_30DAYS * 12;


    /**
     * long类型时间转换
     *
     * @param millis long类型时间
     * @param format 转换格式 null值默认"yyyy-MM-dd_HH:mm:ss"
     * @return String类型时间
     */
    public static String longToStr(long millis, String format) {
        String str = "";
        if (format == null || format.length() == 0) {
            format = "yyyy-MM-dd_HH:mm:ss";
        }
        if (millis != 0) {
            str = new SimpleDateFormat(format, Locale.CHINA)
                    .format(millis);
        }
        return str;
    }

    /**
     * 自动转换时间 当前年就 自动省略 年份
     *
     * @param millis long类型时间
     * @return
     */
    public static String AuthToTime(long millis) {
        String str = "";
        long currentYearFristDay = getCurrYearFirst().getTime();
        String format = "yyyy年MM月dd日 HH:mm";
        if (currentYearFristDay < millis) {
            format = "MM月dd日 HH:mm";
        }
        if (millis != 0) {
            str = new SimpleDateFormat(format, Locale.CHINA)
                    .format(millis);
        }
        return str;
    }

    public static String getCurrentStr(String format) {
        return longToStr(System.currentTimeMillis(), format);
    }

    public static String longToStr_YYYY_MM_DD(long millis) {
        return DateSdf.format(millis);
    }

    public static String longToStr_YYYY_MM_DD_HH_MM(long millis) {
        return DateSdfYMDHM.format(millis);
    }


    /**
     * 字符串类型时间转换成Long类型时间
     *
     * @param str    字符串类型时间
     * @param format 转换格式 null值默认"yyyy-MM-dd_HH:mm:ss"
     * @return Long类型时间
     */
    public static Long strToLong(String str, String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Long newTimeLong = null;
        try {
            newTimeLong = sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTimeLong;
    }


    public static String dateTimeToString(Date date) {
        return DateTime24h.format(date);
    }

    public static String dateToString(Date date) {
        return DateSdf.format(date);
    }

    /**
     * 比较两个时间大小  0:相同  1：大  -1：小
     *
     * @param format 时间格式
     * @param date1  Date时间
     * @param date2  Date时间
     * @return 0:相同  1：大  -1：小
     */
    public static int getTimeByStringFromat(SimpleDateFormat format, String date1, String date2) {
        int size = 0;
        try {
            Date parse1 = format.parse(date1);
            Date parse2 = format.parse(date2);
            size = getTimeByStringFromat(parse1, parse2);
            if (parse1.getTime() > parse2.getTime()) {
                size = 1;
            } else if (parse1.getTime() < parse2.getTime()) {
                size = -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 比较两个时间大小  0:相同  1：大  -1：小
     *
     * @param date1 Date时间
     * @param date2 Date时间
     * @return 0:相同  1：大  -1：小
     */
    public static int getTimeByStringFromat(Date date1, Date date2) {
        int size = 0;
        if (date1.getTime() > date2.getTime()) {
            size = 1;
        } else if (date1.getTime() < date2.getTime()) {
            size = -1;
        }
        return size;
    }


    /**
     * 将long 类型的时间转换成 String格式 ，如果时间是今年则省去年份
     *
     * @param time long类型时间
     * @return str时间
     */
    public static String fromTime(long time) {
        if (time >= getCurrYearFirst().getTime()) {
            return longToStr(time, "MM-dd HH:mm");
        } else {
            return longToStr(time, "yyyy-MM-dd HH:mm");
        }
    }


    /**
     * 将long 类型的时间转换成 String格式 ，如果时间是今年则省去年份
     *
     * @param time long类型时间
     * @return str时间
     */
    public static String showTime(long time) {
        if (time >= getCurrYearFirst().getTime()) {
            return longToStr(time, "MM月dd日 HH:mm:ss");
        } else {
            return longToStr(time, "yyyy年MM月dd日 HH:mm:ss");
        }
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 将long转为mm:ss
     *
     * @param duration 时间
     * @return mm:ss
     */
    public static String getDurationString(long duration) {
        long aux = duration / 1000;
        int minute = (int) (aux / 60);
        int second = (int) (aux % 60);

        final String sDuration = // Minutes
                (minute < 10 ? "0" + minute : minute + "")
                        + ":" +
                        // Seconds
                        (second < 10 ? "0" + second : second + "");
        return sDuration;
    }

    /**
     * 人性化时间
     *
     * @param mTime long类型时间格式
     * @return
     */
    public static String getformatTime(long mTime) {
        if (mTime == 0) {
            return "";
        }

        BigDecimal begin = new BigDecimal(mTime);
        BigDecimal end = new BigDecimal(System.currentTimeMillis());

        Calendar calToday = Calendar.getInstance();
        Date dateToday = new Date(System.currentTimeMillis());
        calToday.setTime(dateToday);
        Calendar calWhen = Calendar.getInstance();
        Date dateWhen = new Date(mTime);
        calWhen.setTime(dateWhen);
        int elapsedTime = ((int) end.subtract(begin).doubleValue()) / 1000;
        if (elapsedTime >= 0) {
            if (elapsedTime < SECONDS_OF_1MINUTE) {
                if (calToday.get(Calendar.YEAR) == calWhen.get(Calendar.YEAR)
                        && calToday.get(Calendar.MONTH) == calWhen.get(Calendar.MONTH)
                        && calToday.get(Calendar.DATE) == calWhen.get(Calendar.DATE)
                        ) {
                    return "刚刚";
                }
            }
            if (elapsedTime < SECONDS_OF_30MINUTES
                    && calToday.get(Calendar.YEAR) == calWhen.get(Calendar.YEAR)
                    && calToday.get(Calendar.MONTH) == calWhen.get(Calendar.MONTH)
                    && calToday.get(Calendar.DATE) == calWhen.get(Calendar.DATE)) {
                return elapsedTime / SECONDS_OF_1MINUTE + "分钟前";
            }
            if (elapsedTime < SECONDS_OF_1HOUR
                    && calToday.get(Calendar.YEAR) == calWhen.get(Calendar.YEAR)
                    && calToday.get(Calendar.MONTH) == calWhen.get(Calendar.MONTH)
                    && calToday.get(Calendar.DATE) == calWhen.get(Calendar.DATE)) {
                return "半小时前";
            }
            if (elapsedTime < SECONDS_OF_1DAY
                    && calToday.get(Calendar.YEAR) == calWhen.get(Calendar.YEAR)
                    && calToday.get(Calendar.MONTH) == calWhen.get(Calendar.MONTH)
                    && calToday.get(Calendar.DATE) == calWhen.get(Calendar.DATE)) {
                return "今天" + longToStr(mTime, "HH:mm:ss");
            }
            if (elapsedTime < SECONDS_OF_30DAYS) {
                int dayNum = elapsedTime / SECONDS_OF_1DAY;
                switch (dayNum) {
                    case 0:
                        return showTime(mTime);
                    case 1:
                        return "昨天" + longToStr(mTime, "HH:mm:ss");
                    case 2:
                        return "前天" + longToStr(mTime, "HH:mm:ss");
                    default:
                        return dayNum + "天前" + longToStr(mTime, "HH:mm:ss");
                }
            }
            return showTime(mTime);
        }
        return longToStr(mTime, "yyyy年MM月dd日 HH:mm:ss");
    }

    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay(Calendar calendar) {

        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDate = calendar.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取指定月份的天数
     *
     * @param month
     * @return
     */
    public static int getCurrentMonthDay(int month) {
// TODO: 2018/6/12 参数错误处理
        Calendar a = Calendar.getInstance();
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
