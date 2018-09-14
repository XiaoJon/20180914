package com.hjhq.teamface.project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.hjhq.teamface.project.bean.NodeBean;
import com.hjhq.teamface.project.presenter.TaskTempFragment;

import java.util.List;

/**
 * 任务看板适配器 动态更改viewpager
 * Created by lx on 2017/3/23.
 */

public class MyOpenFragmentStatePagerAdapter extends OpenFragmentStatePagerAdapter<NodeBean> {
    private List<NodeBean> nodeList;

    public MyOpenFragmentStatePagerAdapter(FragmentManager fm, List<NodeBean> data) {
        super(fm);
        this.nodeList = data;
    }

    @Override
    public NodeBean getItemData(int position) {
        return nodeList.get(position);
    }

    @Override
    boolean dataEquals(NodeBean oldData, NodeBean newData) {
        return oldData.getId() == newData.getId();
    }

    @Override
    public int getDataPosition(NodeBean s) {
        return nodeList == null ? -1 : nodeList.indexOf(s);
    }


    @Override
    public int getCount() {
        return nodeList == null ? 0 : nodeList.size();
    }

    @Override
    public Fragment getItem(int position) {
        NodeBean nodeBean = nodeList.get(position);
        return TaskTempFragment.newInstance(nodeBean.getId(), nodeBean.getSubnodeArr());
    }

    public List<NodeBean> getData() {
        return nodeList;
    }
}
