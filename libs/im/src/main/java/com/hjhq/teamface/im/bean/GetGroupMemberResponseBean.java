package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 群组成员列表
 * Created by lx on 2017/6/5.
 */

public class GetGroupMemberResponseBean extends BaseBean {

    /**
     * data : {"groupInfo":{"name":"新群","desc":"吹水","owner_username":null,"ctime":"2017-06-05 10:54:20","mtime":"2017-06-05 10:54:20","gid":"23093999","appkey":"16d95c5556bcde11277431dc","backGround":null,"maxMemberCount":"500"},"list":[{"id":null,"createDate":null,"disabled":null,"employeeId":3351105254866944,"employeeName":"邹毅","telephone":"13617312230","email":"","photograph":"0","position":"web","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3350964526907392,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"a594be0b1b04ee3726d7c6d38b06e2bc","imPassWord":"3f70c52f375fba4197361b512c6ab4ad","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3351172321017856,"employeeName":"程林根","telephone":"13751041565","email":"","photograph":"0","position":null,"departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3346842603487232,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"5a431ffb2d695b442b372a4f92b29a36","imPassWord":"baaf1ffee699e78fc4ecf9b71ae369cc","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3365301812854784,"employeeName":"陈宇亮","telephone":"15974267842","email":"","photograph":"0","position":"iOS","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3358378015752192,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"9e9615e50d7c545957de2c381234922a","imPassWord":"fb1842a4574b7f49c1a6bc4a200ca288","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3366624183861248,"employeeName":"","telephone":"","email":"","photograph":"0","position":"安卓","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3356812542001152,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"82ca479774574cc7927714b685aee11b","imPassWord":"34d82c89388f5f94d190ec6c9367faa3","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3350770213208064,"employeeName":"尹明亮","telephone":"15818548636","email":"","photograph":"0","position":"iOS","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3350769185210368,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"e6e152baeb25757112a41f0d312b86ac","imPassWord":"7a33e98edeacb7955684bea1ff5f96a7","flag":"1"}]}
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
         * groupInfo : {"name":"新群","desc":"吹水","owner_username":null,"ctime":"2017-06-05 10:54:20","mtime":"2017-06-05 10:54:20","gid":"23093999","appkey":"16d95c5556bcde11277431dc","backGround":null,"maxMemberCount":"500"}
         * list : [{"id":null,"createDate":null,"disabled":null,"employeeId":3351105254866944,"employeeName":"邹毅","telephone":"13617312230","email":"","photograph":"0","position":"web","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3350964526907392,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"a594be0b1b04ee3726d7c6d38b06e2bc","imPassWord":"3f70c52f375fba4197361b512c6ab4ad","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3351172321017856,"employeeName":"程林根","telephone":"13751041565","email":"","photograph":"0","position":null,"departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3346842603487232,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"5a431ffb2d695b442b372a4f92b29a36","imPassWord":"baaf1ffee699e78fc4ecf9b71ae369cc","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3365301812854784,"employeeName":"陈宇亮","telephone":"15974267842","email":"","photograph":"0","position":"iOS","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3358378015752192,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"9e9615e50d7c545957de2c381234922a","imPassWord":"fb1842a4574b7f49c1a6bc4a200ca288","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3366624183861248,"employeeName":"","telephone":"","email":"","photograph":"0","position":"安卓","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3356812542001152,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"82ca479774574cc7927714b685aee11b","imPassWord":"34d82c89388f5f94d190ec6c9367faa3","flag":"0"},{"id":null,"createDate":null,"disabled":null,"employeeId":3350770213208064,"employeeName":"尹明亮","telephone":"15818548636","email":"","photograph":"0","position":"iOS","departmentName":"明亮公司","companyName":"明亮公司","isActive":0,"userId":3350769185210368,"departmentLeaderName":null,"departmentLeaderPhotoUrl":null,"imUserName":"e6e152baeb25757112a41f0d312b86ac","imPassWord":"7a33e98edeacb7955684bea1ff5f96a7","flag":"1"}]
         */

        private GroupInfoBean groupInfo;
        private List<ListBean> list;

        public GroupInfoBean getGroupInfo() {
            return groupInfo;
        }

        public void setGroupInfo(GroupInfoBean groupInfo) {
            this.groupInfo = groupInfo;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class GroupInfoBean {
            /**
             * name : 新群
             * desc : 吹水
             * owner_username : null
             * ctime : 2017-06-05 10:54:20
             * mtime : 2017-06-05 10:54:20
             * gid : 23093999
             * appkey : 16d95c5556bcde11277431dc
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

        public static class ListBean {
            /**
             * id : null
             * createDate : null
             * disabled : null
             * employeeId : 3351105254866944
             * employeeName : 邹毅
             * telephone : 13617312230
             * email :
             * photograph : 0
             * position : web
             * departmentName : 明亮公司
             * companyName : 明亮公司
             * isActive : 0
             * userId : 3350964526907392
             * departmentLeaderName : null
             * departmentLeaderPhotoUrl : null
             * imUserName : a594be0b1b04ee3726d7c6d38b06e2bc
             * imPassWord : 3f70c52f375fba4197361b512c6ab4ad
             * flag : 0
             */

            private long id;
            private String createDate;
            private String disabled;
            private long employeeId;
            private String employeeName;
            private String telephone;
            private String email;
            private String photograph;
            private String position;
            private String departmentName;
            private String companyName;
            private int isActive;
            private long userId;
            private String departmentLeaderName;
            private String departmentLeaderPhotoUrl;
            private String imUserName;
            private String imPassWord;
            private String flag;

            private boolean isSelect;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDisabled() {
                return disabled;
            }

            public void setDisabled(String disabled) {
                this.disabled = disabled;
            }

            public long getEmployeeId() {
                return employeeId;
            }

            public void setEmployeeId(long employeeId) {
                this.employeeId = employeeId;
            }

            public String getEmployeeName() {
                return employeeName;
            }

            public void setEmployeeName(String employeeName) {
                this.employeeName = employeeName;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhotograph() {
                return photograph;
            }

            public void setPhotograph(String photograph) {
                this.photograph = photograph;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public int getIsActive() {
                return isActive;
            }

            public void setIsActive(int isActive) {
                this.isActive = isActive;
            }

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getDepartmentLeaderName() {
                return departmentLeaderName;
            }

            public void setDepartmentLeaderName(String departmentLeaderName) {
                this.departmentLeaderName = departmentLeaderName;
            }

            public String getDepartmentLeaderPhotoUrl() {
                return departmentLeaderPhotoUrl;
            }

            public void setDepartmentLeaderPhotoUrl(String departmentLeaderPhotoUrl) {
                this.departmentLeaderPhotoUrl = departmentLeaderPhotoUrl;
            }

            public String getImUserName() {
                return imUserName;
            }

            public void setImUserName(String imUserName) {
                this.imUserName = imUserName;
            }

            public String getImPassWord() {
                return imPassWord;
            }

            public void setImPassWord(String imPassWord) {
                this.imPassWord = imPassWord;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }
}
