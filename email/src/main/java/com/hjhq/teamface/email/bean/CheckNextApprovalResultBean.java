package com.hjhq.teamface.email.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.database.Member;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 判断是否需要选择下一审批人 返回实体bean
 *
 * @author Administrator
 * @date 2018/3/26
 */

public class CheckNextApprovalResultBean extends BaseBean {
    private CheckNextApproval data;

    public static class CheckNextApproval implements Serializable {
        private String processType;
        private String ccTo;
        private ArrayList<Member> choosePersonnel;

        public String getCcTo() {
            return ccTo;
        }

        public void setCcTo(String ccTo) {
            this.ccTo = ccTo;
        }

        public String getProcess_type() {
            return processType
                    ;
        }

        public void setProcess_type(String process_type) {
            this.processType
                    = process_type;
        }

        public ArrayList<Member> getChoosePersonnel() {
            return choosePersonnel;
        }

        public void setChoosePersonnel(ArrayList<Member> choosePersonnel) {
            this.choosePersonnel = choosePersonnel;
        }
    }

    public CheckNextApproval getData() {
        return data;
    }

    public void setData(CheckNextApproval data) {
        this.data = data;
    }
}
