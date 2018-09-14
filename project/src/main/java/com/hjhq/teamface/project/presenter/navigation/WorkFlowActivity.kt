package com.hjhq.teamface.project.presenter.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber
import com.hjhq.teamface.basis.zygote.ActivityPresenter
import com.hjhq.teamface.basis.zygote.FragmentAdapter
import com.hjhq.teamface.project.bean.FlowNodeData
import com.hjhq.teamface.project.bean.FlowNodeResultBean
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.presenter.temp.ProjectTempSubNodeFragment
import com.hjhq.teamface.project.ui.navigation.WorkFlowDelegate
import java.util.*

/**
 * 项目模板节点流程
 */
class WorkFlowActivity : ActivityPresenter<WorkFlowDelegate, ProjectModel>() {
    private var flowId: Long = 0
    private lateinit var flowData: FlowNodeData
    private var fragments: ArrayList<Fragment> = ArrayList()

    override fun create(savedInstanceState: Bundle?) {
        super.create(savedInstanceState)
        if (savedInstanceState == null) {
            flowId = intent.getLongExtra(Constants.DATA_TAG1, 0)
        }
    }


    override fun init() = queryFlowNodes()

    private fun queryFlowNodes() =
            model.queryFlowNodesById(mContext, flowId, object : ProgressSubscriber<FlowNodeResultBean>(mContext) {
                override fun onNext(t: FlowNodeResultBean) {
                    super.onNext(t)
                    flowData = t.data
                    loadData()
                }
            })

    private fun loadData() {
        viewDelegate.setTitle(flowData.workflow.name)
        fragments.clear()
        for ((index, nodeBean) in flowData.nodes.withIndex()) {
            viewDelegate.addPoint()
            fragments.add(ProjectTempSubNodeFragment.newInstance(nodeBean.text, index))
        }
        viewDelegate.setAdapter(FragmentAdapter(supportFragmentManager, fragments))
        viewDelegate.setPointIndex(0)
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        viewDelegate.mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageSelected(position: Int) = viewDelegate.setPointIndex(position)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        intent.putExtra(Constants.DATA_TAG1, flowData)
        setResult(Activity.RESULT_OK, intent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}
