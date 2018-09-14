package com.hjhq.teamface.custom.bean;

/**
 * 保存或修改自定义数据
 * Created by lx on 2017/9/14.
 */

public class SaveCustomDataRequestBean {

    /**
     * id :
     * bean  : commodity
     * data : {"company":"花旗银行","area":"1","address":"广东省深圳市南山科技园","typeCode":"1","sourceCode":"1","totalMoney":"136945.809","createdDate":"2017-8-23 12:05:06","createdBy":"Leon","market":"","userName":"Andy","phone":"1368415219","mail":"andy@gmail.com","wechat":"1368415219"}
     */

    private String id;
    private String title;
    private String bean;
    private String moduleId;
    private Object data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
