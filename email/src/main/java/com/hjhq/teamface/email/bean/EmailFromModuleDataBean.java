package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/11/3
 */

public class EmailFromModuleDataBean extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * first_field : {"name":"picklist_1521341524527","label":"体育类别","value":"656565"}
         * email_fields : [{"name":"email_1521431523696","label":"电子邮箱","value":"125389889@126.com"}]
         */

        private FirstFieldBean first_field;
        private List<EmailFieldsBean> email_fields;

        public FirstFieldBean getFirst_field() {
            return first_field;
        }

        public void setFirst_field(FirstFieldBean first_field) {
            this.first_field = first_field;
        }

        public List<EmailFieldsBean> getEmail_fields() {
            return email_fields;
        }

        public void setEmail_fields(List<EmailFieldsBean> email_fields) {
            this.email_fields = email_fields;
        }

        public static class FirstFieldBean {
            /**
             * name : picklist_1521341524527
             * label : 体育类别
             * value : 656565
             */

            private String name;
            private String label;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
        }

        public static class EmailFieldsBean {
            /**
             * name : email_1521431523696
             * label : 电子邮箱
             * value : 125389889@126.com
             */

            private String name;
            private String label;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
        }
    }
}
