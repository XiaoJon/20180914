package com.hjhq.teamface.basis.bean;


import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by lx on 2017/8/25.
 */

public class CustomLayoutResultBean extends BaseBean {

    /**
     * data : {"entity":"kehu","title":"客户","version":"1","layout":[{"title":"客户信息","isSpread":"false","type":"default_drop","rows":[{"name":"company","width":"316px","label":"公司名称","type":"text","field":{"type":"text","required":"true","defaultValue":"颜职印象","length":"200"}},{"name":"area","width":"316px","label":"区域","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"深圳"},{"value":"","label":"广州"},{"value":"","label":"上海"},{"value":"","label":"北京"}]}},{"name":"address","width":"316px","label":"地址","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"1000"}},{"name":"typeCode","width":"316px","label":"重要等级","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"VIP客户"},{"value":"","label":"重点客户"},{"value":"","label":"普通客户"}]}},{"name":"sourceCode","width":"316px","label":"消息来源","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"朋友推荐"},{"value":"","label":"网络"},{"value":"","label":"广告"}]}},{"name":"totalMoney","width":"316px","label":"累积购买金额","type":"text","field":{"type":"money","unit":"$","required":"true","defaultValue":"0.0"}},{"name":"createdDate","width":"316px","label":"创建时间","type":"datetime","field":{"type":"date","required":"true","defaultValue":"$NOW$"}},{"name":"createdBy","width":"316px","label":"所属用户","type":"text","field":{"type":"text","required":"true","defaultValue":"$user$","length":"200"}},{"name":"market","width":"732px","label":"备注","type":"textarea","field":{"type":"textarea","required":"true","defaultValue":""}}]},{"title":"联系人信息","isSpread":"false","type":"default_drop","rows":[{"name":"userName","width":"316px","label":"姓名","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"phone","width":"316px","label":"电话","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"mail","width":"316px","label":"邮箱","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"wechat","width":"316px","label":"微信","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}}]}]}
     * response : {"code":1001,"describe":"执行成功"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * entity : kehu
         * title : 客户
         * version : 1
         * layout : [{"title":"客户信息","isSpread":"false","type":"default_drop","rows":[{"name":"company","width":"316px","label":"公司名称","type":"text","field":{"type":"text","required":"true","defaultValue":"颜职印象","length":"200"}},{"name":"area","width":"316px","label":"区域","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"深圳"},{"value":"","label":"广州"},{"value":"","label":"上海"},{"value":"","label":"北京"}]}},{"name":"address","width":"316px","label":"地址","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"1000"}},{"name":"typeCode","width":"316px","label":"重要等级","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"VIP客户"},{"value":"","label":"重点客户"},{"value":"","label":"普通客户"}]}},{"name":"sourceCode","width":"316px","label":"消息来源","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"朋友推荐"},{"value":"","label":"网络"},{"value":"","label":"广告"}]}},{"name":"totalMoney","width":"316px","label":"累积购买金额","type":"text","field":{"type":"money","unit":"$","required":"true","defaultValue":"0.0"}},{"name":"createdDate","width":"316px","label":"创建时间","type":"datetime","field":{"type":"date","required":"true","defaultValue":"$NOW$"}},{"name":"createdBy","width":"316px","label":"所属用户","type":"text","field":{"type":"text","required":"true","defaultValue":"$user$","length":"200"}},{"name":"market","width":"732px","label":"备注","type":"textarea","field":{"type":"textarea","required":"true","defaultValue":""}}]},{"title":"联系人信息","isSpread":"false","type":"default_drop","rows":[{"name":"userName","width":"316px","label":"姓名","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"phone","width":"316px","label":"电话","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"mail","width":"316px","label":"邮箱","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}},{"name":"wechat","width":"316px","label":"微信","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"200"}}]}]
         */

        private String bean;
        private String title;
        private String version;
        private String commentControl;
        private String dynamicControl;
        private List<LayoutBean> layout;

        public String getBean() {
            return bean;
        }

        public void setBean(String bean) {
            this.bean = bean;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<LayoutBean> getLayout() {
            return layout;
        }

        public String getCommentControl() {
            return commentControl;
        }

        public void setCommentControl(String commentControl) {
            this.commentControl = commentControl;
        }

        public String getDynamicControl() {
            return dynamicControl;
        }

        public void setDynamicControl(String dynamicControl) {
            this.dynamicControl = dynamicControl;
        }

        public void setLayout(List<LayoutBean> layout) {
            this.layout = layout;
        }

        public static class LayoutBean {
            /**
             * title : 客户信息
             * isSpread : false
             * terminalApp : 1
             * rows : [{"name":"company","width":"316px","label":"公司名称","type":"text","field":{"type":"text","required":"true","defaultValue":"颜职印象","length":"200"}},{"name":"area","width":"316px","label":"区域","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"深圳"},{"value":"","label":"广州"},{"value":"","label":"上海"},{"value":"","label":"北京"}]}},{"name":"address","width":"316px","label":"地址","type":"text","field":{"type":"text","required":"true","defaultValue":"","length":"1000"}},{"name":"typeCode","width":"316px","label":"重要等级","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"VIP客户"},{"value":"","label":"重点客户"},{"value":"","label":"普通客户"}]}},{"name":"sourceCode","width":"316px","label":"消息来源","type":"picklist","field":{"type":"int","required":"true","defaultValue":"0","length":"1","entrys":[{"value":"0","label":"朋友推荐"},{"value":"","label":"网络"},{"value":"","label":"广告"}]}},{"name":"totalMoney","width":"316px","label":"累积购买金额","type":"text","field":{"type":"money","unit":"$","required":"true","defaultValue":"0.0"}},{"name":"createdDate","width":"316px","label":"创建时间","type":"datetime","field":{"type":"date","required":"true","defaultValue":"$NOW$"}},{"name":"createdBy","width":"316px","label":"所属用户","type":"text","field":{"type":"text","required":"true","defaultValue":"$user$","length":"200"}},{"name":"market","width":"732px","label":"备注","type":"textarea","field":{"type":"textarea","required":"true","defaultValue":""}}]
             */

            private String title;
            private String terminalApp;
            private String isHideInCreate;//新增时是否隐藏分栏
            private String isHideInDetail;//详情时是否隐藏分栏
            private String isHideColumnName;//是否隐藏分栏名称
            private String isSpread;//是否展开
            private String name;

            private List<CustomBean> rows;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIsHideInCreate() {
                return isHideInCreate;
            }

            public void setIsHideInCreate(String isHideInCreate) {
                this.isHideInCreate = isHideInCreate;
            }

            public String getIsHideInDetail() {
                return isHideInDetail;
            }

            public void setIsHideInDetail(String isHideInDetail) {
                this.isHideInDetail = isHideInDetail;
            }

            public String getIsHideColumnName() {
                return isHideColumnName;
            }

            public void setIsHideColumnName(String isHideColumnName) {
                this.isHideColumnName = isHideColumnName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CustomBean> getRows() {
                return rows;
            }

            public void setRows(List<CustomBean> rows) {
                this.rows = rows;
            }

            public String getTerminalApp() {
                return terminalApp;
            }

            public void setTerminalApp(String terminalApp) {
                this.terminalApp = terminalApp;
            }

            public String getIsSpread() {
                return isSpread;
            }

            public void setIsSpread(String isSpread) {
                this.isSpread = isSpread;
            }
        }
    }

}
