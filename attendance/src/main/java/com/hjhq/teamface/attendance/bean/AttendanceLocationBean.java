package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/6/22.
 * Describe：
 */

public class AttendanceLocationBean {
    /**
     * lat : 11.11
     * lng : 22.22
     * address : 广东省深南大道
     */

    private double lat;
    private double lng;
    private String address;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
