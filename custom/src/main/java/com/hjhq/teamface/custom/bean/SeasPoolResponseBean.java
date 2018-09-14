package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/1/30
 */

public class SeasPoolResponseBean extends BaseBean implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * employee_target_lable : 肖俊,李萌,莫凡
         * id : 1
         * title : 公海池1
         */

        private String employee_target_lable;
        private String id;
        private String title;

        protected boolean isCheck;

        public String getEmployee_target_lable() {
            return employee_target_lable;
        }

        public void setEmployee_target_lable(String employee_target_lable) {
            this.employee_target_lable = employee_target_lable;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
