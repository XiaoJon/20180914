package com.hjhq.teamface.project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.CustomModuleResultBean

/**
 * 个人任务关联选择模块
 * Created by Administrator on 2018/4/10.
 */

class SelectReferenceModuleAdapter(data: List<CustomModuleResultBean.DataBean>?) :
        BaseQuickAdapter<CustomModuleResultBean.DataBean, BaseViewHolder>(R.layout.project_item_reference_module, data) {

    override fun convert(helper: BaseViewHolder, item: CustomModuleResultBean.DataBean) {
        helper.setText(R.id.tv_name, item.name)
    }
}
