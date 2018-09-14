package com.hjhq.teamface.memo.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.ModuleItemBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/8.
 * Describe：备忘录详情
 */

public class MemoDetailBean extends BaseBean implements Serializable {

    /**
     * data : {"share_ids":"1,2","create_time":1523150516115,"remind_status":"1","modify_time":"","del_status":"0","modify_by":"","title":"那你呢黄家驹宝贝宝贝","content":[{"num":4,"check":1,"type":1,"content":"宝贝宝贝"}],"create_by":5,"createObj":null,"items_arr":"","shareObj":[{"leader":"0","mood":"","is_enable":"1","personnel_create_by":"","sex":"","sign":"","birth":"","employee_name":"YLG","del_status":"0","picture":"","microblog_background":"","datetime_create_time":"","post_id":1,"phone":"13538283426","role_id":3,"post_name":"工","mobile_phone":"","name":"YLG","id":2,"region":"","email":"","account":"13538283426","status":"0"}],"remind_time":1523496023680,"location":[{"address":"广东省深圳市南山区科技园高新南一道21-2号","lng":113.946318,"name":"思创科技大厦","lat":22.538232}],"id":54,"pic_url":""}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * share_ids : 1,2
         * create_time : 1523150516115
         * remind_status : 1
         * modify_time :
         * del_status : 0
         * modify_by :
         * title : 那你呢黄家驹宝贝宝贝
         * content : [{"num":4,"check":1,"type":1,"content":"宝贝宝贝"}]
         * create_by : 5
         * createObj : null
         * items_arr :
         * shareObj : [{"leader":"0","mood":"","is_enable":"1","personnel_create_by":"","sex":"","sign":"","birth":"","employee_name":"YLG","del_status":"0","picture":"","microblog_background":"","datetime_create_time":"","post_id":1,"phone":"13538283426","role_id":3,"post_name":"工","mobile_phone":"","name":"YLG","id":2,"region":"","email":"","account":"13538283426","status":"0"}]
         * remind_time : 1523496023680
         * location : [{"address":"广东省深圳市南山区科技园高新南一道21-2号","lng":113.946318,"name":"思创科技大厦","lat":22.538232}]
         * id : 54
         * pic_url :
         */

        private String share_ids;
        private String create_time;
        private String remind_status;
        private String modify_time;
        private String del_status;
        private String modify_by;
        private String title;
        private String create_by;
        private MemoCreatorBean createObj;
        private ArrayList<ModuleItemBean> items_arr;
        private String remind_time;
        private String id;
        private String pic_url;
        private String commentsCount;
        private ArrayList<MemoContentBean> content;
        private ArrayList<MemoCreatorBean> shareObj;
        private ArrayList<MemoLocationBean> location;
        private ArrayList<ModuleItemBean> project;



        public String getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(String commentsCount) {
            this.commentsCount = commentsCount;
        }

        public String getShare_ids() {
            return share_ids;
        }

        public void setShare_ids(String share_ids) {
            this.share_ids = share_ids;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getRemind_status() {
            return remind_status;
        }

        public void setRemind_status(String remind_status) {
            this.remind_status = remind_status;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getModify_by() {
            return modify_by;
        }

        public void setModify_by(String modify_by) {
            this.modify_by = modify_by;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreate_by() {
            return create_by;
        }

        public void setCreate_by(String create_by) {
            this.create_by = create_by;
        }

        public MemoCreatorBean getCreateObj() {
            return createObj;
        }

        public void setCreateObj(MemoCreatorBean createObj) {
            this.createObj = createObj;
        }

        public ArrayList<ModuleItemBean> getItemsArr() {
            return items_arr;
        }

        public void setItemsArr(ArrayList<ModuleItemBean> itemsArr) {
            this.items_arr = itemsArr;
        }

        public String getRemind_time() {
            return remind_time;
        }

        public void setRemind_time(String remind_time) {
            this.remind_time = remind_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public ArrayList<MemoContentBean> getContent() {
            return content;
        }

        public void setContent(ArrayList<MemoContentBean> content) {
            this.content = content;
        }

        public ArrayList<MemoCreatorBean> getShareObj() {
            return shareObj;
        }

        public void setShareObj(ArrayList<MemoCreatorBean> shareObj) {
            this.shareObj = shareObj;
        }

        public ArrayList<MemoLocationBean> getLocation() {
            return location;
        }

        public void setLocation(ArrayList<MemoLocationBean> location) {
            this.location = location;
        }

        public ArrayList<ModuleItemBean> getProject() {
            return project;
        }

        public void setProject(ArrayList<ModuleItemBean> project) {
            this.project = project;
        }
    }
}
