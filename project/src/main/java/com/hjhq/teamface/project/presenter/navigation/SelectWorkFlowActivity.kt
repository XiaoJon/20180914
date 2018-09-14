package com.hjhq.teamface.project.presenter.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.util.CollectionUtils
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.common.utils.CommonUtil
import com.hjhq.teamface.project.adapter.SelectWorkFlowAdapter
import com.hjhq.teamface.project.bean.WorkFlowResultBean
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.ui.navigation.SelectWorkFlowDelegate

class SelectWorkFlowActivity : ActivityPresenter<SelectWorkFlowDelegate, ProjectModel>() {
    private lateinit var mAdapter: SelectWorkFlowAdapter

    override fun init() {
        mAdapter = SelectWorkFlowAdapter(null)
        viewDelegate.setAdapter(mAdapter)
        queryWorkFlow()
    }

    private fun queryWorkFlow() =
            model.queryWorkFlow(mContext, object : ProgressSubscriber<WorkFlowResultBean>(mContext) {
                override fun onNext(t: WorkFlowResultBean) {
                    super.onNext(t)
                    CollectionUtils.notifyDataSetChanged(mAdapter, mAdapter.data, t.data)
                }
            })


    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.mRecyclerView.addOnItemTouchListener(object : SimpleItemClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                super.onItemClick(adapter, view, position)
                val item = mAdapter.getItem(position)
                val bundle = Bundle()
                bundle.putLong(Constants.DATA_TAG1, item.id)
                CommonUtil.startActivtiyForResult(mContext, WorkFlowActivity::class.java, Constants.REQUEST_CODE1, bundle)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK,data)
            finish()
        }
    }
}
