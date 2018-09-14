package com.hjhq.teamface.filelib.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库文件列表实体类
 */

public class FolderAuthDetailBean extends BaseBean implements Serializable {
    /**
     * data : {"basics":{"name":"私有文件夹","type":"1"},"manage":[{"file_id":4,"employee_id":1,"employee_name":"王克栋9","id":3,"sign_type":"0","picture":""}],"setting":[{"preview":"1","download":"","upload":"","file_id":4,"employee_id":1,"employee_name":"王克栋9","id":1,"picture":""}]}
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
         * basics : {"name":"私有文件夹","type":"1"}
         * manage : [{"file_id":4,"employee_id":1,"employee_name":"王克栋9","id":3,"sign_type":"0","picture":""}]
         * setting : [{"preview":"1","download":"","upload":"","file_id":4,"employee_id":1,"employee_name":"王克栋9","id":1,"picture":""}]
         */

        private BasicsBean basics;
        private List<SettingBean> manage;
        private List<SettingBean> setting;

        public BasicsBean getBasics() {
            return basics;
        }

        public void setBasics(BasicsBean basics) {
            this.basics = basics;
        }

        public List<SettingBean> getManage() {
            return manage;
        }

        public void setManage(List<SettingBean> manage) {
            this.manage = manage;
        }

        public List<SettingBean> getSetting() {
            return setting;
        }

        public void setSetting(List<SettingBean> setting) {
            this.setting = setting;
        }

        public static class BasicsBean {
            /**
             * name : 私有文件夹
             * type : 1
             */

            private String name;
            private String type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }




    }
    public static class SettingBean {
        /**
         * preview : 1
         * download :
         * upload :
         * file_id : 4
         * employee_id : 1
         * employee_name : 王克栋9
         * id : 1
         * picture :
         */

        private String preview;
        private String download;
        private String upload;
        private String file_id;
        private String employee_id;
        private String employee_name;
        private String id;
        private String picture;
        private String sign_type;

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

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

        public String getFile_id() {
            return file_id;
        }

        public void setFile_id(String file_id) {
            this.file_id = file_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
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

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }
    }
}
