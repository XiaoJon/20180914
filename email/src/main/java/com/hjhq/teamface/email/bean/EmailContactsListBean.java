package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 审批权限bean
 * Created by Administrator on 2018/1/17.
 */

public class EmailContactsListBean extends BaseBean implements Serializable {


    /**
     * data : {"dataList":[{"create_time":1519890640113,"employee_id":1,"name":"试试","del_status":"0","id":4,"mail_address":"888@126.com"}],"pageInfo":{"totalPages":1,"pageSize":1000,"totalRows":2,"pageNum":1,"curPageSize":2}}
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
         * dataList : [{"create_time":1519890640113,"employee_id":1,"name":"试试","del_status":"0","id":4,"mail_address":"888@126.com"}]
         * pageInfo : {"totalPages":1,"pageSize":1000,"totalRows":2,"pageNum":1,"curPageSize":2}
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
             * create_time : 1519890640113
             * employee_id : 1
             * name : 试试
             * del_status : 0
             * id : 4
             * mail_address : 888@126.com
             */

            private long create_time;
            private String employee_id;
            private String name;
            private String del_status;
            private String id;
            private String mail_address;

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(String employee_id) {
                this.employee_id = employee_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getMail_address() {
                return mail_address;
            }

            public void setMail_address(String mail_address) {
                this.mail_address = mail_address;
            }
        }
    }
}
