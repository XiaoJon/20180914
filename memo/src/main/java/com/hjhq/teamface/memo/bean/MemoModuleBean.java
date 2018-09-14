package com.hjhq.teamface.memo.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 17:59 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class
 * @anthor Administrator TEL:
 * @time 2017/5/13 17:59
 * @change
 * @chang time
 * @class describe
 */
public class MemoModuleBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * chinese_name : 测试删除
         * create_by : 11
         * create_time : 1512613203611
         * modify_time :
         * icon : el-icon-star-off
         * topper : 1
         * del_status : 0
         * id : 15
         * modify_by :
         * application_id : 11
         * english_name : bean1512613237585
         */

        private String chinese_name;
        private String create_by;
        private long create_time;
        private String modify_time;
        private String icon;
        private String topper;
        private String del_status;
        private String id;
        private String modify_by;
        private String application_id;
        private String english_name;

        public String getChinese_name() {
            return chinese_name;
        }

        public void setChinese_name(String chinese_name) {
            this.chinese_name = chinese_name;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getModify_by() {
            return modify_by;
        }

        public void setModify_by(String modify_by) {
            this.modify_by = modify_by;
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
    }
}