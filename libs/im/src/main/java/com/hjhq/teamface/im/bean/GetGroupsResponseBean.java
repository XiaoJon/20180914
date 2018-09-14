package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by lx on 2017/6/3.
 */

public class GetGroupsResponseBean extends BaseBean {

    private DataBean data;


    public static class DataBean {

        private List<GroupListBean> groupList;

        public List<GroupListBean> getGroupList() {
            return groupList;
        }

        public void setGroupList(List<GroupListBean> groupList) {
            this.groupList = groupList;
        }

        public static class GroupListBean {
            /**
             * name : 我的群组
             * desc : 睡睡
             * owner_username : null
             * ctime : 2017-06-05 09:15:50
             * mtime : 2017-06-05 09:15:50
             * gid : 23093937
             * appkey : null
             * backGround : null
             * maxMemberCount : 500
             */

            private String name;
            private String desc;
            private String owner_username;
            private String ctime;
            private String mtime;
            private String gid;
            private String appkey;
            private String backGround;
            private String maxMemberCount;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getOwner_username() {
                return owner_username;
            }

            public void setOwner_username(String owner_username) {
                this.owner_username = owner_username;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public String getMtime() {
                return mtime;
            }

            public void setMtime(String mtime) {
                this.mtime = mtime;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getAppkey() {
                return appkey;
            }

            public void setAppkey(String appkey) {
                this.appkey = appkey;
            }

            public String getBackGround() {
                return backGround;
            }

            public void setBackGround(String backGround) {
                this.backGround = backGround;
            }

            public String getMaxMemberCount() {
                return maxMemberCount;
            }

            public void setMaxMemberCount(String maxMemberCount) {
                this.maxMemberCount = maxMemberCount;
            }
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
