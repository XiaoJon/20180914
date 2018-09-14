package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class PersonalTaskListResultBean extends BaseBean {

    private List<PersonalTaskLikeBean> data;

    public List<PersonalTaskLikeBean> getData() {
        return data;
    }

    public void setData(List<PersonalTaskLikeBean> data) {
        this.data = data;
    }
}
