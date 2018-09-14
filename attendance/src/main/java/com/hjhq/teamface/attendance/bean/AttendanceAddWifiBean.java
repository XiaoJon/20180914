package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/6/22.
 * Describe：
 */

public class AttendanceAddWifiBean {


    private String name;
    private String address;
    boolean check;
    boolean isCurrentWifi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCurrentWifi() {
        return isCurrentWifi;
    }

    public void setCurrentWifi(boolean currentWifi) {
        isCurrentWifi = currentWifi;
    }
}
