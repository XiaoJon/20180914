package com.hjhq.teamface.project.presenter.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.util.ToastUtils
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.model.TaskModel
import com.hjhq.teamface.project.ui.task.RemindWayDelegate

/**
 * 提醒方式
 */
class RemindWayActivity : ActivityPresenter<RemindWayDelegate, TaskModel>(), View.OnClickListener {
    private lateinit var remindWay: ArrayList<Int>
    //    private val clickView: IntArray = intArrayOf(R.id.rl_company_letter, R.id.rl_company_wx, R.id.rl_ding_ding, R.id.rl_email)
    //TODO 目前只有企信可以 通知
    private val clickView: IntArray = intArrayOf(R.id.rl_company_letter)

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            remindWay = intent.getSerializableExtra(Constants.DATA_TAG1) as ArrayList<Int>
        }
    }

    override fun init() {
        if (!CollectionUtils.isEmpty(remindWay)) {
            for (way in remindWay) {
                setRemindWay(way)
            }
        }
    }

    private fun setRemindWay(way: Int) = when (way) {
        0 -> {
            viewDelegate.setCompanyLetter()
        }
        1 -> {
            viewDelegate.setCompanyWx()
        }
        2 -> {
            viewDelegate.setDingDing()
        }
        3 -> {
            viewDelegate.setEmail()
        }
        else -> {
        }
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        for (view in clickView) {
            viewDelegate.setOnClickListener(this, view)
        }
    }

    override fun onClick(v: View) {
        val way = clickView.indexOf(v.id)
        setRemindWay(way)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        next()
        return super.onOptionsItemSelected(item)
    }

    private fun next() {
        remindWay = viewDelegate.getRemindWay()
        if (CollectionUtils.isEmpty(remindWay)) {
            ToastUtils.showError(mContext, "请选择提醒方式")
            return
        }
        var intent = Intent()
        remindWay.sort()
        intent.putExtra(Constants.DATA_TAG1, remindWay)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
