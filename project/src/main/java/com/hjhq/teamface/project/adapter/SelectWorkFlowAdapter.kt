package com.hjhq.teamface.project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.WorkFlowData

/**
 * 项目模板适配器
 * Created by Administrator on 2018/4/10.
 */
class SelectWorkFlowAdapter(data: List<WorkFlowData>?) : BaseQuickAdapter<WorkFlowData, BaseViewHolder>(R.layout.project_item_work_flow, data) {
    override fun convert(helper: BaseViewHolder, item: WorkFlowData) {
        helper.setText(R.id.tv_name, item.name)
    }
}
