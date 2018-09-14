package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/24.
 */

public class PersonalTaskResultBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 个人任务
         * projectId : 141
         */

        private String title;
        private String projectId;
        private List<PersonalTaskBean> tasks;

        private boolean isQuest;
        private boolean visibility;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public List<PersonalTaskBean> getTasks() {
            return tasks;
        }

        public void setTasks(List<PersonalTaskBean> tasks) {
            this.tasks = tasks;
        }

        public boolean isVisibility() {
            return visibility;
        }

        public void setVisibility(boolean visibility) {
            this.visibility = visibility;
        }

        public boolean isQuest() {
            return isQuest;
        }

        public void setQuest(boolean quest) {
            isQuest = quest;
        }
    }
}
