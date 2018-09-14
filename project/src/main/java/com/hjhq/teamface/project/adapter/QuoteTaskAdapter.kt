package com.hjhq.teamface.project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.QuoteTaskListResultBean

/**
 * 个人任务适配器
 * Created by Administrator on 2018/4/10.
 */

class QuoteTaskAdapter(data: List<QuoteTaskListResultBean.DataBean>?) :
        BaseQuickAdapter<QuoteTaskListResultBean.DataBean, BaseViewHolder>(R.layout.project_quote_task_item, data) {

    override fun convert(helper: BaseViewHolder, item: QuoteTaskListResultBean.DataBean) {
        helper.setText(R.id.tv_name, item.title)
    }
}
