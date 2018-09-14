package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 18:34 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class 获取职位列表
 * @anthor Administrator TEL:
 * @time 2017/5/13 18:34
 * @change
 * @chang time
 * @class describe
 */
public class GetPositionListResponseBean {

    /**
     * data : {"totalRows":3,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":3,"list":[{"id":3365301732261888,"createDate":1494236691714,"disabled":null,"companyId":3350770212126720,"position":"iOS","isDefault":0},{"id":3366624072679424,"createDate":1494317400968,"disabled":null,"companyId":3350770212126720,"position":"安卓","isDefault":0},{"id":3372359459733504,"createDate":1494667461213,"disabled":null,"companyId":3350770212126720,"position":"android1","isDefault":0}]}
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
         * totalRows : 3
         * totalPages : 1
         * pageNum : 1
         * pageSize : 20
         * curPageSize : 3
         * list : [{"id":3365301732261888,"createDate":1494236691714,"disabled":null,"companyId":3350770212126720,"position":"iOS","isDefault":0},{"id":3366624072679424,"createDate":1494317400968,"disabled":null,"companyId":3350770212126720,"position":"安卓","isDefault":0},{"id":3372359459733504,"createDate":1494667461213,"disabled":null,"companyId":3350770212126720,"position":"android1","isDefault":0}]
         */

        private int totalRows;
        private int totalPages;
        private int pageNum;
        private int pageSize;
        private int curPageSize;
        private List<PositionBean> list;

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

        public List<PositionBean> getList() {
            return list;
        }

        public void setList(List<PositionBean> list) {
            this.list = list;
        }

    }
}
