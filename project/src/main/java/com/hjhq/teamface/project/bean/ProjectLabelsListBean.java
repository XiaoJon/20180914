package com.hjhq.teamface.project.bean;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.common.bean.ProjectLabelBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 当前项目标签(项目模块,有层级)
 * @author Administrator
 */

public class ProjectLabelsListBean extends BaseBean implements Serializable {

    private ArrayList<ProjectLabelBean> data;

    public ArrayList<ProjectLabelBean> getData() {
        return data;
    }

    public void setData(ArrayList<ProjectLabelBean> data) {
        this.data = data;
    }

}
