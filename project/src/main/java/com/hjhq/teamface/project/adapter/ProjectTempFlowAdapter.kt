package com.hjhq.teamface.project.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.hjhq.teamface.project.bean.TempNoteDataList
import com.hjhq.teamface.project.presenter.temp.ProjectTempNodeFragment

/**
 * 项目模板适配器
 * Created by Administrator on 2018/4/10.
 */

class ProjectTempFlowAdapter(fm: FragmentManager, private var nodeList: List<TempNoteDataList>) : OpenFragmentStatePagerAdapter<TempNoteDataList>(fm) {

    override fun getItemData(position: Int): TempNoteDataList = nodeList[position]

    internal override fun dataEquals(oldData: TempNoteDataList, newData: TempNoteDataList): Boolean =
            oldData.id == newData.id

    override fun getDataPosition(s: TempNoteDataList): Int =
            if (nodeList == null) -1 else nodeList.indexOf(s)


    override fun getCount(): Int = if (nodeList == null) 0 else nodeList.size

    override fun getItem(position: Int): Fragment {
        val nodeBean = nodeList[position]
        return ProjectTempNodeFragment.newInstance(nodeBean.id, nodeBean.subnodeArr)
    }
}
