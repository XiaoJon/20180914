package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by lx on 2017/6/1.
 */

public class CreateGroupRequestBean {

    private String desc;//群描述
    private String name;//群名称
    private List<Long> memberId;//所有成员ID（list类型）
    private String background;//群背景
    private String ownerUserName;//该群的群主名称
    private List<String> membersUserName;//该群的成员名称，必须和极光账户名一致

    public CreateGroupRequestBean(){}
    public CreateGroupRequestBean(String desc,String name,List<Long> memberId,String background,String ownerUserName,List<String> membersUserName){
        this.desc = desc;
        this.name = name;
        this.memberId = memberId;
        this.background = background;
        this.ownerUserName = ownerUserName;
        this.membersUserName = membersUserName;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getMemberId() {
        return memberId;
    }

    public void setMemberId(List<Long> memberId) {
        this.memberId = memberId;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public List<String> getMembersUserName() {
        return membersUserName;
    }

    public void setMembersUserName(List<String> membersUserName) {
        this.membersUserName = membersUserName;
    }
}
