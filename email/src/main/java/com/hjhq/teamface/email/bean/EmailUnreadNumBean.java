package com.hjhq.teamface.email.bean;


import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 * Describe：
 */

public class EmailUnreadNumBean extends BaseBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mail_box_id : 4
         * count : 10
         */

        private String mail_box_id;
        private String count;

        public String getMail_box_id() {
            return mail_box_id;
        }

        public void setMail_box_id(String mail_box_id) {
            this.mail_box_id = mail_box_id;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
