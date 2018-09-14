package com.hjhq.teamface.attendance.bean;

/**
 * Created by Administrator on 2018/8/2.
 * Describe：
 */

public class AttendanceDetailDataBean {

    /**
     * employee_id :
     * work_time :
     * serial :
     * classes_id :
     * schedule_id :
     * current_month :
     * create_by  :
     * create_time  :
     */

    private String employee_id;
    private String work_time;
    private String serial;
    private String classes_id;
    private String schedule_id;
    private String current_month;
    private String create_by;
    private String create_time;

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getClasses_id() {
        return classes_id;
    }

    public void setClasses_id(String classes_id) {
        this.classes_id = classes_id;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getCurrent_month() {
        return current_month;
    }

    public void setCurrent_month(String current_month) {
        this.current_month = current_month;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
