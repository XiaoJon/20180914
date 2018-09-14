package com.hjhq.teamface.custom.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */

public class RepeatCheckResponseBean extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private List<RowBean> row;

        public List<RowBean> getRow() {
            return row;
        }

        public void setRow(List<RowBean> row) {
            this.row = row;
        }

        public static class RowBean {
            /**
             * label : 名称
             * name : text_4145555
             * value : 公司
             */

            private String label;
            private String name;
            private String value;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
