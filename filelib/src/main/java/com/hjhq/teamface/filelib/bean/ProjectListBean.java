package com.hjhq.teamface.filelib.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 * Describe：文件库项目列表
 */

public class ProjectListBean extends BaseBean {

    /**
     * data : {"dataList":[{"data_id":84,"file_name":"可修改项目负责人","library_type":"0","id":541}],"pageInfo":{"totalPages":1,"pageSize":70,"totalRows":19,"pageNum":1,"curPageSize":19}}
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
         * dataList : [{"data_id":84,"file_name":"可修改项目负责人","library_type":"0","id":541}]
         * pageInfo : {"totalPages":1,"pageSize":70,"totalRows":19,"pageNum":1,"curPageSize":19}
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
             * data_id : 84
             * file_name : 可修改项目负责人
             * library_type : 0
             * id : 541
             */

            private String data_id;
            private String file_name;
            private String library_type;
            private String id;

            public String getData_id() {
                return data_id;
            }

            public void setData_id(String data_id) {
                this.data_id = data_id;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
