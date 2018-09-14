package com.hjhq.teamface.basis.database;


import com.hjhq.teamface.basis.constants.MsgConstant;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/20.
 * Describe：
 */
@Entity
public class SocketMessage implements com.chad.library.adapter.base.entity.MultiItemEntity, Serializable {
    @Transient
    static final long serialVersionUID = 1L;
    private long conversationId;
    private String companyId;
    private String myId;
    //个人唯一码,4个字节
    private String oneselfIMID;
    //命令类型 2个字节
    private int usCmdID;
    //版本
    private int ucVer;
    //设备类型
    private int ucDeviceType;
    //未使用
    private int ucFlag;
    //服务器时间
    private long serverTimes;
    //消息id
    @Id
    private long msgID;
    //发送者id
    private String senderID;
    //接收者id
    private String receiverID;
    //客户端时间
    private long clientTimes;
    //随机数,用于区分消息id
    private int rand;
    //完整消息体内容
    private String msgContent;
    //消息类型1文本,2图片3语音4文件,5小视频6位置7提醒
    private int msgType;
    //文件名字
    private String fileName;
    //文件大小
    private long fileSize;
    //文件类型
    private String fileType;
    //文件url
    private String fileUrl;
    //视频文件路径
    private String videoUrl;
    //文件库文件id
    private String fileId;
    //文件本地路径
    private String fileLocalPath;
    //经度
    private String longitude;
    //维度
    private String latitude;
    //时长
    private int duration;
    //是否显示时间
    private boolean showTime;
    //错误码
    private int resultCode;
    //消息内容
    private String content;
    //是否@了我
    private boolean isAtMe;
    //是否@所有人
    private boolean isAtAll;
    //已读未读
    private boolean isRead;
    //发送中,已发送,已读(人数)
    private int sendState;
    private long createTime;
    //发送人名字
    private String fromName;
    //接收人名字
    private String targetName;
    //地址
    private String location;
    //发送人头像
    private String senderAvatar;
    //发送人名字
    private String senderName;
    //聊天类型
    private int chatType;
    //已读人数
    private int readnum;
    //当前群人数
    private int groupMemeberNum;
    //当前消息群成员id
    private String allPeoples;
    //已读成员id
    private String readPeoples;


    //4个字节,用于判断登录时的返回码
    int loginResponseCode;


    public SocketMessage() {
    }









    @Generated(hash = 1329435155)
    public SocketMessage(long conversationId, String companyId, String myId, String oneselfIMID, int usCmdID,
            int ucVer, int ucDeviceType, int ucFlag, long serverTimes, long msgID, String senderID,
            String receiverID, long clientTimes, int rand, String msgContent, int msgType, String fileName,
            long fileSize, String fileType, String fileUrl, String videoUrl, String fileId, String fileLocalPath,
            String longitude, String latitude, int duration, boolean showTime, int resultCode, String content,
            boolean isAtMe, boolean isAtAll, boolean isRead, int sendState, long createTime, String fromName,
            String targetName, String location, String senderAvatar, String senderName, int chatType, int readnum,
            int groupMemeberNum, String allPeoples, String readPeoples, int loginResponseCode) {
        this.conversationId = conversationId;
        this.companyId = companyId;
        this.myId = myId;
        this.oneselfIMID = oneselfIMID;
        this.usCmdID = usCmdID;
        this.ucVer = ucVer;
        this.ucDeviceType = ucDeviceType;
        this.ucFlag = ucFlag;
        this.serverTimes = serverTimes;
        this.msgID = msgID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.clientTimes = clientTimes;
        this.rand = rand;
        this.msgContent = msgContent;
        this.msgType = msgType;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.videoUrl = videoUrl;
        this.fileId = fileId;
        this.fileLocalPath = fileLocalPath;
        this.longitude = longitude;
        this.latitude = latitude;
        this.duration = duration;
        this.showTime = showTime;
        this.resultCode = resultCode;
        this.content = content;
        this.isAtMe = isAtMe;
        this.isAtAll = isAtAll;
        this.isRead = isRead;
        this.sendState = sendState;
        this.createTime = createTime;
        this.fromName = fromName;
        this.targetName = targetName;
        this.location = location;
        this.senderAvatar = senderAvatar;
        this.senderName = senderName;
        this.chatType = chatType;
        this.readnum = readnum;
        this.groupMemeberNum = groupMemeberNum;
        this.allPeoples = allPeoples;
        this.readPeoples = readPeoples;
        this.loginResponseCode = loginResponseCode;
    }









