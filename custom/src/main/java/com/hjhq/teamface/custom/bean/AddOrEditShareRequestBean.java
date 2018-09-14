package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.database.Member;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/12/21
 */

public class AddOrEditShareRequestBean {

    /**
     * bean_name : 122
     * dataId : 3
     * id : 3
     * basics : {"access_permissions":"0","allot_employee":"前台传数组","target_lable":"开发部,肖俊"}
     */

    private String bean_name;
    private String dataId;
    private String id;
    private BasicsBean basics;

    public String getBean_name() {
        return bean_name;
    }

    public void setBean_name(String bean_name) {
        this.bean_name = bean_name;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BasicsBean getBasics() {
        return basics;
    }

    public void setBasics(BasicsBean basics) {
        this.basics = basics;
    }

    public static class BasicsBean {
        /**
         * access_permissions : 0
         * allot_employee : 前台传数组
         * target_lable : 开发部,肖俊
         */

        private String access_permissions;
        private List<Member> allot_employee;
        private String target_lable;

        public String getAccess_permissions() {
            return access_permissions;
        }

        public void setAccess_permissions(String access_permissions) {
            this.access_permissions = access_permissions;
        }

        public List<Member> getAllot_employee() {
            return allot_employee;
        }

        public void setAllot_employee(List<Member> allot_employee) {
            this.allot_employee = allot_employee;
        }

        public String getTarget_lable() {
            return target_lable;
        }

        public void setTarget_lable(String target_lable) {
            this.target_lable = target_lable;
        }
    }
}
