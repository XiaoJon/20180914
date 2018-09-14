package com.hjhq.teamface.common.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.UploadFileBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/7/3.
 */

public class FriendsListBean extends BaseBean {


    /**
     * data : {"totalRows":1,"totalPages":1,"pageNum":1,"pageSize":20,"curPageSize":1,"list":[{"commentList":[],"employeeName":null,"praiseList":[],"photograph":null,"images":[{"id":3444389212241920,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/3/03ac750fd1d586","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-32-10.jpg","fileSize":374735,"fileType":"jpg"},{"id":3444389212241921,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/6/03ac763064c3c6","uploadTime":1499063808412,"fileName":"2017_0628_105519.png","fileSize":245896,"fileType":"png"},{"id":3444389212241922,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac77c47f41e6","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-16-17.jpg","fileSize":433406,"fileType":"jpg"},{"id":3444389212241923,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac78baa230b1","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-31-37.jpg","fileSize":344300,"fileType":"jpg"},{"id":3444389212241924,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac798db86ec6","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-32-50.jpg","fileSize":246711,"fileType":"jpg"},{"id":3444389212241925,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac7aa4a45201","uploadTime":1499063808412,"fileName":"2017_0628_105519.png","fileSize":245896,"fileType":"png"}],"isDelete":0,"isPraise":0,"employeeId":3437616166109184,"id":3444389211258880,"info":"公里","createDate":1499063808352}]}
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
         * totalRows : 1
         * totalPages : 1
         * pageNum : 1
         * pageSize : 20
         * curPageSize : 1
         * list : [{"commentList":[],"employeeName":null,"praiseList":[],"photograph":null,"images":[{"id":3444389212241920,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/3/03ac750fd1d586","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-32-10.jpg","fileSize":374735,"fileType":"jpg"},{"id":3444389212241921,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/6/03ac763064c3c6","uploadTime":1499063808412,"fileName":"2017_0628_105519.png","fileSize":245896,"fileType":"png"},{"id":3444389212241922,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac77c47f41e6","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-16-17.jpg","fileSize":433406,"fileType":"jpg"},{"id":3444389212241923,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac78baa230b1","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-31-37.jpg","fileSize":344300,"fileType":"jpg"},{"id":3444389212241924,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac798db86ec6","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-32-50.jpg","fileSize":246711,"fileType":"jpg"},{"id":3444389212241925,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac7aa4a45201","uploadTime":1499063808412,"fileName":"2017_0628_105519.png","fileSize":245896,"fileType":"png"}],"isDelete":0,"isPraise":0,"employeeId":3437616166109184,"id":3444389211258880,"info":"公里","createDate":1499063808352}]
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

        public static class ListBean implements Serializable{
            /**
             * commentList : []
             * employeeName : null
             * praiseList : []
             * photograph : null
             * images : [{"id":3444389212241920,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/3/03ac750fd1d586","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-32-10.jpg","fileSize":374735,"fileType":"jpg"},{"id":3444389212241921,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/6/03ac763064c3c6","uploadTime":1499063808412,"fileName":"2017_0628_105519.png","fileSize":245896,"fileType":"png"},{"id":3444389212241922,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac77c47f41e6","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-16-17.jpg","fileSize":433406,"fileType":"jpg"},{"id":3444389212241923,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac78baa230b1","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-31-37.jpg","fileSize":344300,"fileType":"jpg"},{"id":3444389212241924,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac798db86ec6","uploadTime":1499063808412,"fileName":"Screenshot_2017-06-24-19-32-50.jpg","fileSize":246711,"fileType":"jpg"},{"id":3444389212241925,"createDate":null,"disabled":null,"circleMainId":3444389211258880,"fileUrl":"http://192.168.1.172:9400/1/03ac7aa4a45201","uploadTime":1499063808412,"fileName":"2017_0628_105519.png","fileSize":245896,"fileType":"png"}]
             * isDelete : 0
             * isPraise : 0
             * employeeId : 3437616166109184
             * id : 3444389211258880
             * info : 公里
             * createDate : 1499063808352
             */

