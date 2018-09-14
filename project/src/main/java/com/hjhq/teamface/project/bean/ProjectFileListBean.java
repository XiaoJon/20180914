package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 * Describe：
 */

public class ProjectFileListBean extends BaseBean {

    /**
     * data : {"dataList":[{"project_type":3,"color":"","create_time":1530069954459,"file_name":"0621.xls","library_type":"1","del_status":"0","employee_name":"请叫我娣娣","sort":"","suffix":".xls","type":0,"url":"project2/59/260/262/1530069944450.xls","create_by":4,"size":26112,"data_id":279,"parent_id":263,"id":280},{"project_type":3,"color":"","create_time":1530073473717,"file_name":"10026.wav","library_type":"1","del_status":"0","employee_name":"chenzhuangli","sort":"","suffix":".wav","type":0,"url":"project2/59/260/262/1530073473634.wav","create_by":21,"size":4550044,"data_id":281,"parent_id":263,"id":282},{"project_type":3,"color":"","create_time":1530073489520,"file_name":"timg.gif","library_type":"1","del_status":"0","employee_name":"chenzhuangli","sort":"","suffix":".gif","type":0,"url":"project2/59/260/262/1530073489484.gif","create_by":21,"size":255869,"data_id":282,"parent_id":263,"id":283},{"project_type":3,"color":"","create_time":1530073500295,"file_name":"mov_bbb.mp4","library_type":"1","del_status":"0","employee_name":"chenzhuangli","sort":"","suffix":".mp4","type":0,"url":"project2/59/260/262/1530073500257.mp4","create_by":21,"size":788493,"data_id":283,"parent_id":263,"id":284},{"project_type":3,"color":"","create_time":1530087757502,"file_name":"appIcon5.png","library_type":"1","del_status":"0","employee_name":"请叫我娣娣","sort":"","suffix":".png","type":0,"url":"project2/59/260/262/1530087757459.png","create_by":4,"size":1141,"data_id":289,"parent_id":263,"id":290},{"project_type":3,"color":"","create_time":1530087763740,"file_name":"appIcon5.png","library_type":"1","del_status":"0","employee_name":"请叫我娣娣","sort":"","suffix":".png","type":0,"url":"project2/59/260/262/1530087763704.png","create_by":4,"size":1141,"data_id":290,"parent_id":263,"id":291}],"pageInfo":{"totalPages":1,"pageSize":20,"totalRows":6,"pageNum":1,"curPageSize":6}}
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
         * dataList : [{"project_type":3,"color":"","create_time":1530069954459,"file_name":"0621.xls","library_type":"1","del_status":"0","employee_name":"请叫我娣娣","sort":"","suffix":".xls","type":0,"url":"project2/59/260/262/1530069944450.xls","create_by":4,"size":26112,"data_id":279,"parent_id":263,"id":280},{"project_type":3,"color":"","create_time":1530073473717,"file_name":"10026.wav","library_type":"1","del_status":"0","employee_name":"chenzhuangli","sort":"","suffix":".wav","type":0,"url":"project2/59/260/262/1530073473634.wav","create_by":21,"size":4550044,"data_id":281,"parent_id":263,"id":282},{"project_type":3,"color":"","create_time":1530073489520,"file_name":"timg.gif","library_type":"1","del_status":"0","employee_name":"chenzhuangli","sort":"","suffix":".gif","type":0,"url":"project2/59/260/262/1530073489484.gif","create_by":21,"size":255869,"data_id":282,"parent_id":263,"id":283},{"project_type":3,"color":"","create_time":1530073500295,"file_name":"mov_bbb.mp4","library_type":"1","del_status":"0","employee_name":"chenzhuangli","sort":"","suffix":".mp4","type":0,"url":"project2/59/260/262/1530073500257.mp4","create_by":21,"size":788493,"data_id":283,"parent_id":263,"id":284},{"project_type":3,"color":"","create_time":1530087757502,"file_name":"appIcon5.png","library_type":"1","del_status":"0","employee_name":"请叫我娣娣","sort":"","suffix":".png","type":0,"url":"project2/59/260/262/1530087757459.png","create_by":4,"size":1141,"data_id":289,"parent_id":263,"id":290},{"project_type":3,"color":"","create_time":1530087763740,"file_name":"appIcon5.png","library_type":"1","del_status":"0","employee_name":"请叫我娣娣","sort":"","suffix":".png","type":0,"url":"project2/59/260/262/1530087763704.png","create_by":4,"size":1141,"data_id":290,"parent_id":263,"id":291}]
         * pageInfo : {"totalPages":1,"pageSize":20,"totalRows":6,"pageNum":1,"curPageSize":6}
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


        public static class DataListBean implements Serializable {
            /**
             * project_type : 3
             * color :
             * create_time : 1530069954459
             * file_name : 0621.xls
             * library_type : 1
             * del_status : 0
             * employee_name : 请叫我娣娣
             * sort :
             * suffix : .xls
             * type : 0
             * url : project2/59/260/262/1530069944450.xls
             * create_by : 4
             * size : 26112
             * data_id : 279
             * parent_id : 263
             * id : 280
             */

            private String project_type;
            private String color;
            private String create_time;
            private String file_name;
            private String library_type;
            private String del_status;
            private String employee_name;
            private String sort;
            private String suffix;
            private String type;
            private String url;
            private String create_by;
            private String size;
            private String data_id;
            private String parent_id;
            private String id;

            public String getProject_type() {
                return project_type;
            }

            public void setProject_type(String project_type) {
                this.project_type = project_type;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getFile_name() {
                return file_name;
            }

            public void setFile_name(String file_name) {
                this.file_name = file_name;
            }

            public String getLibrary_type() {
                return library_type;
            }

            public void setLibrary_type(String library_type) {
                this.library_type = library_type;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getEmployee_name() {
                return employee_name;
            }

            public void setEmployee_name(String employee_name) {
                this.employee_name = employee_name;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getSuffix() {
                return suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
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

            public String getCreate_by() {
                return create_by;
            }

            public void setCreate_by(String create_by) {
                this.create_by = create_by;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getData_id() {
                return data_id;
            }

            public void setData_id(String data_id) {
                this.data_id = data_id;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
