package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * 修改群组成员请求实体
 * Created by lx on 2017/6/5.
 */

public class ModGroupMemberRequestBean {
    private String groupId;
    private Integer isTop;
    private List<Long> memberIds;
    private String background;

    public ModGroupMemberRequestBean(){}
    public ModGroupMemberRequestBean(String groupId,
                                     Integer isTop,
                                     List<Long> memberIds,
                                     String background){
        this.groupId = groupId;
        this.isTop = isTop;
        this.memberIds = memberIds;
        background = background;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
