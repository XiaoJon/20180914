package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

/**
 * Created by lx on 2017/6/1.
 */

public class CreateGroupResponseBean extends BaseBean {


    /**
     * data : {"name":"我的群","desc":"我么","owner_username":"e6e152baeb25757112a41f0d312b86ac","ctime":null,"mtime":null,"gid":"23092741","appkey":null,"maxMemberCount":"500"}
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
         * name : 我的群
         * desc : 我么
         * owner_username : e6e152baeb25757112a41f0d312b86ac
         * ctime : null
         * mtime : null
         * gid : 23092741
         * appkey : null
         * maxMemberCount : 500
         */

        private String name;
        private String desc;
        private String owner_username;
        private String ctime;
        private String mtime;
        private long gid;
        private String appkey;
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

        public long getGid() {
            return gid;
        }

        public void setGid(long gid) {
            this.gid = gid;
        }

        public String getAppkey() {
            return appkey;
        }

        public void setAppkey(String appkey) {
            this.appkey = appkey;
        }

        public String getMaxMemberCount() {
            return maxMemberCount;
        }

        public void setMaxMemberCount(String maxMemberCount) {
            this.maxMemberCount = maxMemberCount;
        }

        @Override
        public String toString() {
            return "ModuleInfoBean{" +
                    "name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", owner_username='" + owner_username + '\'' +
                    ", ctime='" + ctime + '\'' +
                    ", mtime='" + mtime + '\'' +
                    ", gid='" + gid + '\'' +
                    ", appkey='" + appkey + '\'' +
                    ", maxMemberCount='" + maxMemberCount + '\'' +
                    '}';
        }
    }
}
