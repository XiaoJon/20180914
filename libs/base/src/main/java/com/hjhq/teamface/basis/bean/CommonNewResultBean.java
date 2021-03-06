package com.hjhq.teamface.basis.bean;

/**
 * 通用新增返回实体
 * @author Administrator
 * @date 2018/6/22
 */

public class CommonNewResultBean extends BaseBean {

    /**
     * data : {"id":1}
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
         * id : 1
         */

        private long dataId;

        public long getId() {
            return dataId;
        }

        public void setId(long id) {
            this.dataId = id;
        }
    }
}
