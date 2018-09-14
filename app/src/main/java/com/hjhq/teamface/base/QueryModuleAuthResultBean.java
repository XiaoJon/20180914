package com.hjhq.teamface.base;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2018/5/10.
 */

public class QueryModuleAuthResultBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * saveAuth 0没有 1 有
         */
        private int saveAuth;

        public int getSaveAuth() {
            return saveAuth;
        }

        public void setSaveAuth(int saveAuth) {
            this.saveAuth = saveAuth;
        }
    }
}
