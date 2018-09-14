package com.hjhq.teamface.basis.bean;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/8.
 * Describe：
 */

public class EmailBean implements Serializable {
    //账号ID
    private String account_id;
    private String mail_content;
    private String from_recipient;
    private String subject;
    //2 审批通过 3 审批驳回 4 已撤销 10 没有审批
    private String approval_status;
    private ArrayList<ReceiverBean> bcc_recipients;
    private String mail_source;
    private String embedded_images;
    private String read_status;
    private String is_track = "0";
    private String bcc_setting;
    private ArrayList<AttachmentBean> attachments_name;
    private ArrayList<ReceiverBean> cc_recipients;
    private ArrayList<ReceiverBean> to_recipients;
    private String is_emergent;
    private String is_plain;
    //0 未发送  1 已发送 2 部分发送
    private String send_status;
    private String is_notification;
    private String id;
    private String create_time;
    private String draft_status;
    private String timer_status;
    private String mail_tags;
    //发件人姓名
    private String from_recipient_name = "";
    private String ip_address;
    private String timer_task_time;
    //审批人
    private String personnel_approverBy;
    //审批抄送人
    private String personnel_ccTo;

    private String process_instance_id;

    public String getProcess_instance_id() {
        return process_instance_id;
    }

    public void setProcess_instance_id(String process_instance_id) {
        this.process_instance_id = process_instance_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
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

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getMail_tags() {
        return mail_tags;
    }

    public void setMail_tags(String mail_tags) {
        this.mail_tags = mail_tags;
    }

    public String getFrom_recipient_name() {

        return TextUtils.isEmpty(from_recipient_name) ? "" : from_recipient_name;
    }

    public void setFrom_recipient_name(String from_recipient_name) {
        this.from_recipient_name = from_recipient_name;
    }
    // private List<ReceiverBean> to_recipients;

    public String getTimer_status() {
        return timer_status;
    }

    public void setTimer_status(String timer_status) {
        this.timer_status = timer_status;
    }

    public String getDraft_status() {
        return draft_status;
    }

    public void setDraft_status(String draft_status) {
        this.draft_status = draft_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMail_content() {
        return mail_content;
    }

    public void setMail_content(String mail_content) {
        this.mail_content = mail_content;
    }

    public String getFrom_recipient() {
        return from_recipient;
    }

    public void setFrom_recipient(String from_recipient) {
        this.from_recipient = from_recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public ArrayList<ReceiverBean> getBcc_recipients() {
        return bcc_recipients;
    }

    public void setBcc_recipients(ArrayList<ReceiverBean> bcc_recipients) {
        this.bcc_recipients = bcc_recipients;
    }

    public ArrayList<ReceiverBean> getTo_recipients() {
        return to_recipients;
    }

    public void setTo_recipients(ArrayList<ReceiverBean> to_recipients) {
        this.to_recipients = to_recipients;
    }

    public String getMail_source() {
        return mail_source;
    }

    public void setMail_source(String mail_source) {
        this.mail_source = mail_source;
    }

    public String getEmbedded_images() {
        return embedded_images;
    }

    public void setEmbedded_images(String embedded_images) {
        this.embedded_images = embedded_images;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getIs_track() {
        return is_track;
    }

    public void setIs_track(String is_track) {
        this.is_track = is_track;
    }

    public String getBcc_setting() {
        return bcc_setting;
    }

    public void setBcc_setting(String bcc_setting) {
        this.bcc_setting = bcc_setting;
    }

    public ArrayList<AttachmentBean> getAttachments_name() {
        return attachments_name;
    }

    public void setAttachments_name(ArrayList<AttachmentBean> attachments_name) {
        this.attachments_name = attachments_name;
    }

    public ArrayList<ReceiverBean> getCc_recipients() {
        return cc_recipients;
    }

    public void setCc_recipients(ArrayList<ReceiverBean> cc_recipients) {
        this.cc_recipients = cc_recipients;
    }

    public String getIs_emergent() {
        return is_emergent;
    }

    public void setIs_emergent(String is_emergent) {
        this.is_emergent = is_emergent;
    }

    public String getIs_plain() {
        return is_plain;
    }

    public void setIs_plain(String is_plain) {
        this.is_plain = is_plain;
    }

    public String getSend_status() {
        return send_status;
    }

    public void setSend_status(String send_status) {
        this.send_status = send_status;
    }

    public String getIs_notification() {
        return is_notification;
    }

    public void setIs_notification(String is_notification) {
        this.is_notification = is_notification;
    }

    public String getTimer_task_time() {
        return timer_task_time;
    }

    public void setTimer_task_time(String timer_task_time) {
        this.timer_task_time = timer_task_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

           /* public List<ReceiverBean> getTo_recipients() {
                return to_recipients;
            }

            public void setTo_recipients(List<ReceiverBean> to_recipients) {
                this.to_recipients = to_recipients;
            }*/


}