    public String getAllPeoples() {
        return allPeoples;
    }

    public void setAllPeoples(String allPeoples) {
        this.allPeoples = allPeoples;
    }

    public String getReadPeoples() {
        return readPeoples;
    }

    public void setReadPeoples(String readPeoples) {
        this.readPeoples = readPeoples;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getOneselfIMID() {
        return oneselfIMID;
    }

    public void setOneselfIMID(String oneselfIMID) {
        this.oneselfIMID = oneselfIMID;
    }

    public int getUsCmdID() {
        return usCmdID;
    }

    public void setUsCmdID(int usCmdID) {
        this.usCmdID = usCmdID;
    }

    public int getUcVer() {
        return ucVer;
    }

    public void setUcVer(int ucVer) {
        this.ucVer = ucVer;
    }

    public int getUcDeviceType() {
        return ucDeviceType;
    }

    public void setUcDeviceType(int ucDeviceType) {
        this.ucDeviceType = ucDeviceType;
    }

    public int getUcFlag() {
        return ucFlag;
    }

    public void setUcFlag(int ucFlag) {
        this.ucFlag = ucFlag;
    }

    public long getServerTimes() {
        return serverTimes;
    }

    public void setServerTimes(long serverTimes) {
        this.serverTimes = serverTimes;
    }

    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public long getClientTimes() {
        return clientTimes;
    }

    public void setClientTimes(long clientTimes) {
        this.clientTimes = clientTimes;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public int getLoginResponseCode() {
        return loginResponseCode;
    }

    public void setLoginResponseCode(int loginResponseCode) {
        this.loginResponseCode = loginResponseCode;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAtMe() {
        return isAtMe;
    }

    public void setAtMe(boolean atMe) {
        isAtMe = atMe;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }


    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public int getItemType() {
        if (getUcFlag() == 0) {
            //1文本,2图片3语音4文件,5小视频6位置7提醒

            switch (getMsgType()) {
                //文本
                case MsgConstant.TEXT:
                    return MsgConstant.TYPE_TEXT_SEND;

                //图片
                case MsgConstant.IMAGE:

                    return MsgConstant.TYPE_IMAGE_SEND;
                //语音
                case MsgConstant.VOICE:

                    return MsgConstant.TYPE_VOICE_SEND;
                //文件
                case MsgConstant.FILE:

                    return MsgConstant.TYPE_FILE_SEND;
                //小视频
                case MsgConstant.VIDEO:

                    return MsgConstant.TYPE_VIDEO_SEND;
                //位置
                case MsgConstant.LOCATION:

                    return MsgConstant.TYPE_LOCATION_SEND;
                //提醒
                case MsgConstant.NOTIFICATION:
                    return MsgConstant.TYPE_NOTIFICATION_SEND;
                default:
                    return MsgConstant.TYPE_UNKNOWN;


            }
        } else if (getUcFlag() == 1) {
            switch (getMsgType()) {
                //文本
                case MsgConstant.TEXT:
                    return MsgConstant.TYPE_TEXT_RECEIVE;

                //图片
                case MsgConstant.IMAGE:

                    return MsgConstant.TYPE_IMAGE_RECEIVE;
                //语音
                case MsgConstant.VOICE:

                    return MsgConstant.TYPE_VOICE_RECEIVE;
                //文件
                case MsgConstant.FILE:

                    return MsgConstant.TYPE_FILE_RECEIVE;
                //小视频
                case MsgConstant.VIDEO:

                    return MsgConstant.TYPE_VIDEO_RECEIVE;
                //位置
                case MsgConstant.LOCATION:

                    return MsgConstant.TYPE_LOCATION_RECEIVE;
                //提醒
                case MsgConstant.NOTIFICATION:
                    return MsgConstant.TYPE_NOTIFICATION_SEND;
                default:
                    return MsgConstant.TYPE_UNKNOWN;


            }
        }
        return MsgConstant.TYPE_UNKNOWN;


    }

    public boolean getIsAtMe() {
        return this.isAtMe;
    }

    public void setIsAtMe(boolean isAtMe) {
        this.isAtMe = isAtMe;
    }

    public boolean getIsAtAll() {
        return this.isAtAll;
    }

    public void setIsAtAll(boolean isAtAll) {
        this.isAtAll = isAtAll;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public boolean getShowTime() {
        return this.showTime;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getGroupMemeberNum() {
        return groupMemeberNum;
    }

    public void setGroupMemeberNum(int groupMemeberNum) {
        this.groupMemeberNum = groupMemeberNum;
    }
}
