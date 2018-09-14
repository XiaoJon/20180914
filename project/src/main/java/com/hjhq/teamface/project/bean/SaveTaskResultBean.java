package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * 通用新增返回实体
 * @author Administrator
 * @date 2018/6/22
 */

public class SaveTaskResultBean extends BaseBean {

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

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
