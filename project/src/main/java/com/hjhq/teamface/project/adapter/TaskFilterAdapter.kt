package com.hjhq.teamface.project.adapter

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.constants.ProjectConstants
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.ConditionBean
import com.hjhq.teamface.project.bean.TaskConditionResultBean
import com.hjhq.teamface.project.ui.filter.weight.BaseFilterView
import com.hjhq.teamface.project.ui.filter.weight.filter.*
import com.hjhq.teamface.project.util.TaskHelper

/**
 * 任务筛选适配器
 * Created by Administrator on 2018/4/10.
 */

class TaskFilterAdapter(data: List<TaskConditionResultBean.DataBean>?) :
        BaseQuickAdapter<TaskConditionResultBean.DataBean, BaseViewHolder>(R.layout.project_filter_task, data) {
    private var viewArray: JSONObject = JSONObject()
    private var inflate: View? = null

    private val ivTimeSortId = intArrayOf(R.id.iv_over_time, R.id.iv_today, R.id.iv_tomorrow, R.id.iv_planned, R.id.iv_completed)
    private val timeSortClickId = intArrayOf(R.id.rl_over_time, R.id.rl_today, R.id.rl_tomorrow, R.id.rl_planned, R.id.rl_completed)
    private val tvTimeSortId = intArrayOf(R.id.tv_over_time, R.id.tv_today, R.id.tv_tomorrow, R.id.tv_planned, R.id.tv_completed)

    override fun convert(helper: BaseViewHolder, item: TaskConditionResultBean.DataBean) {
        var llContainer = helper.getView<LinearLayout>(R.id.ll_container)
        var ivArrows = helper.getView<ImageView>(R.id.iv_arrows)

        helper.setText(R.id.tv_name, item.beanName)

        helper.getView<View>(R.id.rl_name).setOnClickListener {
            val vis = llContainer.visibility == View.VISIBLE
            llContainer.visibility = if (vis) View.GONE else View.VISIBLE
            TaskHelper.rotateView(mContext, ivArrows, 0f, if (vis) -180f else 180f, 500, R.drawable.icon_sort_down)
        }

        if (ProjectConstants.PERSONAL_TASK_BEAN == item.bean) {
            //个人任务
            inflate = View.inflate(mContext, R.layout.project_layout_filter_time_sort, null)
            llContainer.removeAllViews()
            llContainer.addView(inflate)
            for (clickId in timeSortClickId) {
                inflate!!.findViewById<View>(clickId).setOnClickListener {
                    timeSortClick(clickId)
                }
            }
        }

        drawLayout(helper, llContainer, item.condition)
    }


    /**
     * 绘制筛选组件
     */
    private fun drawLayout(helper: BaseViewHolder, llContainer: LinearLayout, data: List<ConditionBean>) {
        var i = 0
        for (bean in data) {
            var view: BaseFilterView? = null
            when (bean.type) {
                "text"//单行文本
                    , "location"//定位
                    , "textarea"//多行文本
                    , "phone"//电话
                    , "email"//邮箱
                    , "url"//超链接
                    , "identifier"//自动编号
                    , "formula"//公式
                -> view = KeywordFilterView(bean)
                "number"//数字
                -> view = NumberFilterView(bean)
                "picklist"//下拉选项
                -> view = ItemFilterView(bean)
                "multi"//复选框
                -> view = ItemFilterView(bean)
                "personnel"//人员
                -> view = MemberFilterView(bean)
                "date" -> view = TimeFilterView(bean)
                "datetime"//时间
                -> view = TimeFilterView(bean)
                "area"//省市区
                -> view = AreaFilterView(bean)
                else -> {
                }
            }

            if (view != null) {
                view.addView(llContainer, llContainer.context as Activity)
                var viewList = viewArray.getJSONArray(helper.layoutPosition.toString())
                if (viewList == null) {
                    viewList = JSONArray()
                    viewArray[helper.layoutPosition.toString()] = viewList
                }
                viewList[i] = view
                i++
            }
        }
    }

    fun getViewList(): JSONObject = viewArray


    /**
     * 时间排序点击
     */
    private fun timeSortClick(id: Int) {
        if (inflate != null) {
            for ((i, clickId) in timeSortClickId.withIndex()) {
                inflate!!.findViewById<View>(ivTimeSortId[i]).isSelected = clickId === id
                inflate!!.findViewById<View>(tvTimeSortId[i]).isSelected = clickId === id
            }
        }
    }

    /**
     * 获取时间排序 结果
     */
    fun getTimeSort(): Int {
        if (inflate != null) {
            for ((i, ivId) in ivTimeSortId.withIndex()) {
                if (inflate!!.findViewById<View>(ivId).isSelected) {
                    return i
                }
            }
        }
        return -1
    }

    /**
     * 重置
     */
    fun reset() {
        timeSortClick(0)
        val viewList = getViewList()
        val keys1 = viewList.keys.iterator()

        while (keys1.hasNext()) {
            val next = keys1.next()
            val jsonArray = viewList.getJSONArray(next)
            jsonArray.indices
                    .map { jsonArray[it] as BaseFilterView }
                    .forEach { it.reset() }
        }

    }
}
