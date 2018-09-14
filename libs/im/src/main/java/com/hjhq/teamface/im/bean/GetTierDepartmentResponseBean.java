package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 *
 * 获取所有部门按层级
 */

public class GetTierDepartmentResponseBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3341102220394496
         * createDate : 1492759670726
         * disabled : 0
         * companyId : 3341081741901824
         * departmentName : 研发部
         * parentDepartmentId : 3341081779453952
         * principalId : null
         * isDefault : 0
         */

        private long id;
        private long createDate;
        private int disabled;
        private long companyId;
        private String departmentName;
        private long parentDepartmentId;
        private Object principalId;
        private int isDefault;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(long companyId) {
            this.companyId = companyId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public long getParentDepartmentId() {
            return parentDepartmentId;
        }

        public void setParentDepartmentId(long parentDepartmentId) {
            this.parentDepartmentId = parentDepartmentId;
        }

        public Object getPrincipalId() {
            return principalId;
        }

        public void setPrincipalId(Object principalId) {
            this.principalId = principalId;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }
    }
}
