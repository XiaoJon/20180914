package com.hjhq.teamface.filelib.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库文件列表实体类
 */

public class FileDetailBean extends BaseBean implements Serializable {
    /**
     * data : {"comment_count":0,"basics":{"color":"","create_time":1515394166571,"sign":"1","employee_name":"王克栋9","model_id":"","table_id":1,"url":"company6/2company/1515394166503.jpeg","create_by":1,"siffix":".jpeg","size":88282,"parent_id":1,"name":"1514749442980.jpeg","id":7,"status":"0"},"fabulous_count":0,"fabulous_status":1}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * comment_count : 0
         * basics : {"color":"","create_time":1515394166571,"sign":"1","employee_name":"王克栋9","model_id":"","table_id":1,"url":"company6/2company/1515394166503.jpeg","create_by":1,"siffix":".jpeg","size":88282,"parent_id":1,"name":"1514749442980.jpeg","id":7,"status":"0"}
         * fabulous_count : 0
         * fabulous_status : 1
         */

        private String comment_count;
        private BasicsBean basics;
        private String fabulous_count;
        private String fabulous_status;
        private String download;
        private String upload;
        private String is_manage;

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getUpload() {
            return upload;
        }

        public void setUpload(String upload) {
            this.upload = upload;
        }

        public String getIs_manage() {
            return is_manage;
        }

        public void setIs_manage(String is_manage) {
            this.is_manage = is_manage;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public BasicsBean getBasics() {
            return basics;
        }

        public void setBasics(BasicsBean basics) {
            this.basics = basics;
        }

        public String getFabulous_count() {
            return fabulous_count;
        }

        public void setFabulous_count(String fabulous_count) {
            this.fabulous_count = fabulous_count;
        }

        public String getFabulous_status() {
            return fabulous_status;
        }

        public void setFabulous_status(String fabulous_status) {
            this.fabulous_status = fabulous_status;
        }

        public static class BasicsBean {
            /**
             * color :
             * create_time : 1515394166571
             * sign : 1
             * employee_name : 王克栋9
             * model_id :
             * table_id : 1
             * url : company6/2company/1515394166503.jpeg
             * create_by : 1
             * siffix : .jpeg
             * size : 88282
             * parent_id : 1
             * name : 1514749442980.jpeg
             * id : 7
             * status : 0
             */

            private String color;
            private long create_time;
            private String sign;
            private String employee_name;
            private String model_id;
            private String table_id;
            private String url;
            private String create_by;
            private String siffix;
            private int size;
            private String parent_id;
            private String name;
            private String id;
            private String status;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getModel_id() {
                return model_id;
            }

            public void setModel_id(String model_id) {
                this.model_id = model_id;
            }

            public String getTable_id() {
                return table_id;
            }

            public void setTable_id(String table_id) {
                this.table_id = table_id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getCreate_by() {
                return create_by;
            }

            public void setCreate_by(String create_by) {
                this.create_by = create_by;
            }

            public String getSiffix() {
                return siffix;
            }

            public void setSiffix(String siffix) {
                this.siffix = siffix;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
