package com.hjhq.teamface.project.presenter.task

import android.os.Bundle
import com.hjhq.teamface.basis.constants.ProjectConstants
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.project.adapter.TaskStatusAdapter
import com.hjhq.teamface.project.bean.PersonalTaskStatusResultBean
import com.hjhq.teamface.project.bean.TaskStatusResultBean
import com.hjhq.teamface.project.model.TaskModel
import com.hjhq.teamface.project.ui.task.TaskStatusDelegate

/**
 * 任务状态
 * Created by Administrator on 2018/8/20.
 */

class TaskStatusActivity : ActivityPresenter<TaskStatusDelegate, TaskModel>() {

    private var projectId: Long = 0
    private var taskId: Long = 0
    private var fromType: Long = 0

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0)
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0)
            fromType = intent.getLongExtra(ProjectConstants.TASK_FROM_TYPE, 0)
        }
    }

    override fun init() = if (projectId == 0L) {
        model.queryPersonalTaskStatus(mContext, taskId, fromType, object : ProgressSubscriber<PersonalTaskStatusResultBean>(mContext) {
            override fun onNext(t: PersonalTaskStatusResultBean) {
                super.onNext(t)
                viewDelegate.setAdapter(TaskStatusAdapter(t.data))
            }
        })
    } else {
        model.queryTaskStatus(mContext, projectId, taskId, fromType, object : ProgressSubscriber<TaskStatusResultBean>(mContext) {
            override fun onNext(t: TaskStatusResultBean) {
                super.onNext(t)
                viewDelegate.setAdapter(TaskStatusAdapter(t.data.dataList))
            }
        })
    }
}