package com.hjhq.teamface.im.bean;

import java.util.List;

/**
 * Created by lx on 2017/6/6.
 */

public class HelperBean {
    /**
     * id : 110
     * createDate : null
     * disabled : null
     * assistName : 任务助手
     * assistId : 110
     * imUserName : null
     * noReadNum : 0
     */

    private long id;
    private String createDate;
    private String disabled;
    private String assistName;
    private int assistId;
    private String imUserName;
    private int noReadNum;
    private long companyId;
    private Integer top;



    public HelperBean(long id, String createDate, String disabled,
            String assistName, int assistId, String imUserName, int noReadNum,
            long companyId, Integer top) {
        this.id = id;
        this.createDate = createDate;
        this.disabled = disabled;
        this.assistName = assistName;
        this.assistId = assistId;
        this.imUserName = imUserName;
        this.noReadNum = noReadNum;
        this.companyId = companyId;
        this.top = top;
    }

    public HelperBean() {
    }

    public static void setCompnayId(List<HelperBean> list){
        if (list!=null){
            /*for (HelperBean bean : list) {
                bean.setCompanyId(HttpMethods.getCOMPANYID());
            }*/
        }
    }


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

    public String getAssistName() {
        return assistName;
    }

    public void setAssistName(String assistName) {
        this.assistName = assistName;
    }

    public int getAssistId() {
        return assistId;
    }

    public void setAssistId(int assistId) {
        this.assistId = assistId;
    }

    public String getImUserName() {
        return imUserName;
    }

    public void setImUserName(String imUserName) {
        this.imUserName = imUserName;
    }

    public int getNoReadNum() {
        return noReadNum;
    }

    public void setNoReadNum(int noReadNum) {
        this.noReadNum = noReadNum;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
