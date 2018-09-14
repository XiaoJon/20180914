package com.hjhq.teamface.project.ui.navigation

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hjhq.teamface.basis.view.recycler.MyLinearDeviderDecoration
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.adapter.SelectWorkFlowAdapter

/**
 * 选择企业工作流
 * Created by Administrator on 2018/7/20.
 */
class SelectWorkFlowDelegate : AppDelegate() {
    lateinit var mRecyclerView: RecyclerView

    override fun getRootLayoutId(): Int = R.layout.project_activity_select_work_flow

    override fun isToolBar(): Boolean = true


    override fun initWidget() {
        super.initWidget()
        setTitle(R.string.project_template)
        mRecyclerView = get(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(MyLinearDeviderDecoration(mContext))
    }

    fun setAdapter(adapter: SelectWorkFlowAdapter) {
        mRecyclerView.adapter = adapter
    }
}