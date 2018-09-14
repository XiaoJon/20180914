package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/6/8.
 * Describe：
 */

public class AttendanceDataBean {
    String date;
    boolean isToday;
    boolean selected;
    int state;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
