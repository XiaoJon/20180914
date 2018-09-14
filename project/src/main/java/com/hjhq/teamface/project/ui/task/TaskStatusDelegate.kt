package com.hjhq.teamface.project.ui.task

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.bean.TaskStatusBean

/**
 * 任务状态
 * Created by Administrator on 2018/8/20.
 */
class TaskStatusDelegate : AppDelegate() {
    lateinit var mRecyclerView: RecyclerView
    override fun getRootLayoutId(): Int = R.layout.project_activity_task_status

    override fun isToolBar(): Boolean = true

    override fun initWidget() {
        super.initWidget()
        setTitle("任务状态")
        mRecyclerView = get(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun setAdapter(adapter: BaseQuickAdapter<TaskStatusBean, BaseViewHolder>) {
        mRecyclerView.adapter = adapter
    }
}