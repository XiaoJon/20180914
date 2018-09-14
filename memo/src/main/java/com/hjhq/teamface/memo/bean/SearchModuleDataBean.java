package com.hjhq.teamface.memo.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.RowDataBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 * Describe：
 */

public class SearchModuleDataBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 30
         * title : 789
         */

        private IdBean id;
        private List<RowDataBean> row;
        private String color;

        public IdBean getId() {
            return id;
        }

        public void setId(IdBean id) {
            this.id = id;
        }

        public List<RowDataBean> getRow() {
            return row;
        }

        public void setRow(List<RowDataBean> row) {
            this.row = row;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }


    }


    public static class IdBean {
        /**
         * name : id
         * label : 主键id
         * value : 1
         */

        private String name;
        private String label;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
