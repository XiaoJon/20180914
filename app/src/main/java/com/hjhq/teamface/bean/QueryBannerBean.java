package com.hjhq.teamface.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by Administrator on 2018/6/15.
 */

public class QueryBannerBean extends BaseBean {

    /**
     * data : {"banner":"[\"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=appIcon1.png&fileSize=1334\",\"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=com.jpg&fileSize=10504\",\"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=appIcon7.png&fileSize=974\",\"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=appIcon1.png&fileSize=1334\",\"http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=Picture_02_Desert.jpg&fileSize=234444\"]"}
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
         * banner : ["http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=appIcon1.png&fileSize=1334","http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=com.jpg&fileSize=10504","http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=appIcon7.png&fileSize=974","http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=appIcon1.png&fileSize=1334","http://192.168.1.180:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=Picture_02_Desert.jpg&fileSize=234444"]
         */

        private String banner;

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }
    }
}
