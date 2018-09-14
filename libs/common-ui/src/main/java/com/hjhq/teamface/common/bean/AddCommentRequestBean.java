package com.hjhq.teamface.common.bean;


import com.hjhq.teamface.basis.bean.UploadFileBean;

import java.util.List;

/**
 * 添加评论请求bean
 * Created by lx on 2017/9/14.
 */

public class AddCommentRequestBean {

    /**
     * id :
     * bean  : commodity
     * data : {"company":"花旗银行","area":"1","address":"广东省深圳市南山科技园","typeCode":"1","sourceCode":"1","totalMoney":"136945.809","createdDate":"2017-8-23 12:05:06","createdBy":"Leon","market":"","userName":"Andy","phone":"1368415219","mail":"andy@gmail.com","wechat":"1368415219"}
     */

    private String style;
    private String relation_id;
    private String bean;
    private String content;//内容
    private String at_employee;//审批@评论时要传

    private String processInstanceId;
    private String fromType;
    private String dataId;
    private String moduleBean;
    private List<UploadFileBean> information;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getAt_employee() {
        return at_employee;
    }

    public void setAt_employee(String at_employee) {
        this.at_employee = at_employee;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(String moduleBean) {
        this.moduleBean = moduleBean;
    }
    public List<UploadFileBean> getInformation() {
        return information;
    }

    public void setInformation(List<UploadFileBean> information) {
        this.information = information;
    }
}
