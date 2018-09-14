package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */

public class PersonalTaskStatusResultBean extends BaseBean {
    /**
     * data : {"dataList":[{"read_time":1534739328809,"read_status":"1","employee_pic":"http://192.168.1.183:8081/custom-gateway/common/file/imageDownload?bean=company&fileName=1534232592741/blob&fileSize=5456","employee_name":"徐兵"}]}
     */

    private List<TaskStatusBean> data;

    public List<TaskStatusBean> getData() {
        return data;
    }

    public void setData(List<TaskStatusBean> data) {
        this.data = data;
    }

}
