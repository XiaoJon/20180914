package com.hjhq.teamface.basis.bean;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2017/10/19
 * Describe：名片信息
 */

public class EmployeeCardDetailBean extends BaseBean implements Serializable {
    DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        String card_template;
        String choice_template;
        String hide_set;

        public String getCard_template() {
            return card_template;
        }

        public void setCard_template(String card_template) {
            this.card_template = card_template;
        }

        public String getChoice_template() {
            return choice_template;
        }

        public void setChoice_template(String choice_template) {
            this.choice_template = choice_template;
        }

        public String getHide_set() {
            return hide_set;
        }

        public void setHide_set(String hide_set) {
            this.hide_set = hide_set;
        }
    }


}