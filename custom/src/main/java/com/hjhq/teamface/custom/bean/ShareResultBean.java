package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.database.Member;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2017/12/25
 */

public class ShareResultBean extends BaseBean implements Serializable {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * personnel_create_by : 3
         * allot_employee_v : 1-1
         * access_permissions : 1
         * del_status : 0
         * datetime_modify_time :
         * target_lable : 李萌
         * bean_name : bean1513937622761
         * datetime_create_time : 1514186294800
         * module_id : 19
         * personnel_modify_by :
         * employee_id : 3
         * id : 1
         * allot_employee : [{"sign_id":4,"check":false,"id":1,"text":"李萌","type":1,"picture":"http://192.168.1.58:8281/custom-gateway/common/file/imageDownload?bean=company&fileName=company/1513935949635.ab0ca502f12ae618be567da104fbab84.jpg"}]
         */

        private String personnel_create_by;
        private String allot_employee_v;
        private String access_permissions;
        private String del_status;
        private String datetime_modify_time;
        private String target_lable;
        private String bean_name;
        private long datetime_create_time;
        private String module_id;
        private String personnel_modify_by;
        private int employee_id;
        private int id;
        private List<Member> allot_employee;

        public String getPersonnel_create_by() {
            return personnel_create_by;
        }

        public void setPersonnel_create_by(String personnel_create_by) {
            this.personnel_create_by = personnel_create_by;
        }

        public String getAllot_employee_v() {
            return allot_employee_v;
        }

        public void setAllot_employee_v(String allot_employee_v) {
            this.allot_employee_v = allot_employee_v;
        }

        public String getAccess_permissions() {
            return access_permissions;
        }

        public void setAccess_permissions(String access_permissions) {
            this.access_permissions = access_permissions;
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

        public String getTarget_lable() {
            return target_lable;
        }

        public void setTarget_lable(String target_lable) {
            this.target_lable = target_lable;
        }

        public String getBean_name() {
            return bean_name;
        }

        public void setBean_name(String bean_name) {
            this.bean_name = bean_name;
        }

        public long getDatetime_create_time() {
            return datetime_create_time;
        }

        public void setDatetime_create_time(long datetime_create_time) {
            this.datetime_create_time = datetime_create_time;
        }

        public String getModule_id() {
            return module_id;
        }

        public void setModule_id(String module_id) {
            this.module_id = module_id;
        }

        public String getPersonnel_modify_by() {
            return personnel_modify_by;
        }

        public void setPersonnel_modify_by(String personnel_modify_by) {
            this.personnel_modify_by = personnel_modify_by;
        }

        public int getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(int employee_id) {
            this.employee_id = employee_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Member> getAllot_employee() {
            return allot_employee;
        }

        public void setAllot_employee(List<Member> allot_employee) {
            this.allot_employee = allot_employee;
        }

    }
}
