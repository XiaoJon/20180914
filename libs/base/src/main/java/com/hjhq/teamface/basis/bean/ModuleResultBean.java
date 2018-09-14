package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/9/9.
 */

public class ModuleResultBean extends BaseBean implements Serializable {

    private ArrayList<DataBean> data;

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String icon_url;
        private String company_id;
        private String personnel_create_by;
        private String icon;
        private String topper;
        private String del_status;
        private String datetime_modify_time;
        private String datetime_create_time;
        private String icon_color;
        private String personnel_modify_by;
        private String id;
        private String icon_type;
        private String name;
        private ArrayList<AppModuleBean> modules;

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
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

        public String getDatetime_modify_time() {
            return datetime_modify_time;
        }

        public void setDatetime_modify_time(String datetime_modify_time) {
            this.datetime_modify_time = datetime_modify_time;
        }

        public String getDatetime_create_time() {
            return datetime_create_time;
        }

        public void setDatetime_create_time(String datetime_create_time) {
            this.datetime_create_time = datetime_create_time;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<AppModuleBean> getModules() {
            return modules;
        }

        public void setModules(ArrayList<AppModuleBean> modules) {
            this.modules = modules;
        }
    }

}
