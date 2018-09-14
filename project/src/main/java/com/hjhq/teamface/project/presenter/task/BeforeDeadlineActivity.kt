package com.hjhq.teamface.project.presenter.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.util.ToastUtils
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.model.TaskModel
import com.hjhq.teamface.project.ui.task.BeforeDeadlineDelegate

class BeforeDeadlineActivity : ActivityPresenter<BeforeDeadlineDelegate, TaskModel>(), View.OnClickListener {
    private var deadlineUnit = -1
    private var deadline: String? = null
    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState==null){
            deadline = intent.getStringExtra(Constants.DATA_TAG1)
            deadlineUnit = intent.getIntExtra(Constants.DATA_TAG2, -1)
        }
    }
    override fun init() {
        viewDelegate.setDeadline(deadline)
        setTimeUnit()
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.setOnClickListener(this, R.id.rl_before_deadline_minute, R.id.rl_before_deadline_hour, R.id.rl_before_deadline_day)
    }

    private fun setTimeUnit() = when (deadlineUnit) {
        0 -> {
            viewDelegate.setMinute()
        }
        1 -> {
            viewDelegate.setHour()
        }
        2 -> {
            viewDelegate.setDay()
        }
        else -> {
            viewDelegate.clearUnit()
        }
    }

    override fun onClick(v: View) {
        deadlineUnit = when (v.id) {
            R.id.rl_before_deadline_minute -> {
                0
            }
            R.id.rl_before_deadline_hour -> {
                1
            }
            R.id.rl_before_deadline_day -> {
                2
            }
            else -> {
                -1
            }
        }
        setTimeUnit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        next()
        return super.onOptionsItemSelected(item)
    }

    private fun next() {
        val deadline = viewDelegate.getDeadline()
        if (TextUtil.isEmpty(deadline)) {
            ToastUtils.showError(mContext, "请输入时间")
            return
        }
        if (deadlineUnit < 0) {
            ToastUtils.showError(mContext, "请选择时间单位")
            return
        }

        var intent = Intent()
        intent.putExtra(Constants.DATA_TAG1, deadline)
        intent.putExtra(Constants.DATA_TAG2, deadlineUnit)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
