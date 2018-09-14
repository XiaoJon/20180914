package com.hjhq.teamface.project.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.image.ImageLoader
import com.hjhq.teamface.basis.util.ColorUtils
import com.hjhq.teamface.basis.util.DateTimeUtil
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.TaskStatusBean

/**
 * 任务状态
 * Created by Administrator on 2018/4/10.
 */

class TaskStatusAdapter(data: List<TaskStatusBean>?) :
        BaseQuickAdapter<TaskStatusBean, BaseViewHolder>(R.layout.project_task_status_item, data) {

    override fun convert(helper: BaseViewHolder, item: TaskStatusBean) {
        ImageLoader.loadCircleImage(mContext, item.employee_pic, helper.getView(R.id.iv_head), item.employee_name)
        TextUtil.setText(helper.getView<TextView>(R.id.tv_name), item.employee_name)

        if ("1" == item.read_status) {
            helper.setText(R.id.tv_status, "已查看")
            helper.setTextColor(R.id.tv_status, ColorUtils.resToColor(mContext, R.color.gray))
        } else {
            helper.setText(R.id.tv_status, "未查看")
            helper.setTextColor(R.id.tv_status, ColorUtils.resToColor(mContext, R.color.red_f4))
        }

        val parseLong = TextUtil.parseLong(item.read_time)
        if (parseLong == 0L) {
            helper.setVisible(R.id.tv_time, false)
        } else {
            helper.setVisible(R.id.tv_time, true)
            val time = DateTimeUtil.longToStr_YYYY_MM_DD_HH_MM(TextUtil.parseLong(item.read_time))
            TextUtil.setText(helper.getView<TextView>(R.id.tv_time), time)
        }
    }
}
