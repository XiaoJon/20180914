package com.hjhq.teamface.project.ui.task

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.adapter.TaskThumbUpAdapter

/**
 * Created by Administrator on 2018/7/19.
 */
class TaskThumbUpDelegate : AppDelegate() {
    override fun getRootLayoutId(): Int = R.layout.project_activity_task_thumb_up

    override fun isToolBar(): Boolean = true

    private lateinit var mRecyclerView: RecyclerView

    override fun initWidget() {
        super.initWidget()
        setRightMenuTexts(R.color.app_blue, "关闭")
        mRecyclerView = get(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun setAdapter(mAdapter: TaskThumbUpAdapter) {
        mRecyclerView.adapter = mAdapter
    }

}