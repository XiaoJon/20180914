package com.hjhq.teamface.oa.approve.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * 审批未读数
 * Created by Administrator on 2018/1/12.
 */

public class ApproveUnReadCountResponseBean extends BaseBean {

    /**
     * data : {"treatCount":1,"copyCount":1}
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
         * treatCount : 1
         * copyCount : 1
         */

        private String treatCount;
        private String copyCount;

        public String getTreatCount() {
            return treatCount;
        }

        public void setTreatCount(String treatCount) {
            this.treatCount = treatCount;
        }

        public String getCopyCount() {
            return copyCount;
        }

        public void setCopyCount(String copyCount) {
            this.copyCount = copyCount;
        }
    }
}
