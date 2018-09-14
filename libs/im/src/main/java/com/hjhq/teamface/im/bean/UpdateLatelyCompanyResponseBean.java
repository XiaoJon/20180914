package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 18:48 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class describe
 * @anthor Administrator TEL:
 * @time 2017/5/13 18:48
 * @change
 * @chang time
 * @class describe
 */
public class UpdateLatelyCompanyResponseBean extends BaseBean {

    /**
     * data : {"totalRows":10,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":10,"list":[{"employeeName":"尹明亮","isDefault":1,"companyName":"明亮公司","employeeId":3350770213208064,"id":3350770212126720,"employeeStatus":1,"createDate":1493349758112},{"employeeName":"亮亮","isDefault":1,"companyName":"好机会","employeeId":3350869805596672,"id":3345143040458752,"employeeStatus":1,"createDate":1493006302810},{"employeeName":"亮亮","isDefault":1,"companyName":"邹毅的团队","employeeId":3350981622972416,"id":3350965542436864,"employeeStatus":1,"createDate":1493361680128},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试","employeeId":3369580680511488,"id":3369580679675904,"employeeStatus":0,"createDate":1494497857938},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试","employeeId":3370938612613120,"id":3370938611482624,"employeeStatus":0,"createDate":1494580739518},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试2","employeeId":3371071649300480,"id":3371071648481280,"employeeStatus":0,"createDate":1494588859452},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试2","employeeId":3372231120470016,"id":3372231119634432,"employeeStatus":0,"createDate":1494659627955},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试3","employeeId":3372231230046208,"id":3372231229112320,"employeeStatus":0,"createDate":1494659634637},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试4","employeeId":3372231344963584,"id":3372231344193536,"employeeStatus":0,"createDate":1494659641661},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试5","employeeId":3372231437238272,"id":3372231436320768,"employeeStatus":0,"createDate":1494659647284}]}
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
         * totalRows : 10
         * totalPages : 1
         * pageNum : 1
         * pageSize : 20
         * curPageSize : 10
         * list : [{"employeeName":"尹明亮","isDefault":1,"companyName":"明亮公司","employeeId":3350770213208064,"id":3350770212126720,"employeeStatus":1,"createDate":1493349758112},{"employeeName":"亮亮","isDefault":1,"companyName":"好机会","employeeId":3350869805596672,"id":3345143040458752,"employeeStatus":1,"createDate":1493006302810},{"employeeName":"亮亮","isDefault":1,"companyName":"邹毅的团队","employeeId":3350981622972416,"id":3350965542436864,"employeeStatus":1,"createDate":1493361680128},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试","employeeId":3369580680511488,"id":3369580679675904,"employeeStatus":0,"createDate":1494497857938},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试","employeeId":3370938612613120,"id":3370938611482624,"employeeStatus":0,"createDate":1494580739518},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试2","employeeId":3371071649300480,"id":3371071648481280,"employeeStatus":0,"createDate":1494588859452},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试2","employeeId":3372231120470016,"id":3372231119634432,"employeeStatus":0,"createDate":1494659627955},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试3","employeeId":3372231230046208,"id":3372231229112320,"employeeStatus":0,"createDate":1494659634637},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试4","employeeId":3372231344963584,"id":3372231344193536,"employeeStatus":0,"createDate":1494659641661},{"employeeName":"尹明亮","isDefault":0,"companyName":"公司名称测试5","employeeId":3372231437238272,"id":3372231436320768,"employeeStatus":0,"createDate":1494659647284}]
         */

        private int totalRows;
        private int totalPages;
        private int pageNum;
        private int pageSize;
        private int curPageSize;
        private List<ListBean> list;

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurPageSize() {
            return curPageSize;
        }

        public void setCurPageSize(int curPageSize) {
            this.curPageSize = curPageSize;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * employeeName : 尹明亮
             * isDefault : 1
             * companyName : 明亮公司
             * employeeId : 3350770213208064
             * id : 3350770212126720
             * employeeStatus : 1
             * createDate : 1493349758112
             */

            private String employeeName;
            private int isDefault;
            private String companyName;
            private long employeeId;
            private long id;
            private int employeeStatus;
            private long createDate;

            public String getEmployeeName() {
                return employeeName;
            }

            public void setEmployeeName(String employeeName) {
                this.employeeName = employeeName;
            }

            public int getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(int isDefault) {
                this.isDefault = isDefault;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public long getEmployeeId() {
                return employeeId;
            }

            public void setEmployeeId(long employeeId) {
                this.employeeId = employeeId;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getEmployeeStatus() {
                return employeeStatus;
            }

            public void setEmployeeStatus(int employeeStatus) {
                this.employeeStatus = employeeStatus;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }
        }
    }
}
