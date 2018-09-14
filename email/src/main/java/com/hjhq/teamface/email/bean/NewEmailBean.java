package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.AttachmentBean;
import com.hjhq.teamface.basis.bean.ReceiverBean;

import java.util.List;

/**
 * 审批权限bean
 * Created by Administrator on 2018/1/17.
 */

public class NewEmailBean {

    /**
     * subject : 邮件测试
     * account_id : 1
     * from_recipient : 163@163.com
     * mail_content : 凤凰山
     * to_recipients : [{"employee_name":"张三","mail_account":"126@126.com"}]
     * cc_recipients : [{"employee_name":"张三","mail_account":"126@126.com"}]
     * bcc_recipients : [{"employee_name":"张三","mail_account":"126@126.com"}]
     * cc_setting : 0
     * bcc_setting : 0
     * single_show : 0
     * is_emergent : 0
     * is_notification : 0
     * is_plain : 0
     * is_track : 1
     * is_encryption : 0
     * is_signature : 0
     * signature_id : 0
     * mail_source : 0
     * attachments_name : [{"fileName":"","fileType":"jpg","fileSize":"12323","fileUrl":"http://234324.dd/fd.jpg"}]
     */
    //邮件id
    private String id;
    //邮件标题/主题
    private String subject;
    //账号ID
    private long account_id;
    //发件人账号
    private String from_recipient;
    //邮件内容
    private String mail_content;
    //审批人
    private String personnel_approverBy;
    //审批抄送人
    private String personnel_ccTo;
    //是否选择抄送人 0 否 1 是
    private int cc_setting;
    //是否选择密送人 0 否 1 是
    private int bcc_setting;
    //是否群发单显，0 否 1 是
    private int single_show;
    //是否为紧急邮件  0否 1 是
    private int is_emergent;
    //是否需要回执 0 否 1 是
    private int is_notification;
    //是否为纯文本  0 否 1 是
    private int is_plain;
    //是否追踪 0 否1 是
    private int is_track;
    //是否加密 0 否 1 是
    private int is_encryption;
    //是否签名 0 否 1 是
    private int is_signature;
    //签名ID
    private int signature_id;
    //邮件来源 0 PC 1 手机
    private int mail_source;
    private String timer_task_time;

    public String getTimer_task_time() {
        return timer_task_time;
    }

    public void setTimer_task_time(String timer_task_time) {
        this.timer_task_time = timer_task_time;
    }

    //收件人
    private List<ReceiverBean> to_recipients;
    //抄送人
    private List<ReceiverBean> cc_recipients;
    //密送人
    private List<ReceiverBean> bcc_recipients;
    //附件
    private List<AttachmentBean> attachments_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public String getFrom_recipient() {
        return from_recipient;
    }

    public void setFrom_recipient(String from_recipient) {
        this.from_recipient = from_recipient;
    }

    public String getMail_content() {
        return mail_content;
    }

    public void setMail_content(String mail_content) {
        this.mail_content = mail_content;
    }

    public String getPersonnel_approverBy() {
        return personnel_approverBy;
    }

    public void setPersonnel_approverBy(String personnel_approverBy) {
        this.personnel_approverBy = personnel_approverBy;
    }

    public String getPersonnel_ccTo() {
        return personnel_ccTo;
    }

    public void setPersonnel_ccTo(String personnel_ccTo) {
        this.personnel_ccTo = personnel_ccTo;
    }

    public int getCc_setting() {
        return cc_setting;
    }

    public void setCc_setting(int cc_setting) {
        this.cc_setting = cc_setting;
    }

    public int getBcc_setting() {
        return bcc_setting;
    }

    public void setBcc_setting(int bcc_setting) {
        this.bcc_setting = bcc_setting;
    }

    public int getSingle_show() {
        return single_show;
    }

    public void setSingle_show(int single_show) {
        this.single_show = single_show;
    }

    public int getIs_emergent() {
        return is_emergent;
    }

    public void setIs_emergent(int is_emergent) {
        this.is_emergent = is_emergent;
    }

    public int getIs_notification() {
        return is_notification;
    }

    public void setIs_notification(int is_notification) {
        this.is_notification = is_notification;
    }

    public int getIs_plain() {
        return is_plain;
    }

    public void setIs_plain(int is_plain) {
        this.is_plain = is_plain;
    }

    public int getIs_track() {
        return is_track;
    }

    public void setIs_track(int is_track) {
        this.is_track = is_track;
    }

    public int getIs_encryption() {
        return is_encryption;
    }

    public void setIs_encryption(int is_encryption) {
        this.is_encryption = is_encryption;
    }

    public int getIs_signature() {
        return is_signature;
    }

    public void setIs_signature(int is_signature) {
        this.is_signature = is_signature;
    }

    public int getSignature_id() {
        return signature_id;
    }

    public void setSignature_id(int signature_id) {
        this.signature_id = signature_id;
    }

    public int getMail_source() {
        return mail_source;
    }

    public void setMail_source(int mail_source) {
        this.mail_source = mail_source;
    }

    public List<ReceiverBean> getTo_recipients() {
        return to_recipients;
    }

    public void setTo_recipients(List<ReceiverBean> to_recipients) {
        this.to_recipients = to_recipients;
    }

    public List<ReceiverBean> getCc_recipients() {
        return cc_recipients;
    }

    public void setCc_recipients(List<ReceiverBean> cc_recipients) {
        this.cc_recipients = cc_recipients;
    }

    public List<ReceiverBean> getBcc_recipients() {
        return bcc_recipients;
    }

    public void setBcc_recipients(List<ReceiverBean> bcc_recipients) {
        this.bcc_recipients = bcc_recipients;
    }

    public List<AttachmentBean> getAttachments_name() {
        return attachments_name;
    }

    public void setAttachments_name(List<AttachmentBean> attachments_name) {
        this.attachments_name = attachments_name;
    }
}
