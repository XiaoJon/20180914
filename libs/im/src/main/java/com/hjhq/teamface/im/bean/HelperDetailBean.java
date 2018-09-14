package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by lx on 2017/6/28.
 */

public class HelperDetailBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * itemType : 2
         * msgDesc : 抄送到我
         * receiverName : babab
         * isRead : 0
         * associateModuleId : 3427575543611392
         * msgBody : 抄送到我
         * createrName : 程林根
         * itemId : 114
         * senderId : 3426016997687296
         * senderName : 程林根
         * receiverId : 3426048086343680
         * sendContent : 程林根抄送到我
         * startTime : 1498037583702
         * id : 1640
         * createrId : 3426016997687296
         * "createDate": 1497488453988
         */

        private int totalRows;
        private int totalPages;
        private int pageNum;
        private int pageSize;
        private int curPageSize;
        private List<ListBean> list;

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurPageSize() {
            return curPageSize;
        }

        public void setCurPageSize(int curPageSize) {
            this.curPageSize = curPageSize;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean{

            private int itemType;
            private String msgDesc;
            private String receiverName;
            private int isRead;
            private int isHandle;
            private int type;
            private long associateModuleId;
            private long fileSrc;
            private String msgBody;
            private String createrName;
            private int itemId;
            private long senderId;
            private String senderName;
            private long receiverId;
            private String sendContent;
            private long startTime;
            private long endTime;
            private long id;
            private long createrId;
            private long createDate;

            public void setId(long id) {
                this.id = id;
            }

            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }

            public String getMsgDesc() {
                return msgDesc;
            }

            public void setMsgDesc(String msgDesc) {
                this.msgDesc = msgDesc;
            }

            public String getReceiverName() {
                return receiverName;
            }

            public void setReceiverName(String receiverName) {
                this.receiverName = receiverName;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }

            public int getIsHandle() {
                return isHandle;
            }

            public void setIsHandle(int isHandle) {
                this.isHandle = isHandle;
            }

            public long getAssociateModuleId() {
                return associateModuleId;
            }

            public void setAssociateModuleId(long associateModuleId) {
                this.associateModuleId = associateModuleId;
            }

            public long getFileSrc() {
                return fileSrc;
            }

            public void setFileSrc(long fileSrc) {
                this.fileSrc = fileSrc;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getMsgBody() {
                return msgBody;
            }

            public void setMsgBody(String msgBody) {
                this.msgBody = msgBody;
            }

            public String getCreaterName() {
                return createrName;
            }

            public void setCreaterName(String createrName) {
                this.createrName = createrName;
            }

            public int getItemId() {
                return itemId;
            }

            public void setItemId(int itemId) {
                this.itemId = itemId;
            }

            public long getSenderId() {
                return senderId;
            }

            public void setSenderId(long senderId) {
                this.senderId = senderId;
            }

            public String getSenderName() {
                return senderName;
            }

            public void setSenderName(String senderName) {
                this.senderName = senderName;
            }

            public long getReceiverId() {
                return receiverId;
            }

            public void setReceiverId(long receiverId) {
                this.receiverId = receiverId;
            }

            public String getSendContent() {
                return sendContent;
            }

            public void setSendContent(String sendContent) {
                this.sendContent = sendContent;
            }

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public long getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getCreaterId() {
                return createrId;
            }

            public void setCreaterId(long createrId) {
                this.createrId = createrId;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }
        }

    }
}
