package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by lx on 2017/6/28.
 */

public class HelperSettingBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3437376136839168
         * createDate : null
         * disabled : null
         * itemId : 110
         * itemType : 1
         * assiatNotice : 1
         * assiatRead : 0
         * employeeId : 3437375100813312
         * companyId : 3437376034717696
         * createTime : 1498635764256
         * updateTime : null
         */

        private long id;
        private Long createDate;
        private Long disabled;
        private int itemId;
        private int itemType;
        private int assiatNotice;
        private int assiatRead;
        private long employeeId;
        private long companyId;
        private long createTime;
        private Long updateTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Long createDate) {
            this.createDate = createDate;
        }

        public Long getDisabled() {
            return disabled;
        }

        public void setDisabled(Long disabled) {
            this.disabled = disabled;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public int getAssiatNotice() {
            return assiatNotice;
        }

        public void setAssiatNotice(int assiatNotice) {
            this.assiatNotice = assiatNotice;
        }

        public int getAssiatRead() {
            return assiatRead;
        }

        public void setAssiatRead(int assiatRead) {
            this.assiatRead = assiatRead;
        }

        public long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(long employeeId) {
            this.employeeId = employeeId;
        }

        public long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(long companyId) {
            this.companyId = companyId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
