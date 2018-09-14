package com.hjhq.teamface.project.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/18.
 */

public class MonthDateBean implements Serializable{
    private int day;
    private boolean isCheck;

    public MonthDateBean() {
    }

    public MonthDateBean(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
