package com.hjhq.teamface.attendance.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 * Describe：
 */

public class AddRulesBean {

    /**
     * name : 考勤组1-1
     * mustAttendance : [{"name":"名字","id":"4","picture":"","type":1,"value":"1-4"}]
     * noAttendance :
     * attendanceType : 0
     * autoStatus : 0
     * mustPunchcardDate : [[{"name":"必须打卡日期","id":"21","type":"3","time":"1529995805","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00"},{"name":"必须打卡日期","id":"21","type":"3","time":"1529999805","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00"}]]
     * noPunchcardDate : 1529995805,1530995805,1531995805
     * reslutData : [{"name":"星期一","id":"21","type":"1","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00"},{"name":"星期二","id":"21","type":"1","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00"},{"name":"星期三","id":"21","type":"1","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00"},{"name":"星期四","id":"21","type":"1","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00"},{"name":"星期五","id":"21","type":"1","label":"班3-2:07:00-14:00;12:00-15:00;16:00-18:00;"},{"name":"星期六","id":"","type":"2","label":"休息"},{"name":"星期日","id":"","type":"2","label":"休息"}]
     * attendanceStartTime : 1529995805
     * attendanceAddress : [{"create_by":1,"effective_range":"1000","address":"广东省深圳市南山区科技园高新南一道21-2号","create_time":1529658127739,"modify_time":1529660101309,"name":"思创科技大厦","location":[{"address":"广东省深圳市南山区科技园高新南一道21-2号","lng":113.946318,"lat":22.538232}],"del_status":"0","id":31,"modify_by":1,"attendance_status":"0","attendance_type":"0"}]
     * attendanceWifi : [{"create_by":1,"effective_range":"","address":"14:75:90:95:b2:ca","create_time":1529655027235,"modify_time":"","name":"YPJ8888","location":[],"del_status":"0","id":26,"modify_by":"","attendance_status":"0","attendance_type":"1"}]
     * outwokerStatus : 0
     * faceStatus : 0
     */
    private String id;
    private String name;
    private String noAttendance;
    private String attendanceType;
    private String autoStatus;
    private String noPunchcardDate;
    private String attendanceStartTime;
    private String outwokerStatus;
    private String faceStatus;
    private List<MemberOrDepartmentBean> mustAttendance;
    private List<AddDateBean> mustPunchcardDate;
    private List<AddDateBean> reslutData;
    private List<AttendanceTypeListBean> attendanceAddress;
    private List<AttendanceTypeListBean> attendanceWifi;

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

    public String getNoAttendance() {
        return noAttendance;
    }

    public void setNoAttendance(String noAttendance) {
        this.noAttendance = noAttendance;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(String autoStatus) {
        this.autoStatus = autoStatus;
    }

    public String getNoPunchcardDate() {
        return noPunchcardDate;
    }

    public void setNoPunchcardDate(String noPunchcardDate) {
        this.noPunchcardDate = noPunchcardDate;
    }

    public String getAttendanceStartTime() {
        return attendanceStartTime;
    }

    public void setAttendanceStartTime(String attendanceStartTime) {
        this.attendanceStartTime = attendanceStartTime;
    }

    public String getOutwokerStatus() {
        return outwokerStatus;
    }

    public void setOutwokerStatus(String outwokerStatus) {
        this.outwokerStatus = outwokerStatus;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public List<MemberOrDepartmentBean> getMustAttendance() {
        return mustAttendance;
    }

    public void setMustAttendance(List<MemberOrDepartmentBean> mustAttendance) {
        this.mustAttendance = mustAttendance;
    }

    public List<AddDateBean> getMustPunchcardDate() {
        return mustPunchcardDate;
    }

    public void setMustPunchcardDate(List<AddDateBean> mustPunchcardDate) {
        this.mustPunchcardDate = mustPunchcardDate;
    }

    public List<AddDateBean> getReslutData() {
        return reslutData;
    }

    public void setReslutData(List<AddDateBean> reslutData) {
        this.reslutData = reslutData;
    }

    public List<AttendanceTypeListBean> getAttendanceAddress() {
        return attendanceAddress;
    }

    public void setAttendanceAddress(List<AttendanceTypeListBean> attendanceAddress) {
        this.attendanceAddress = attendanceAddress;
    }

    public List<AttendanceTypeListBean> getAttendanceWifi() {
        return attendanceWifi;
    }

    public void setAttendanceWifi(List<AttendanceTypeListBean> attendanceWifi) {
        this.attendanceWifi = attendanceWifi;
    }


}
