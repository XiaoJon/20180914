package com.hjhq.teamface.project.ui.task

import android.widget.EditText
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.project.R

/**
 * 截止时间
 * Created by Administrator on 2018/7/19.
 */
class BeforeDeadlineDelegate : AppDelegate() {
    private lateinit var etDeadline: EditText
    override fun getRootLayoutId(): Int = R.layout.project_activity_before_deadline

    override fun isToolBar(): Boolean = true


    override fun initWidget() {
        super.initWidget()
        etDeadline = get(R.id.et_deadline)
        setTitle("截止前")
        setRightMenuTexts(R.color.app_blue,mContext.getString(R.string.confirm))
    }

    fun setMinute() {
        clearUnit()
        setVisibility(R.id.iv_before_deadline_minute, true)
    }

    fun setHour() {
        clearUnit()
        setVisibility(R.id.iv_before_deadline_hour, true)
    }

    fun setDay() {
        clearUnit()
        setVisibility(R.id.iv_before_deadline_day, true)
    }
    fun clearUnit(){
        setVisibility(R.id.iv_before_deadline_minute, false)
        setVisibility(R.id.iv_before_deadline_hour, false)
        setVisibility(R.id.iv_before_deadline_day, false)
    }

    fun getDeadline(): String = etDeadline.text.toString().trim()

    fun setDeadline(deadline: String?) = TextUtil.setText(etDeadline, deadline)

}