package com.hjhq.teamface.project.presenter.temp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.hjhq.teamface.basis.constants.Constants
import com.hjhq.teamface.basis.constants.ProjectConstants
import com.hjhq.teamface.basis.zygote.FragmentAdapter
import com.hjhq.teamface.basis.zygote.FragmentPresenter
import com.hjhq.teamface.project.bean.TempNoteSubnodeArr
import com.hjhq.teamface.project.model.ProjectModel
import com.hjhq.teamface.project.ui.temp.ProjectTempNodeDelegate
import java.io.Serializable
import java.util.*

/**
 * 项目模板流程
 * Created by Administrator on 2018/7/20.
 */
class ProjectTempNodeFragment : FragmentPresenter<ProjectTempNodeDelegate, ProjectModel>() {
    private var nodeId: Long = 0
    private lateinit var subNodeList: List<TempNoteSubnodeArr>
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var mAdapter: FragmentAdapter

    companion object {
        fun newInstance(nodeId: Long, subnodeArr: List<TempNoteSubnodeArr>): ProjectTempNodeFragment {
            val myFragment = ProjectTempNodeFragment()
            val bundle = Bundle()
            bundle.putSerializable(Constants.DATA_TAG1, subnodeArr as Serializable)
            bundle.putLong(ProjectConstants.NODE_ID, nodeId)
            myFragment.arguments = bundle
            return myFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            subNodeList = arguments.getSerializable(Constants.DATA_TAG1) as List<TempNoteSubnodeArr>
            nodeId = arguments.getLong(ProjectConstants.NODE_ID, 0)
        }
    }


    override fun init() {
        fragments = ArrayList(subNodeList.size)
        var index =0
        for (nodeBean in subNodeList) {
            viewDelegate.addPoint()
            fragments.add(ProjectTempSubNodeFragment.newInstance(nodeBean.name,index++))
        }
        mAdapter = FragmentAdapter(childFragmentManager, fragments)
        viewDelegate.setAdapter(mAdapter)
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
}
