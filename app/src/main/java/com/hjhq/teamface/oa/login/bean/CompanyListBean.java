package com.hjhq.teamface.oa.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/11/13
 * Describe：公司列表
 */

public class CompanyListBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 39
         * company_name : 王克栋公司
         */

        private String id;
        private String company_name;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }
    }
}
