package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * 应用模块实体
 *
 * @author Administrator
 * @date 2018/5/9
 */

public class AppModuleBean implements Serializable {
    /**
     * terminal_app : 3
     * icon_url :
     * personnel_create_by : 28
     * icon : icon-mokuai-xian4
     * topper : 3
     * del_status : 0
     * application_id : 156
     * english_name : bean1525830206944
     * chinese_name : 加班
     * datetime_modify_time : 1525846680225
     * datetime_create_time : 1525830324667
     * application_name : 帅胜余-审批
     * icon_color :
     * personnel_modify_by : 30
     * id : 317
     * icon_type :
     */

    private String terminal_app;
    private String personnel_create_by;
    private String icon;
    private String topper;
    private String icon_url;
    private String icon_color;
    private String icon_type;
    private String del_status;
    private String application_id;
    private String english_name;
    private String chinese_name;
    private String application_name;
    private String personnel_modify_by;
    private String id;

    public String getTerminal_app() {
        return terminal_app;
    }

    public void setTerminal_app(String terminal_app) {
        this.terminal_app = terminal_app;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getPersonnel_create_by() {
        return personnel_create_by;
    }

    public void setPersonnel_create_by(String personnel_create_by) {
        this.personnel_create_by = personnel_create_by;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTopper() {
        return topper;
    }

    public void setTopper(String topper) {
        this.topper = topper;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getChinese_name() {
        return chinese_name;
    }

    public void setChinese_name(String chinese_name) {
        this.chinese_name = chinese_name;
    }

    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(String icon_color) {
        this.icon_color = icon_color;
    }

    public String getPersonnel_modify_by() {
        return personnel_modify_by;
    }

    public void setPersonnel_modify_by(String personnel_modify_by) {
        this.personnel_modify_by = personnel_modify_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon_type() {
        return icon_type;
    }

    public void setIcon_type(String icon_type) {
        this.icon_type = icon_type;
    }
}
