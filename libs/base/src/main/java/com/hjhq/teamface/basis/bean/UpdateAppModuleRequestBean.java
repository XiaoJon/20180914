package com.hjhq.teamface.basis.bean;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/3/1
 */

public class UpdateAppModuleRequestBean {
    private List<UpdateAppModuleBean> data;

    public List<UpdateAppModuleBean> getData() {
        return data;
    }

    public void setData(List<UpdateAppModuleBean> data) {
        this.data = data;
    }

    public static class UpdateAppModuleBean {

        private String english_name;
        private String chinese_name;
        private String id;
        private String icon_type;
        private String icon_url;
        private String icon_color;

        public UpdateAppModuleBean() {

        }

        public UpdateAppModuleBean(String id, String english_name, String chinese_name, String icon_type, String icon_url, String icon_color) {
            this.id = id;
            this.english_name = english_name;
            this.chinese_name = chinese_name;
            this.icon_type = icon_type;
            this.icon_url = icon_url;
            this.icon_color = icon_color;
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

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getIcon_color() {
            return icon_color;
        }

        public void setIcon_color(String icon_color) {
            this.icon_color = icon_color;
        }
    }
}
