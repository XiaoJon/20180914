package com.hjhq.teamface.im.bean;


import com.hjhq.teamface.basis.bean.UploadFileBean;

import java.util.List;

/**
 * Created by lx on 2017/6/30.
 */

public class AddFriendsRequestBean {
    private String address;
    private Double longitude;
    private Double latitude;
    private String info;
    private List<UploadFileBean> images;
    private List<String> peoples;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<UploadFileBean> getImages() {
        return images;
    }

    public void setImages(List<UploadFileBean> images) {
        this.images = images;
    }

    public List<String> getPeoples() {
        return peoples;
    }

    public void setPeoples(List<String> peoples) {
        this.peoples = peoples;
    }
}