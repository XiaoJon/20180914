package com.hjhq.teamface.basis.bean;


import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/8.
 * Describe：
 */

public class EmailListBean extends BaseBean {

    /**
     * data : {"dataList":[{"mail_content":"测试邮件可以不回复","from_recipient":"18688783645@126.com","subject":"邮件测试","approval_status":"0","bcc_recipients":"","mail_source":"0","embedded_images":"","read_status":"0","is_track":"1","bcc_setting":"0","attachments_name":"","to_recipients":[{"mail_account":"dengshunli12345678@qq.com","employee_name":"张三"}],"cc_recipients":"","is_emergent":"0","is_plain":"0","send_status":"1","is_notification":"0","id":33}],"pageInfo":""}
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
         * dataList : [{"mail_content":"测试邮件可以不回复","from_recipient":"18688783645@126.com","subject":"邮件测试","approval_status":"0","bcc_recipients":"","mail_source":"0","embedded_images":"","read_status":"0","is_track":"1","bcc_setting":"0","attachments_name":"","to_recipients":[{"mail_account":"dengshunli12345678@qq.com","employee_name":"张三"}],"cc_recipients":"","is_emergent":"0","is_plain":"0","send_status":"1","is_notification":"0","id":33}]
         * pageInfo :
         */

        private PageInfo pageInfo;
        private ArrayList<EmailBean> dataList;

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public ArrayList<EmailBean> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<EmailBean> dataList) {
            this.dataList = dataList;
        }

    }

}
