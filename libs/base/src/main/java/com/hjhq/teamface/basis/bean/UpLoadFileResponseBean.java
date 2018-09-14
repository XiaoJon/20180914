package com.hjhq.teamface.basis.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lx
 * @date 2017/5/18
 */

public class UpLoadFileResponseBean extends BaseBean implements Serializable {

    private List<UploadFileBean> data;

    public List<UploadFileBean> getData() {
        return data;
    }

    public void setData(List<UploadFileBean> data) {
        this.data = data;
    }

}
