package com.hjhq.teamface.attendance.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 * Describe：
 */

public class AttendanceSettingBean {

   /* {
        "adminArr":1,3,5                        //管理员
        "remindCockBeforeWork":15,              //上班前打卡提醒
            " remindClockAfterWork":30,             //下班后打卡提醒
            "listSetType": 0,                       //榜单统计方式（0分开、1一起）
            "listSetEarlyArrival": 10               //早到榜统计人数
        "listSetDiligent": 10                   //勤勉榜统计人数
        "listSetBeLate": 10                     //迟到榜统计人数
        "listSetSortType":0,                    //排序方式（0迟到次数、1迟到时长）
            "lateNigthWalkArr":[],                  //设置晚到晚走数组
        "humanizationAllowLateTimes":3,         //人性化允许每月迟到次数
            "humanizationAllowLateMinutes":10,      //人性化允许单次迟到分钟数
            "absenteeismRuleBeLateMinutes":30,      //单次迟到超过分钟数为旷工
            "absenteeismRuleLeaveEarlyMinutes ":30  //单次早退分钟数为旷工
    }*/


    private String adminArr;
    private int remindCockBeforeWork;
    private int remindClockAfterWork;
    private int listSetType;
    private int listSetEarlyArrival;
    private int listSetDiligent;
    private int listSetBeLate;
    private int listSetSortType;
    private int humanizationAllowLateTimes;
    private int humanizationAllowLateMinutes;
    private int absenteeismRuleBeLateMinutes;
    private int absenteeismRuleLeaveEarlyMinutes;
    private java.util.List<AddLeaveingLateRulesBean> lateNigthWalkArr;

    public int getRemindClockAfterWork() {
        return remindClockAfterWork;
    }

    public void setRemindClockAfterWork(int remindClockAfterWork) {
        this.remindClockAfterWork = remindClockAfterWork;
    }

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

    public int getHumanizationAllowLateTimes() {
        return humanizationAllowLateTimes;
    }

    public void setHumanizationAllowLateTimes(int humanizationAllowLateTimes) {
        this.humanizationAllowLateTimes = humanizationAllowLateTimes;
    }

    public int getHumanizationAllowLateMinutes() {
        return humanizationAllowLateMinutes;
    }

    public void setHumanizationAllowLateMinutes(int humanizationAllowLateMinutes) {
        this.humanizationAllowLateMinutes = humanizationAllowLateMinutes;
    }

    public int getAbsenteeismRuleBeLateMinutes() {
        return absenteeismRuleBeLateMinutes;
    }

    public void setAbsenteeismRuleBeLateMinutes(int absenteeismRuleBeLateMinutes) {
        this.absenteeismRuleBeLateMinutes = absenteeismRuleBeLateMinutes;
    }

    public int getAbsenteeismRuleLeaveEarlyMinutes() {
        return absenteeismRuleLeaveEarlyMinutes;
    }

    public void setAbsenteeismRuleLeaveEarlyMinutes(int absenteeismRuleLeaveEarlyMinutes) {
        this.absenteeismRuleLeaveEarlyMinutes = absenteeismRuleLeaveEarlyMinutes;
    }

    public List<AddLeaveingLateRulesBean> getLateNigthWalkArr() {
        return lateNigthWalkArr;
    }

    public void setLateNigthWalkArr(List<AddLeaveingLateRulesBean> lateNigthWalkArr) {
        this.lateNigthWalkArr = lateNigthWalkArr;
    }

    public String getAdminArr() {
        return adminArr;
    }

    public void setAdminArr(String adminArr) {
        this.adminArr = adminArr;
    }

    public int getRemindCockBeforeWork() {
        return remindCockBeforeWork;
    }

    public void setRemindCockBeforeWork(int remindCockBeforeWork) {
        this.remindCockBeforeWork = remindCockBeforeWork;
    }
}
