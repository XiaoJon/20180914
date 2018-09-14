package com.hjhq.teamface.project.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.image.ImageLoader
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.TempDataList

/**
 * 项目模板适配器
 * Created by Administrator on 2018/4/10.
 */
class ProjectTemplateAdapter(data: List<TempDataList>?) : BaseQuickAdapter<TempDataList, BaseViewHolder>(R.layout.project_template_item, data) {
    override fun convert(helper: BaseViewHolder, item: TempDataList) {

        if (item.temp_type == 0 || (helper.layoutPosition != 0 && getItem(helper.layoutPosition - 1).temp_type == item.temp_type)) {
            helper.setVisible(R.id.rl_project_type, false)
        } else {
            helper.setVisible(R.id.rl_project_type, true)
            helper.setText(R.id.tv_type_name, item.temp_name)
        }

        if (item.temp_type == 0) {
            ImageLoader.loadRoundImage(mContext, item.pic_url, helper.getView(R.id.iv_project_icon), R.drawable.project_temp_icon_1)
        } else {
            val iconName = "project_temp_icon_" + item.id
            val resId = mContext.resources.getIdentifier(iconName, "drawable", mContext.packageName)
            ImageLoader.loadRoundImage(mContext, resId, helper.getView(R.id.iv_project_icon), R.drawable.ic_image)
        }

        helper.setText(R.id.tv_project_name, item.name)
    }
}
