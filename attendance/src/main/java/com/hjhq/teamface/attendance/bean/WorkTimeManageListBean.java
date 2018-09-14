package com.hjhq.teamface.attendance.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/8.
 * Describe：
 */

public class WorkTimeManageListBean extends BaseBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        String name;
        String time11;
        String time12;
        String time21;
        String time22;
        String time31;
        String time32;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime11() {
            return time11;
        }

        public void setTime11(String time11) {
            this.time11 = time11;
        }

        public String getTime12() {
            return time12;
        }

        public void setTime12(String time12) {
            this.time12 = time12;
        }

        public String getTime21() {
            return time21;
        }

        public void setTime21(String time21) {
            this.time21 = time21;
        }

        public String getTime22() {
            return time22;
        }

        public void setTime22(String time22) {
            this.time22 = time22;
        }

        public String getTime31() {
            return time31;
        }

        public void setTime31(String time31) {
            this.time31 = time31;
        }

        public String getTime32() {
            return time32;
        }

        public void setTime32(String time32) {
            this.time32 = time32;
        }
    }
}
