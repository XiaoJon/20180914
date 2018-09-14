package com.hjhq.teamface.oa.login.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2017/10/9.
 * Describe：
 */

public class CompleteCompanyResponseBean extends BaseBean {

    /**
     * data : {"companyId ":1}
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
         * companyId  : 1
         */

        private Long companyId;

        public Long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Long companyId) {
            this.companyId = companyId;
        }
    }
}
