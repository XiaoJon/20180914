package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 机器人
 * Created by lx on 2017/6/5.
 */

public class RobotBean extends BaseBean {

    /**
     * data : {"taskDetail":[{"id":110,"createDate":null,"disabled":null,"assistName":"任务助手","assistId":110,"imUserName":null,"noReadNum":0},{"id":111,"createDate":null,"disabled":null,"assistName":"日程助手","assistId":111,"imUserName":null,"noReadNum":0},{"id":112,"createDate":null,"disabled":null,"assistName":"随手记助手","assistId":112,"imUserName":null,"noReadNum":0},{"id":113,"createDate":null,"disabled":null,"assistName":"文件库助手","assistId":113,"imUserName":null,"noReadNum":0},{"id":114,"createDate":null,"disabled":null,"assistName":"审批助手","assistId":114,"imUserName":null,"noReadNum":0},{"id":115,"createDate":null,"disabled":null,"assistName":"公告助手","assistId":115,"imUserName":null,"noReadNum":0},{"id":116,"createDate":null,"disabled":null,"assistName":"投诉建议助手","assistId":116,"imUserName":null,"noReadNum":0},{"id":117,"createDate":null,"disabled":null,"assistName":"工作汇报助手","assistId":117,"imUserName":null,"noReadNum":0}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<HelperBean> taskDetail;

        public List<HelperBean> getTaskDetail() {
            return taskDetail;
        }

        public void setTaskDetail(List<HelperBean> taskDetail) {
            this.taskDetail = taskDetail;
        }
    }
}
