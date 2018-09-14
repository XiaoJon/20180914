package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Ked ,the Administrator, on 2017/5/13 17:38 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.teammessage.bean
 * @class describe
 * @anthor Administrator TEL:
 * @time 2017/5/13 17:38
 * @change
 * @chang time
 * @class describe
 */
public class VerifTeleResponseBean extends BaseBean {

    /**
     * data : {"result":0}
     * 0不存在1已存在
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
         * result : 0
         */

        private int result;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}
