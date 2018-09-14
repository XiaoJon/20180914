package com.hjhq.teamface.memo.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/27.
 * Describe：
 */

public class MemoLocationBean implements Serializable {

    /**
     * lat : 22.538232
     * lng : 113.946318
     * address : 深圳市南高新南一道
     */

    private String lat;
    private String lng;
    private String address;
    private String name;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
