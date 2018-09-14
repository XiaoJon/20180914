package com.hjhq.teamface.project.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.image.ImageLoader
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.TaskLikeBean

/**
 * 点赞列表
 * Created by Administrator on 2018/7/19.
 */
class TaskThumbUpAdapter(list: List<TaskLikeBean>) : BaseQuickAdapter<TaskLikeBean, BaseViewHolder>(R.layout.project_item_task_thumb_up, list) {

    override fun convert(helper: BaseViewHolder, item: TaskLikeBean) {
        ImageLoader.loadCircleImage(mContext, item.picture, helper.getView(R.id.iv_head), item.name)
        TextUtil.setText(helper.getView<TextView>(R.id.tv_name), item.name)
    }

}