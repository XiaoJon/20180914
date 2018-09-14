package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/7/26.
 * Describe：
 */

public class ChangeRankRulesBean {

    /**
     * listSetType : 0
     * listSetEarlyArrival : 10
     * listSetDiligent : 10
     * listSetBeLate : 10
     * listSetSortType : 0
     */

    private int listSetType;
    private int listSetEarlyArrival;
    private int listSetDiligent;
    private int listSetBeLate;
    private int listSetSortType;

    public int getListSetType() {
        return listSetType;
    }

    public void setListSetType(int listSetType) {
        this.listSetType = listSetType;
    }

    public int getListSetEarlyArrival() {
        return listSetEarlyArrival;
    }

    public void setListSetEarlyArrival(int listSetEarlyArrival) {
        this.listSetEarlyArrival = listSetEarlyArrival;
    }

    public int getListSetDiligent() {
        return listSetDiligent;
    }

    public void setListSetDiligent(int listSetDiligent) {
        this.listSetDiligent = listSetDiligent;
    }

    public int getListSetBeLate() {
        return listSetBeLate;
    }

    public void setListSetBeLate(int listSetBeLate) {
        this.listSetBeLate = listSetBeLate;
    }

    public int getListSetSortType() {
        return listSetSortType;
    }

    public void setListSetSortType(int listSetSortType) {
        this.listSetSortType = listSetSortType;
    }
}
