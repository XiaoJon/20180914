package com.hjhq.teamface.project.ui.task

import android.view.View
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.project.R

/**
 * 截止时间
 * Created by Administrator on 2018/7/19.
 */
class RemindWayDelegate : AppDelegate() {
    override fun getRootLayoutId(): Int = R.layout.project_activity_remind_way

    override fun isToolBar(): Boolean = true


    override fun initWidget() {
        super.initWidget()
        setTitle(R.string.project_remind_way)
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm))
    }

    fun setCompanyLetter() =
            setVisibility(R.id.iv_company_letter, get<View>(R.id.iv_company_letter).visibility != View.VISIBLE)

    fun setCompanyWx() =
            setVisibility(R.id.iv_company_wx, get<View>(R.id.iv_company_wx).visibility != View.VISIBLE)

    fun setDingDing() =
            setVisibility(R.id.iv_ding_ding, get<View>(R.id.iv_ding_ding).visibility != View.VISIBLE)

    fun setEmail() =
            setVisibility(R.id.iv_email, get<View>(R.id.iv_email).visibility != View.VISIBLE)

    fun getRemindWay(): ArrayList<Int> {
        var remindWay = ArrayList<Int>()
        if (get<View>(R.id.iv_company_letter).visibility == View.VISIBLE) {
            remindWay.add(0)
        }
        if (get<View>(R.id.iv_company_wx).visibility == View.VISIBLE) {
            remindWay.add(1)
        }
        if (get<View>(R.id.iv_ding_ding).visibility == View.VISIBLE) {
            remindWay.add(2)
        }
        if (get<View>(R.id.iv_email).visibility == View.VISIBLE) {
            remindWay.add(3)
        }
        return remindWay
    }

}