package com.hjhq.teamface.project.ui.task

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.project.R

/**
 * 任务状态
 * Created by Administrator on 2018/8/20.
 */
class TaskMemberDelegate : AppDelegate() {
    lateinit var mRecyclerView: RecyclerView
    override fun getRootLayoutId(): Int = R.layout.project_activity_task_status

    override fun isToolBar(): Boolean = true

    override fun initWidget() {
        super.initWidget()
        setTitle("协作人")
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.add))
        mRecyclerView = get(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(MyLinearDeviderDecoration(mContext, R.color.gray_f2))

    }

    fun setAdapter(adapter: BaseQuickAdapter<*, BaseViewHolder>) {
        mRecyclerView.adapter = adapter
    }
}