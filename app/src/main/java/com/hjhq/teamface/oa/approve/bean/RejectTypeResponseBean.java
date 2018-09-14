package com.hjhq.teamface.oa.approve.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 驳回方式
 * Created by Administrator on 2018/1/18.
 */

public class RejectTypeResponseBean extends BaseBean {

    /**
     * data : {"rejectType":[{"id":0,"label":"驳回给上一节点审批人"},{"id":1,"label":"驳回到发起人"},{"id":2,"label":"驳回到指定节点"},{"id":3,"label":"驳回并结束"}],"historicTaskList":[{"taskId":54896,"taskKey":"task15145641231568","taskName":"人事部"},{"taskId":65223,"taskKey":"task15145641231568","taskName":"研发部"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<RejectTypeBean> rejectType;
        private List<HistoricTaskListBean> historicTaskList;

        public List<RejectTypeBean> getRejectType() {
            return rejectType;
        }

        public void setRejectType(List<RejectTypeBean> rejectType) {
            this.rejectType = rejectType;
        }

        public List<HistoricTaskListBean> getHistoricTaskList() {
            return historicTaskList;
        }

        public void setHistoricTaskList(List<HistoricTaskListBean> historicTaskList) {
            this.historicTaskList = historicTaskList;
        }

        public static class RejectTypeBean implements Serializable {
            /**
             * id : 0
             * label : 驳回给上一节点审批人
             */

            private String id;
            private String label;
            private boolean isCheck;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }

        public static class HistoricTaskListBean implements Serializable {
            /**
             * taskId : 54896
             * taskKey : task15145641231568
             * taskName : 人事部
             */

            private String taskId;
            private String taskKey;
            private String taskName;
            private boolean isCheck;

            public String getTaskId() {
                return taskId;
            }

            public void setTaskId(String taskId) {
                this.taskId = taskId;
            }

            public String getTaskKey() {
                return taskKey;
            }

            public void setTaskKey(String taskKey) {
                this.taskKey = taskKey;
            }

            public String getTaskName() {
                return taskName;
            }

            public void setTaskName(String taskName) {
                this.taskName = taskName;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }
        }
    }
}
