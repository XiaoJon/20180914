package com.hjhq.teamface.project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.common.bean.ProjectListBean
import com.hjhq.teamface.project.R

/**
 * 个人任务关联选择项目
 * Created by Administrator on 2018/4/10.
 */

class ReferenceChooseProjectAdapter(data: List<ProjectListBean.DataBean.DataListBean>?) :
        BaseQuickAdapter<ProjectListBean.DataBean.DataListBean, BaseViewHolder>(R.layout.project_item_reference_module, data) {

    override fun convert(helper: BaseViewHolder, item: ProjectListBean.DataBean.DataListBean) {
        helper.setText(R.id.tv_name, item.name)
    }
}
