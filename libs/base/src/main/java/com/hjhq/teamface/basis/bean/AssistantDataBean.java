package com.hjhq.teamface.basis.bean;

import java.util.List;

public class AssistantDataBean {
    /**
     * bean_name : bean1515809747766
     * datetime_create_time : 1515816689975
     * assistant_id : 81
     * bean_name_chinese : 小助手推送1
     * read_status : 0
     * id : 9
     * "param_fields":"{"dataId":4,"moduleBean":"bean1524898371070"}",
     * push_content : 11111
     * field_info : [{"field_label":"名称","field_value":"u快快快","id":13,"type":"text"}]
     */

    private String bean_name;
    private String datetime_create_time;
    private String assistant_id;
    private String bean_name_chinese;
    private String read_status;
    private String id;
    private String data_id;
    private String param_fields;
    private String push_content;
    private String type;
    private String style;
    private String icon_url;
    private String icon_color;
    private String icon_type;
    private String im_apr_id;

    public String getIm_apr_id() {
        return im_apr_id;
    }

    public void setIm_apr_id(String im_apr_id) {
        this.im_apr_id = im_apr_id;
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

    //模块图标
    private String icon;
    private List<FieldInfoBean> field_info;

    public String getParam_fields() {
        return param_fields;
    }

    public void setParam_fields(String param_fields) {
        this.param_fields = param_fields;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBean_name() {
        return bean_name;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public void setBean_name(String bean_name) {
        this.bean_name = bean_name;
    }

    public String getDatetime_create_time() {
        return datetime_create_time;
    }

    public void setDatetime_create_time(String datetime_create_time) {
        this.datetime_create_time = datetime_create_time;
    }

    public String getAssistant_id() {
        return assistant_id;
    }

    public void setAssistant_id(String assistant_id) {
        this.assistant_id = assistant_id;
    }

    public String getBean_name_chinese() {
        return bean_name_chinese;
    }

    public void setBean_name_chinese(String bean_name_chinese) {
        this.bean_name_chinese = bean_name_chinese;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPush_content() {
        return push_content;
    }

    public void setPush_content(String push_content) {
        this.push_content = push_content;
    }

    public List<FieldInfoBean> getField_info() {
        return field_info;
    }

    public void setField_info(List<FieldInfoBean> field_info) {
        this.field_info = field_info;
    }

}