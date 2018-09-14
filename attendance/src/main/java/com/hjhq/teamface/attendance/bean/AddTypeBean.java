package com.hjhq.teamface.attendance.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/13.
 * Describe：
 */

public class AddTypeBean {

    /**
     * name : 思创科技大厦/DAKA1
     * address : 广东省深圳市南山区粤海街道思创科技大厦
     * location : [{"lat":11.11,"lng":22.22,"address":"广东省深南大道"}]
     * effectiveRange  : 100
     * attendanceType  : 0
     */
    private String id;
    //地址或者wifi名称
    private String name;
    ////考勤地址或者MAC地址
    private String address;
    private String effectiveRange;
    private String attendanceType;
    private List<AttendanceLocationBean> location;
    ////开启状态  0启用  1禁用
    private String attendanceStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEffectiveRange() {
        return effectiveRange;
    }

    public void setEffectiveRange(String effectiveRange) {
        this.effectiveRange = effectiveRange;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public List<AttendanceLocationBean> getLocation() {
        return location;
    }

    public void setLocation(List<AttendanceLocationBean> location) {
        this.location = location;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }


}
