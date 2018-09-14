package com.hjhq.teamface.filelib.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/01/03
 * Describe：文件库文件列表实体类
 */

public class FileListResBean extends BaseBean implements Serializable {

    /**
     * data : {"dataList":[{"name":"名字","color":"#CCCCCC","create_time":1514945072268,"size":"","employee_name":"王克栋","id":6,"type":"","url":"company9/5company/"}],"pageInfo":{"totalPages":1,"totalRows":5,"curPageSize":5}}
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
         * dataList : [{"name":"名字","color":"#CCCCCC","create_time":1514945072268,"size":"","employee_name":"王克栋","id":6,"type":"","url":"company9/5company/"}]
         * pageInfo : {"totalPages":1,"totalRows":5,"curPageSize":5}
         */

        private PageInfo pageInfo;
        private List<DataListBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {
            /**
             * name : 名字
             * color : #CCCCCC
             * create_time : 1514945072268
             * size :
             * employee_name : 王克栋
             * id : 6
             * type :
             * url : company9/5company/
             */

            private String name;
            private String color;
            private long create_time;
            private String size;
            private String employee_name;
            private String id;
            //0公开,1私有
            private String type;
            private String url;
            private String model_id;
            private String table_id;
            private String picture;
            private String create_by;
            private String parent_id;
            private String status;
            private String sign;
            //用于我的共享和与我共享
            String file_id;
            //权限
            private String preview;
            private String download;
            private String upload;
            //是否是管理员
            private String is_manage;
            private String siffix;
            private String style;


            private boolean checked;

            public String getStyle() {
                return style;
            }

            public void setStyle(String style) {
                this.style = style;
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

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getCreate_by() {
                return create_by;
            }

            public void setCreate_by(String create_by) {
                this.create_by = create_by;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

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

            public String getIs_manage() {
                return is_manage;
            }

            public void setIs_manage(String is_manage) {
                this.is_manage = is_manage;
            }

            public String getSiffix() {
                return siffix;
            }

            public void setSiffix(String siffix) {
                this.siffix = siffix;
            }
        }
    }
}
