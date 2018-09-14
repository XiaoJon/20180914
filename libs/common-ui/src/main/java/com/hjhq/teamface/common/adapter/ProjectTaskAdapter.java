package com.hjhq.teamface.common.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.bean.TaskInfoBean;

import java.util.List;

/**
 * 项目列表适配器
 *
 * @author Administrator
 * @date 2018/4/10
 */

public class ProjectTaskAdapter extends BaseQuickAdapter<TaskInfoBean, BaseViewHolder> {
    public ProjectTaskAdapter(List<TaskInfoBean> data) {
        super(R.layout.root_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfoBean item) {

    }
}
