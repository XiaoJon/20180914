package com.hjhq.teamface.project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.NodeBean

/**
 * 个人任务关联选择分组
 * Created by Administrator on 2018/4/10.
 */

class ReferenceChooseNodeAdapter(data: List<NodeBean>?) :
        BaseQuickAdapter<NodeBean, BaseViewHolder>(R.layout.project_item_reference_module, data) {

    override fun convert(helper: BaseViewHolder, item: NodeBean) {
        helper.setText(R.id.tv_name, item.name)
    }
}
