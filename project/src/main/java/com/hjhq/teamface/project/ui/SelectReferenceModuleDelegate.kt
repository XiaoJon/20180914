package com.hjhq.teamface.project.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hjhq.teamface.basis.util.TextUtil
import com.hjhq.teamface.basis.zygote.AppDelegate
import com.hjhq.teamface.common.view.SearchBar
import com.hjhq.teamface.project.R
import com.hjhq.teamface.project.adapter.ProjectTempFlowAdapter

/**
 * 项目模板 的流程节点
 * Created by Administrator on 2018/7/20.
 */
class SelectReferenceModuleDelegate : AppDelegate() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ProjectTempFlowAdapter
    override fun getRootLayoutId(): Int = R.layout.project_activity_select_reference_module

    override fun isToolBar(): Boolean = true


    private lateinit var mSearchBar: SearchBar

    override fun initWidget() {
        super.initWidget()

        TextUtil.setText(get<TextView>(R.id.tv_name), "项目")
        mSearchBar = get(R.id.search_bar)
        mSearchBar.setCancelVis(false)
        mSearchBar.setHintText("搜索")

        mRecyclerView = get(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun setAdaper(mAdapter: BaseQuickAdapter<*, BaseViewHolder>) {
        mRecyclerView.adapter = mAdapter
    }

    fun hideSearchBar() {
        mSearchBar.visibility = View.GONE
    }

    fun hideProject() = setVisibility(R.id.rl_project, false)
}