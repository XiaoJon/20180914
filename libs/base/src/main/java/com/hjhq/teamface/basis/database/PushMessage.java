package com.hjhq.teamface.basis.database;

import com.hjhq.teamface.basis.bean.FieldInfoBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/22.
 * Describe：
 */
@Entity
public class PushMessage implements Serializable {
    /**
     * type : 3
     * push_content : 推送信息
     * assistant_id : 2
     * bean_name : customer
     * bean_name_chinese : 客户模块
     * 任务:"param_fields":"{"data_Type":2,"sub_id":398,"taskInfoId":570,"beanName":"project_custom_60","taskName":"莫非朕不知兵？","projectId":60}",
     * title : 有人@你
     * sender_name : 张三
     * field_info : [{"id ":2,"field_label":"字段名","field_value":"1"}]
     * create_time : 15241231121
     * read_satus : 1
     */
    /**
     *
     */
    // TODO: 2018/8/3 审批状态变更推送 找到原来的推送
    /*{
    "bean_name":"bean1529635156119",
    "param_fields":{
        "dataId":18,
        "fromType":"2",
        "taskKey":"task1529635952541",
        "moduleBean":"bean1529635156119"
    },
    "assistant_id":3,
    "create_time":1533266495186,
    "im_apr_id":"2572",
    "bean_name_chinese":"审批",
    "push_content":"已执行通过操作",
    "id":2577,
    "type":20,
    "field_info":[
        {
            "field_label":"",
            "field_value":""
        }
    ]
}*/
    @Transient
    static final long serialVersionUID = 1L;

    @Id
    long id;
    private String type;
    private String push_content;
    private String assistant_id;
    private String bean_name;
    private String bean_name_chinese;
    private String title;
    private String sender_name;
    private String create_time;
    private String read_status;
    private String group_id;
    private String assistant_name;
    private String myId;
    private String companyId;
    private String fieldInfo;
    private String orginFieldInfo;
    private String style;
    private String data_id;
    private String param_fields;
    private String icon_url;
    private String icon_color;
    private String icon_type;
    private String icon;
    //与之前的推送相关
    private String im_apr_id;
    @Transient
    private ArrayList<FieldInfoBean> field_info;


    public PushMessage() {
    }

    @Generated(hash = 487520779)
    public PushMessage(long id, String type, String push_content,
            String assistant_id, String bean_name, String bean_name_chinese,
            String title, String sender_name, String create_time,
            String read_status, String group_id, String assistant_name, String myId,
            String companyId, String fieldInfo, String orginFieldInfo, String style,
            String data_id, String param_fields, String icon_url, String icon_color,
            String icon_type, String icon, String im_apr_id) {
        this.id = id;
        this.type = type;
        this.push_content = push_content;
        this.assistant_id = assistant_id;
        this.bean_name = bean_name;
        this.bean_name_chinese = bean_name_chinese;
        this.title = title;
        this.sender_name = sender_name;
        this.create_time = create_time;
        this.read_status = read_status;
        this.group_id = group_id;
        this.assistant_name = assistant_name;
        this.myId = myId;
        this.companyId = companyId;
        this.fieldInfo = fieldInfo;
        this.orginFieldInfo = orginFieldInfo;
        this.style = style;
        this.data_id = data_id;
        this.param_fields = param_fields;
        this.icon_url = icon_url;
        this.icon_color = icon_color;
        this.icon_type = icon_type;
        this.icon = icon;
        this.im_apr_id = im_apr_id;
    }

    public String getIm_apr_id() {
        return im_apr_id;
    }

    public void setIm_apr_id(String im_apr_id) {
        this.im_apr_id = im_apr_id;
    }

    public String getParam_fields() {
        return param_fields;
    }

    public void setParam_fields(String param_fields) {
        this.param_fields = param_fields;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPush_content() {
        return push_content;
    }

    public void setPush_content(String push_content) {
        this.push_content = push_content;
    }

    public String getAssistant_id() {
        return assistant_id;
    }

    public void setAssistant_id(String assistant_id) {
        this.assistant_id = assistant_id;
    }

    public String getBean_name() {
        return bean_name;
    }

    public void setBean_name(String bean_name) {
        this.bean_name = bean_name;
    }

    public String getBean_name_chinese() {
        return bean_name_chinese;
    }

    public void setBean_name_chinese(String bean_name_chinese) {
        this.bean_name_chinese = bean_name_chinese;
    }

    public String getOrginFieldInfo() {
        return orginFieldInfo;
    }

    public void setOrginFieldInfo(String orginFieldInfo) {
        this.orginFieldInfo = orginFieldInfo;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public ArrayList<FieldInfoBean> getField_info() {
        return field_info;
    }

    public void setField_info(ArrayList<FieldInfoBean> field_info) {
        this.field_info = field_info;
    }

    public String getAssistant_name() {
        return assistant_name;
    }

    public void setAssistant_name(String assistant_name) {
        this.assistant_name = assistant_name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(String icon_color) {
        this.icon_color = icon_color;
    }

    public String getIcon_type() {
        return icon_type;
    }

    public void setIcon_type(String icon_type) {
        this.icon_type = icon_type;
    }
}
