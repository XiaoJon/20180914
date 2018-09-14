package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.common.bean.TaskInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */

public class TimeWorkbenchResultBean extends BaseBean {

    /**
     * data : {"dataList":[{"module_bean":"bean1532403663035","module_data_id":"","create_time":1532591564921,"timeId":99,"dataType":4,"approval_data_id":10,"task_id":"166179","del_status":0,"begin_user_id":14,"beanType":1,"process_definition_id":"166171","process_field_v":1532589535199,"process_name":"转换逻辑二","process_status":0,"task_key":"firstTask","process_key":"process1532427776184","process_v":258,"begin_user_name":"赖测三","beanName":"bean1532403663035","id":466,"beanId":10},{"module_bean":"bean1532403663035","module_data_id":"","create_time":1532591564921,"timeId":340,"dataType":4,"approval_data_id":10,"task_id":"166179","del_status":0,"begin_user_id":14,"beanType":1,"process_definition_id":"166171","process_field_v":1532589535199,"process_name":"转换逻辑二","process_status":0,"task_key":"firstTask","process_key":"process1532427776184","process_v":258,"begin_user_name":"赖测三","beanName":"bean1533032427386","id":466,"beanId":10}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TaskInfoBean> dataList;

        public List<TaskInfoBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<TaskInfoBean> dataList) {
            this.dataList = dataList;
        }
    }
}
