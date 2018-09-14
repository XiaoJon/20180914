package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * 生成条形码结果数据量类
 *
 * @author Administrator
 * @date
 */

public class GetBarcodeImgBean extends BaseBean implements Serializable {

    /**
     * data : {"barcodeValue":1534732663616}
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
         * barcodeValue : 1534732663616
         */

        private String barcodePic;

        public String getBarcodePic() {
            return barcodePic;
        }

        public void setBarcodePic(String barcodePic) {
            this.barcodePic = barcodePic;
        }
    }
}
