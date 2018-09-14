package com.hjhq.teamface.project.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.bean.BaseBean
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.constants.ProjectConstants
import com.hjhq.teamface.basis.image.ImageLoader
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.util.ColorUtils
import com.hjhq.teamface.basis.util.DateTimeUtil
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.common.view.FlowLayout
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.PersonalTaskBean
import com.hjhq.teamface.project.presenter.task.PersonalTaskDetailActivity
import com.hjhq.teamface.project.presenter.task.TaskDetailActivity
import com.hjhq.teamface.project.util.ProjectDialog
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

/**
 * 个人任务列表适配器
 * Created by Administrator on 2018/4/10.
 */

class PersonalTaskAdapter(data: List<PersonalTaskBean>?) : BaseQuickAdapter<PersonalTaskBean, BaseViewHolder>(R.layout.project_personal_task_item, data) {
    var isSelect = false

    override fun convert(helper: BaseViewHolder, item: PersonalTaskBean) {
        val tvTaskName = helper.getView<TextView>(R.id.tv_task_name)
        val ivTaskHead = helper.getView<ImageView>(R.id.iv_head)

        val tvTaskTime = helper.getView<TextView>(R.id.tv_time)
        val ivCheck = helper.getView<ImageView>(R.id.iv_check)
        val tvCompleteNumber = helper.getView<TextView>(R.id.tv_complete_number)
        val flowPicklistTag = helper.getView<FlowLayout>(R.id.flow_picklist_tag)
        val tvSubTask = helper.getView<TextView>(R.id.tv_sub_task)

        //完成激活
        var completeStatus = "1" == item.complete_status
        ivCheck.isSelected = completeStatus
        ivCheck.isClickable = !isSelect
        tvTaskName.setTextColor(ColorUtils.resToColor(mContext, if (completeStatus) R.color.project_task_foot_text_size else R.color.black_17))

        //任务名称
        tvTaskName.text = item.text_name

        //执行人
        val members = item.personnel_execution
        if (!CollectionUtils.isEmpty(members)) {
            ImageLoader.loadCircleImage(mContext, members[0].picture, ivTaskHead, members[0].name)
        }

        //激活次数
        var compleateCount = item.activate_number

        TextUtil.setText(tvCompleteNumber, compleateCount.toString())
        helper.setVisible(R.id.tv_complete_number, compleateCount != 0)

        //时间
        val deadline = TextUtil.parseLong(item.datetime_deadline)
        val startTime = TextUtil.parseLong(item.datetime_starttime)
        helper.setVisible(R.id.tv_time, deadline != 0L || startTime != 0L)

        var taskTime = DateTimeUtil.AuthToTime(startTime)
        if (deadline != 0L) {
            if (startTime != 0L) {
                taskTime += " 至 "
            } else {
                taskTime = "截止时间 "
            }
            val taskDeadline = DateTimeUtil.AuthToTime(deadline)
            taskTime += taskDeadline
            TextUtil.setText(tvTaskTime, taskTime)
        }

        val overTime = System.currentTimeMillis() - deadline
        //时间图标字体变换
        if (deadline != 0L && overTime > 0) {
            tvTaskTime.setTextColor(ColorUtils.hexToColor("#FF3C26"))
        } else {
            tvTaskTime.setTextColor(ColorUtils.hexToColor("#5c5c69"))
        }


        //子任务
        if (item.subtotal == null || item.subtotal == 0) {
            tvSubTask.visibility = View.GONE
        } else {
            tvSubTask.visibility = View.VISIBLE
            val subfinish = item.subfinishtotal ?: 0
            TextUtil.setText(tvSubTask, subfinish.toString() + "/" + item.subtotal)
        }
        //完成激活
        if (!isSelect) {
            ivCheck.setOnClickListener {
                ProjectDialog.updatePersonalTaskStatus(false, mContext as RxAppCompatActivity, tvTaskName, item.id, completeStatus, object : ProgressSubscriber<BaseBean>(mContext, 1) {
                    override fun onNext(baseBean: BaseBean) {
                        super.onNext(baseBean)
                        completeStatus = !completeStatus
                        item.complete_status = if (completeStatus) "1" else "0"
                        ivCheck.isSelected = completeStatus
                        tvTaskName.setTextColor(ColorUtils.resToColor(mContext, if (completeStatus) R.color.black_17 else R.color.project_task_foot_text_size))
                    }
                })
            }
        }


        //跳转详情
        helper.setOnClickListener(R.id.card_view) {
            if (isSelect) {
                var intent = Intent()
                intent.putExtra(Constants.DATA_TAG1, item)
                if (mContext is ActivityPresenter<*, *>) {
                    (mContext as ActivityPresenter<*, *>).setResult(Activity.RESULT_OK, intent)
                    (mContext as ActivityPresenter<*, *>).finish()
                }
            } else {
                val bundle = Bundle()
                if (item.project_id != null) {
                    bundle.putLong(ProjectConstants.PROJECT_ID, item.project_id)
                    bundle.putLong(ProjectConstants.SUBNODE_ID, item.sub_id!!)
                    bundle.putLong(ProjectConstants.TASK_ID, item.id)
                    bundle.putString(ProjectConstants.TASK_NAME, item.text_name)
                    bundle.putString(Constants.MODULE_BEAN, item.bean_name)
                    CommonUtil.startActivtiy(mContext, TaskDetailActivity::class.java, bundle)
                } else {
                    bundle.putLong(ProjectConstants.TASK_ID, item.id)
                    bundle.putString(ProjectConstants.TASK_NAME, item.text_name)
                    bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN)
                    CommonUtil.startActivtiy(mContext, PersonalTaskDetailActivity::class.java, bundle)
                }
            }

        }

        //标签
        try {
            flowPicklistTag.removeAllViews()
            if (CollectionUtils.isEmpty(item.picklist_tag)) {
                flowPicklistTag.visibility = View.GONE
            } else {
                flowPicklistTag.visibility = View.VISIBLE

                val lp = ViewGroup.MarginLayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT)
                lp.rightMargin = 15
                for (label in item.picklist_tag) {
                    if (TextUtil.isEmpty(label.name)) {
                        continue
                    }
                    val view = TextView(mContext)
                    view.text = label.name
                    view.setTextColor(Color.WHITE)
                    view.textSize = 12f
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.project_task_tag_stroke)
                    val myGrad = view.background as GradientDrawable
                    if ("#FFFFFF" == label.colour) {
                        view.setTextColor(ColorUtils.resToColor(mContext, R.color.black_4a))
                    }
                    myGrad.setColor(ColorUtils.hexToColor(label.colour, "#000000"))
                    flowPicklistTag.addView(view, lp)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}