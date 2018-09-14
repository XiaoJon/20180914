package com.hjhq.teamface.project.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.database.Member
import com.hjhq.teamface.basis.image.ImageLoader
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.project.R

/**
 * 协作人
 * Created by Administrator on 2018/4/10.
 */

class TaskMemberAdapter(data: List<Member>?) :
        BaseQuickAdapter<Member, BaseViewHolder>(R.layout.project_task_member_item, data) {

    override fun convert(helper: BaseViewHolder, item: Member) {
        ImageLoader.loadCircleImage(mContext, item.picture, helper.getView(R.id.iv_head), item.name)

        TextUtil.setText(helper.getView<TextView>(R.id.tv_title), item.name)
        TextUtil.setText(helper.getView<TextView>(R.id.tv_sub_title), item.post_name)
        helper.addOnClickListener(R.id.tv_remove)
    }
}
