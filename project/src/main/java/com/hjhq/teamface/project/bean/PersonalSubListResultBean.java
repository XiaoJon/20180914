package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class PersonalSubListResultBean extends BaseBean {

    /**
     * data : {"dataList":[]}
     */

    private List<PersonalSubTaskBean> data;

    public List<PersonalSubTaskBean> getData() {
        return data;
    }

    public void setData(List<PersonalSubTaskBean> data) {
        this.data = data;
    }

}
