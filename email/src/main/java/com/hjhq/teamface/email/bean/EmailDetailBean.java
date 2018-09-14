package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmailBean;

import java.io.Serializable;

public class EmailDetailBean extends BaseBean implements Serializable {


    /**
     * data : {"mail_content":"测试邮件可以不回复","from_recipient":"18688783645@126.com","create_time":1520389046953,"subject":"邮件测试","approval_status":"0","bcc_recipients":[{"mail_account":"dengshunli12345678@qq.com","employee_name":"张三"}],"mail_source":"0","embedded_images":"","read_status":"0","is_track":"1","draft_status":"0","bcc_setting":"0","attachments_name":[{"fileName":"Screenshot_20180311-045445.jpg","fileSize":"1139205","fileType":"jpg","fileUrl":"http://192.168.1.9:8080/custom-gateway/common/file/imageDownload?bean=company&fileName=1/company/1520822247999.Screenshot_20180311-045445.jpg"}],"to_recipients":[{"mail_account":"dengshunli12345678@qq.com","employee_name":"张三"}],"cc_recipients":[{"mail_account":"dengshunli12345678@qq.com","employee_name":"张三"}],"is_emergent":"0","is_plain":"0","timer_status":"0","send_status":"1","is_notification":"0","id":32,"mail_tags":""}
     */

    private EmailBean data;

    public EmailBean getData() {
        return data;
    }

    public void setData(EmailBean data) {
        this.data = data;
    }
}