            private String employeeName;
            private String photograph;
            private String isDelete;
            private String isPraise;
            private String employeeId;
            private String id;
            private String info;
            private String address;
            private String latitude;
            private String longitude;
            private String datetimeCreateDate;
            private List<CommentBean> commentList;
            private List<PraiseBean> praiseList;
            private List<UploadFileBean> images;


            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getEmployeeName() {
                return employeeName;
            }

            public void setEmployeeName(String employeeName) {
                this.employeeName = employeeName;
            }

            public String getPhotograph() {
                return photograph;
            }

            public void setPhotograph(String photograph) {
                this.photograph = photograph;
            }

            public String getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(String isDelete) {
                this.isDelete = isDelete;
            }

            public String getIsPraise() {
                return isPraise;
            }

            public void setIsPraise(String isPraise) {
                this.isPraise = isPraise;
            }

            public String getEmployeeId() {
                return employeeId;
            }

            public void setEmployeeId(String employeeId) {
                this.employeeId = employeeId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getDatetimeCreateDate() {
                return datetimeCreateDate;
            }

            public void setDatetimeCreateDate(String datetimeCreateDate) {
                this.datetimeCreateDate = datetimeCreateDate;
            }

            public List<CommentBean> getCommentList() {
                return commentList;
            }

            public void setCommentList(List<CommentBean> commentList) {
                this.commentList = commentList;
            }

            public List<PraiseBean> getPraiseList() {
                return praiseList;
            }

            public void setPraiseList(List<PraiseBean> praiseList) {
                this.praiseList = praiseList;
            }

            public List<UploadFileBean> getImages() {
                return images;
            }

            public void setImages(List<UploadFileBean> images) {
                this.images = images;
            }

            public static class PraiseBean implements Serializable{

                /**
                 * employeeName : null
                 * photograph : null
                 * employeeId : 3437616166109184
                 */

                private String employeeName;
                private String photograph;
                private String employeeId;

                public String getEmployeeName() {
                    return employeeName;
                }

                public void setEmployeeName(String employeeName) {
                    this.employeeName = employeeName;
                }

                public String getPhotograph() {
                    return photograph;
                }

                public void setPhotograph(String photograph) {
                    this.photograph = photograph;
                }

                public String getEmployeeId() {
                    return employeeId;
                }

                public void setEmployeeId(String employeeId) {
                    this.employeeId = employeeId;
                }
            }

            public static class CommentBean  implements Serializable{

                /**
                 * contentinfo : "asdfasdfsdfa"
                 * senderPhotograph : null
                 * senderName : null
                 * senderId : 3437616166109184
                 * receiverId : null
                 * receiverPhotograph : null
                 * receiverName : null
                 * commentId : 3444501408366592
                 * createDate : 1499070656320
                 */

                private String contentinfo;
                private String senderPhotograph;
                private String senderName;
                private String senderId;
                private String receiverId;
                private String receiverPhotograph;
                private String receiverName;
                private String commentId;
                private String createDate;

                public String getContentinfo() {
                    return contentinfo;
                }

                public void setContentinfo(String contentinfo) {
                    this.contentinfo = contentinfo;
                }

                public String getSenderPhotograph() {
                    return senderPhotograph;
                }

                public void setSenderPhotograph(String senderPhotograph) {
                    this.senderPhotograph = senderPhotograph;
                }

                public String getSenderName() {
                    return senderName;
                }

                public void setSenderName(String senderName) {
                    this.senderName = senderName;
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

                public String getReceiverPhotograph() {
                    return receiverPhotograph;
                }

                public void setReceiverPhotograph(String receiverPhotograph) {
                    this.receiverPhotograph = receiverPhotograph;
                }

                public String getReceiverName() {
                    return receiverName;
                }

                public void setReceiverName(String receiverName) {
                    this.receiverName = receiverName;
                }

                public String getCommentId() {
                    return commentId;
                }

                public void setCommentId(String commentId) {
                    this.commentId = commentId;
                }

                public String getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(String createDate) {
                    this.createDate = createDate;
                }
            }

        }
    }
}
