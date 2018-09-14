package com.hjhq.teamface.memo.bean;

import com.hjhq.teamface.basis.bean.ModuleItemBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 * Describe：
 */

public class NewMemoBean {

    /**
     * content : [{"type":1},{"text":{"check":0,"content":"没毛病","num":3},"type":1}]
     * itemsArr :
     * location : [{"lat":22.538232,"lng":113.946318,"address":"深圳市南山区高新南一道"}]
     * picUrl : http://img1.imgtn.bdimg.com/it/u=2915973107,3750612984&fm=27&gp=0.jpg
     * remindStatus : 0
     * remindTime : 0
     * shareIds :
     * title : 我鼓风机房刷卡缴费开发的管理地方
     */
    private String id;
    private String picUrl;
    private int remindStatus;
    private long remindTime;
    private String shareIds;
    private String title;
    private List<MemoContentBean> content;
    private List<MemoLocationBean> location;
    private List<ModuleItemBean> itemsArr;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(int remindStatus) {
        this.remindStatus = remindStatus;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public String getShareIds() {
        return shareIds;
    }

    public void setShareIds(String shareIds) {
        this.shareIds = shareIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MemoContentBean> getContent() {
        return content;
    }

    public void setContent(List<MemoContentBean> content) {
        this.content = content;
    }

    public List<MemoLocationBean> getLocation() {
        return location;
    }

    public void setLocation(List<MemoLocationBean> location) {
        this.location = location;
    }

    public List<ModuleItemBean> getItemsArr() {
        return itemsArr;
    }

    public void setItemsArr(List<ModuleItemBean> itemsArr) {
        this.itemsArr = itemsArr;
    }
}
