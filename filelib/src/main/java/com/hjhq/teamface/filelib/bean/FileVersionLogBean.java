package com.hjhq.teamface.filelib.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库文件列表实体类
 */

public class FileVersionLogBean extends BaseBean implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * midf_time : 1515394664038
         * file_id : 8
         * name : 1514749442980.jpeg
         * employee_name :
         * id : 2
         * midf_by : 1
         * url : company6/5company/1515394663653.jpeg
         * picture :
         */

        private String midf_time;
        private String file_id;
        private String name;
        private String employee_name;
        private String id;
        private String midf_by;
        private String url;
        private String picture;
        private String size;
        private String suffix;

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getMidf_time() {
            return midf_time;
        }

        public void setMidf_time(String midf_time) {
            this.midf_time = midf_time;
        }

        public String getFile_id() {
            return file_id;
        }

        public void setFile_id(String file_id) {
            this.file_id = file_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMidf_by() {
            return midf_by;
        }

        public void setMidf_by(String midf_by) {
            this.midf_by = midf_by;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
