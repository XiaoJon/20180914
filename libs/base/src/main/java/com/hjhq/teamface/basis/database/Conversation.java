package com.hjhq.teamface.basis.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 会话实体
 *
 * @author Administrator
 * @date 2017/11/23
 * Describe：会话
 */
@Entity
public class Conversation implements Serializable {
    static final long serialVersionUID = 2L;
    //会话id
    @Id
    protected long conversationId;
    protected String myId;
    //用户企信id
    String oneselfIMID;
    //公司id
    String companyId;
    protected String senderId;
    protected String receiverId;
    protected String senderName;
    protected String senderAvatarUrl;
    protected int conversationType;
    protected String targetId;
    protected String title;
    protected int unreadMsgCount;
    //最后一条消息
    protected int lastMessageType;
    protected int lastMessageState;
    protected String latestText;
    protected String latestMessage;
    protected String draft;
    //最后接收消息时间
    protected long lastMsgDate;
    //
    protected long nextShowTime;


    //会话头像
    String avatarUrl;
    //会话类型

    //隐藏或显示
    int isHide;
    //管理员id
    long principal;
    //免打扰
    int noBother;
    //置顶
    int topStatus;
    //群成员或者是否查看已读0否1是
    String peoples;
    //群类型
    int groupType = 10000;
    //是否是空会话
    boolean notEmpty;
    //当前会话总消息数,在小助手中用来标记小助手类型
    int totalMsgNum;
    //搜到的消息数;
    int resultNum;
    //
    long applicationId;
    //置顶时间
    long updateTime;
    String icon_color;
    String icon_type;
    String icon_url;


    public Conversation() {
    }




    @Keep
    public Conversation(long conversationId, String myId, String oneselfIMID,
            String companyId, String senderId, String receiverId, String senderName,
            String senderAvatarUrl, int conversationType, String targetId,
            String title, int unreadMsgCount, int lastMessageType,
            int lastMessageState, String latestText, String latestMessage,
            String draft, long lastMsgDate, long nextShowTime, String avatarUrl,
            int isHide, long principal, int noBother, int topStatus, String peoples,
            int groupType, boolean notEmpty, int totalMsgNum, int resultNum,
            long applicationId, long updateTime, String icon_color,
            String icon_type, String icon_url) {
        this.conversationId = conversationId;
        this.myId = myId;
        this.oneselfIMID = oneselfIMID;
        this.companyId = companyId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.senderAvatarUrl = senderAvatarUrl;
        this.conversationType = conversationType;
        this.targetId = targetId;
        this.title = title;
        this.unreadMsgCount = unreadMsgCount;
        this.lastMessageType = lastMessageType;
        this.lastMessageState = lastMessageState;
        this.latestText = latestText;
        this.latestMessage = latestMessage;
        this.draft = draft;
        this.lastMsgDate = lastMsgDate;
        this.nextShowTime = nextShowTime;
        this.avatarUrl = avatarUrl;
        this.isHide = isHide;
        this.principal = principal;
        this.noBother = noBother;
        this.topStatus = topStatus;
        this.peoples = peoples;
        this.groupType = groupType;
        this.notEmpty = notEmpty;
        this.totalMsgNum = totalMsgNum;
        this.resultNum = resultNum;
        this.applicationId = applicationId;
        this.updateTime = updateTime;
        this.icon_color = icon_color;
        this.icon_type = icon_type;
        this.icon_url = icon_url;
    }




    public String getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(String icon_color) {
        this.icon_color = icon_color;
    }

    public String getIcon_type() {
        return icon_type;
    }

    public void setIcon_type(String icon_type) {
        this.icon_type = icon_type;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public int getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(int lastMessageType) {
        this.lastMessageType = lastMessageType;
    }

    public int getLastMessageState() {
        return lastMessageState;
    }

    public void setLastMessageState(int lastMessageState) {
        this.lastMessageState = lastMessageState;
    }

    public String getOneselfIMID() {
        return oneselfIMID;
    }

    public void setOneselfIMID(String oneselfIMID) {
        this.oneselfIMID = oneselfIMID;
    }

    public String getLatestText() {
        return latestText;
    }

    public void setLatestText(String latestText) {
        this.latestText = latestText;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public long getLastMsgDate() {
        return lastMsgDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setLastMsgDate(long lastMsgDate) {

        this.lastMsgDate = lastMsgDate;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getIsHide() {
        return isHide;
    }

    public void setIsHide(int isHide) {
        this.isHide = isHide;
    }

    public long getPrincipal() {
        return principal;
    }

    public void setPrincipal(long principal) {
        this.principal = principal;
    }

    public int getNoBother() {
        return noBother;
    }

    public void setNoBother(int noBother) {
        this.noBother = noBother;
    }

    public int getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public long getNextShowTime() {
        return nextShowTime;
    }

    public void setNextShowTime(long nextShowTime) {
        this.nextShowTime = nextShowTime;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public boolean getNotEmpty() {
        return this.notEmpty;
    }

    public int getTotalMsgNum() {
        return totalMsgNum;
    }

    public void setTotalMsgNum(int totalMsgNum) {
        this.totalMsgNum = totalMsgNum;
    }

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
