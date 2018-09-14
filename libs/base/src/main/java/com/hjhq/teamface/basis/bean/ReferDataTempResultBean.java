package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 关联组件列表数据列表返回Bean
 *
 * @author lx
 * @date 2017/9/16
 */

public class ReferDataTempResultBean extends BaseBean {


    private List<DataBean> data;

    public static class DataBean implements Serializable {
        private List<RowBean> row;
        private RowBean id;
        private Map<String, Object> relationField;

        public RowBean getId() {
            return id;
        }

        public void setId(RowBean id) {
            this.id = id;
        }

        public List<RowBean> getRow() {
            return row;
        }

        public void setRow(List<RowBean> row) {
            this.row = row;
        }

        public Map<String, Object> getRelationField() {
            return relationField;
        }

        public void setRelationField(Map<String, Object> relationField) {
            this.relationField = relationField;
        }
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
