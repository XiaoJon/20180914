package com.hjhq.teamface.basis.constants;

/**
 * @Description:邮箱常量类
 */
public interface EmailConstant {

    //邮件详情有编辑使用网页url
    public static final String BEAN_NAME = "email";
    public static final String MAIL_BOX_SCOPE = "mail_box_scope";
//    public static final String EMAIL_DETAIL_URL = "http://192.168.1.188:8087/#/emailDetail";
//    public static final String EMAIL_EDIT_URL = "http://192.168.1.188:8087/#/emailEdit";
//    public static final String EMAIL_DETAIL_URL = "http://192.168.1.51:8088/#/emailDetail";
//    public static final String EMAIL_EDIT_URL = "http://192.168.1.51:8088/#/emailEdit";

    //邮件已读
    public static final String EMAIL_READ_TAG = "1";
    //邮件未读
    public static final String EMAIL_UNREAD_TAG = "0";


    public static final int FROM_LOCAL_FILE = 1;
    public static final int FROM_FILE_LIB = 2;
    public static final int FROM_UPLOAD = 3;

    /**
     * View标签的Key
     */
    public static final int VIEW_TAG_KEY = 3;

    /**
     * 收件箱
     */
    public static final int RECEVER_BOX = 1;
    /**
     * 未读
     */
    public static final int UNREAD_BOX = 6;
    /**
     * 发件箱
     */
    public static final int SENDED_BOX = 2;
    /**
     * 草稿箱
     */
    public static final int DRAFT_BOX = 3;
    /**
     * 已删除
     */
    public static final int DELETED_BOX = 4;
    /**
     * 垃圾箱
     */
    public static final int TRASH_BOX = 5;
    /**
     * 可审批
     */
    public static final int CAN_APPROVAL = 7;
    /**
     * 不可审批
     */
    public static final int CAN_NOT_APPROVAL = 8;
    /**
     * 无操作
     */
    public static final int NO_OPERATION = 9;
    //审批驳回或撤销
    public static final int APPROVAL_FAILED = 10;


    public static final int EDIT = 0;
    public static final int DETAIL = 1;
    //审批状态
    //审批通过
    public static final String APPROVAL_STATE_PASS = "2";
    //审批驳回
    public static final String APPROVAL_STATE_REJECTED = "3";
    //已撤销
    public static final String APPROVAL_STATE_CANCEL = "4";
    //无审批
    public static final String APPROVAL_STATE_NO = "10";
    //操作类型-新增邮件
    public static final int ADD_NEW_EMAIL = 1;
    //操作类型-回复邮件
    public static final int REPLY_EAMAIL = 2;
    //操作类型-转发邮件
    public static final int TRANSMIT_EMAIL = 3;
    //操作类型-编辑草稿
    public static final int EDIT_DRAFT = 4;
    //操作类型-再次编辑
    public static final int EDIT_AGAIN = 5;
    //信箱类型
    public static final String INBOX = "1";
    public static final String UNREAD = "6";
    public static final String SENDED = "2";
    public static final String DRAFTBOX = "3";
    public static final String DELETED = "4";
    public static final String TRASH = "5";
}

