package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 数据列表返回Bean
 *
 * @author lx
 * @date 2017/9/16
 */

public class DataTempResultBean extends BaseBean {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private List<DataListBean> dataList;
        private PageInfo pageInfo;
        private RowBean operationInfo;

        public static class DataListBean implements Serializable {
            private Row row;
            private RowBean id;
            private String color;
            private boolean isCheck;

            public RowBean getId() {
                return id;
            }

            public void setId(RowBean id) {
                this.id = id;
            }

            public Row getRow() {
                return row;
            }

            public void setRow(Row row) {
                this.row = row;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public static class Row implements Serializable {
                private List<RowBean> row1;
                private List<RowBean> row2;
                private List<RowBean> row3;

                public List<RowBean> getRow1() {
                    return row1;
                }

                public void setRow1(List<RowBean> row1) {
                    this.row1 = row1;
                }

                public List<RowBean> getRow2() {
                    return row2;
                }

                public void setRow2(List<RowBean> row2) {
                    this.row2 = row2;
                }

                public List<RowBean> getRow3() {
                    return row3;
                }

                public void setRow3(List<RowBean> row3) {
                    this.row3 = row3;
                }

            }
        }

        public List<DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public RowBean getOperationInfo() {
            return operationInfo;
        }

        public void setOperationInfo(RowBean operationInfo) {
            this.operationInfo = operationInfo;
        }
    }

}
