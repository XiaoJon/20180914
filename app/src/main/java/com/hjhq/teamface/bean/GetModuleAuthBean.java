package com.hjhq.teamface.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */

public class GetModuleAuthBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1110
         * createDate : null
         * disabled : null
         * name : 任务
         * descr : 项目协作管理，任务进度跟进
         * icon : null
         * moduleStatus : 0
         * createTime : 1495715028756
         * updateTime : null
         * isFront : 1
         * pageUrl : task_page
         */

        private int id;
        private Object createDate;
        private Object disabled;
        private String name;
        private String descr;
        private Object icon;
        private int moduleStatus;
        private long createTime;
        private Object updateTime;
        private int isFront;
        private String pageUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }

        public Object getDisabled() {
            return disabled;
        }

        public void setDisabled(Object disabled) {
            this.disabled = disabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

        public int getModuleStatus() {
            return moduleStatus;
        }

        public void setModuleStatus(int moduleStatus) {
            this.moduleStatus = moduleStatus;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public int getIsFront() {
            return isFront;
        }

        public void setIsFront(int isFront) {
            this.isFront = isFront;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }
    }
}
