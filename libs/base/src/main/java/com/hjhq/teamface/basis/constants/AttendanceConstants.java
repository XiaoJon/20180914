package com.hjhq.teamface.basis.constants;

/**
 * 考勤常量类
 * Created by Administrator on 2018/4/24.
 */

public class AttendanceConstants {
    public static final String BEAN_NAME = "attendance";


    /**
     * 考勤打卡类型 -正常上班
     */
    public static final int TYPE_WORK_NORMAL = 1;
    /**
     * 考勤打卡类型 -正常下班
     */
    public static final int TYPE_OFF_NORMAL = 2;
    /**
     * 考勤打卡类型 -迟到
     */
    public static final int TYPE_WORK_UNNORMAL = 3;
    /**
     * 考勤打卡类型 -早退
     */
    public static final int TYPE_OFF_UNNORMAL = 4;
    /**
     * 考勤打卡类型 -旷工
     */
    public static final int TYPE_NO_RECORD = 5;

    /**
     * 地址类型考勤
     */
    public static final String TYPE_LOCATION = "0";
    /**
     * WiFi类型考勤
     */
    public static final String TYPE_WIFI = "1";


    public static final int TYPE_SELECT_TIME = 101;
    public static final int TYPE_SELECT_WIFI = 102;
    public static final int TYPE_SELECT_LOCATION = 103;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;

}
