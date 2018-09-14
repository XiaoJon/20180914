package com.hjhq.teamface.im.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 得到角色列表接口返回的数据实体类
 */
public class GetRoleReponseBean extends BaseBean {

    private List<RoleBean> data;

    public List<RoleBean> getData() {
        return data;
    }

    public void setData(List<RoleBean> data) {
        this.data = data;
    }

}
