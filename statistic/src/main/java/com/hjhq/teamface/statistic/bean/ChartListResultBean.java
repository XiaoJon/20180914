package com.hjhq.teamface.statistic.bean;

import com.hjhq.teamface.basis.bean.BaseBean;

import java.util.List;

/**
 * 仪表盘列表
 *
 * @author Administrator
 * @date 2018/3/19
 */

public class ChartListResultBean extends BaseBean {

    private List<MenuBean> data;

    public List<MenuBean> getData() {
        return data;
    }

    public void setData(List<MenuBean> data) {
        this.data = data;
    }

}
