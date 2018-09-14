package com.hjhq.teamface.basis.bean;

import com.hjhq.teamface.basis.util.file.SPHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/30.
 * Describe：
 */

public class QxMessage implements Serializable {

    /**
     * type : 1
     * fileUrl : 图片,文件,语音等url
     * fileName : 文件名字
     * fileSize : 1234324
     * fileType : avi
     * atList : [{"id":65465972946524654,"name":"被@人的名字"}]
     * msg : 这是文本消息体
     */

    //消息id
    private String msgId;
    //消息类型
    private int type;
    private long chatId;
    private String fileUrl;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String msg;
    private ArrayList<AtListBean> atList;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    //时长
    int duration;
    //地址
    String location;
    //文件本地路径
    String filePath;
    //发送人的名字
    String senderName;
    //发送人的头像
    String senderAvatar;
    String companyId;
    //文件库文件id
    String fileId;
    //群消息发送时当前群成员id
    String allPeoples;


    public QxMessage() {

        this.senderName = getSenderName();
        this.senderAvatar = getSenderAvatar();
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setAllPeoples(String allPeoples) {

        this.allPeoples = allPeoples;
    }

    public String getAllPeoples() {
        return allPeoples;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<AtListBean> getAtList() {
        return atList;
    }

    public void setAtList(ArrayList<AtListBean> atList) {
        this.atList = atList;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getCompanyId() {
        return SPHelper.getCompanyId();
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public static class AtListBean implements Serializable {
        /**
         * id : 65465972946524654
         * name : 被@人的名字
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
