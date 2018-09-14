package com.hjhq.teamface.basis.bean;


import com.hjhq.teamface.basis.database.Member;

import java.util.List;

/**
 * 筛选字段
 * Created by lx on 2017/8/25.
 */

public class FilterFieldResultBean extends BaseBean {

    /**
     * data : {"entity":"kehu","title":"客户","version":"1","layout":[{"title":"客户信息","isSpread":"false","type":"default_drop","rows":[{"name":"company","width":"316px","label":"公司名称","type":"text","field":{"type":"text","required":"true","defaultValue":"颜职印象","length":"200"}},{"name":"area","width":"316px","label":"区域","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"深圳"},{"value":"","label":"广州"},{"value":"","label":"上海"},{"value":"","label":"北京"}]}},{"name":"address","width":"316px","label":"地址","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"1000"}},{"name":"typeCode","width":"316px","label":"重要等级","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"VIP客户"},{"value":"","label":"重点客户"},{"value":"","label":"普通客户"}]}},{"name":"sourceCode","width":"316px","label":"消息来源","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"朋友推荐"},{"value":"","label":"网络"},{"value":"","label":"广告"}]}},{"name":"totalMoney","width":"316px","label":"累积购买金额","type":"text","field":{"type":"money","unit":"$","required":"true","defaultValue":"0.0"}},{"name":"createdDate","width":"316px","label":"创建时间","type":"datetime","field":{"type":"date","required":"true","defaultValue":"$NOW$"}},{"name":"createdBy","width":"316px","label":"所属用户","type":"text","field":{"type":"text","required":"true","defaultValue":"$user$","length":"200"}},{"name":"market","width":"732px","label":"备注","type":"textarea","field":{"type":"textarea","required":"true","defaultValue":""}}]},{"title":"联系人信息","isSpread":"false","type":"default_drop","rows":[{"name":"userName","width":"316px","label":"姓名","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"phone","width":"316px","label":"电话","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"mail","width":"316px","label":"邮箱","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"wechat","width":"316px","label":"微信","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}}]}]}
     * response : {"code":1001,"describe":"执行成功"}
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * name : 客户级别
         * id : customer_leve
         * type : picklist
         * entrys : [{"color":"#fff234","label":"大客户","value":"0"},{"color":"#fff234","label":"小客户","value":"1"},{"color":"#fff234","label":"中型客户","value":"2"},{"color":"#fff234","label":"VIP客户","value":"3"},{"color":"#fff234","label":"重点客户","value":"4"},{"color":"#fff234","label":"普通客户","value":"5"}]
         */

        private String name;
        private String id;
        private String type;
        private List<EntrysBean> entrys;
        private List<Member> member;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Member> getMember() {
            return member;
        }

        public void setMember(List<Member> member) {
            this.member = member;
        }

        public List<EntrysBean> getEntrys() {
            return entrys;
        }

        public void setEntrys(List<EntrysBean> entrys) {
            this.entrys = entrys;
        }

        public static class EntrysBean {
            /**
             * color : #fff234
             * label : 大客户
             * value : 0
             */

            private String color;
            private String label;
            private String value;
            private boolean checked;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }
        }
    }

}
