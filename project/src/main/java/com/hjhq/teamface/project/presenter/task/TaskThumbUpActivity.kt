package com.hjhq.teamface.project.presenter.task

import android.os.Bundle
import android.view.MenuItem
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.project.adapter.TaskThumbUpAdapter
import com.hjhq.teamface.project.bean.TaskLikeBean
import com.hjhq.teamface.project.model.TaskModel
import com.hjhq.teamface.project.ui.task.TaskThumbUpDelegate

/**
 * 点赞人列表
 */
class TaskThumbUpActivity : ActivityPresenter<TaskThumbUpDelegate, TaskModel>() {
    private lateinit var likes: ArrayList<TaskLikeBean>
    private lateinit var mAdapter: TaskThumbUpAdapter

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            likes = intent.getSerializableExtra(Constants.DATA_TAG1) as ArrayList<TaskLikeBean>
        }
    }

    override fun init() {
        mAdapter = TaskThumbUpAdapter(likes)
        viewDelegate.setAdapter(mAdapter)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
