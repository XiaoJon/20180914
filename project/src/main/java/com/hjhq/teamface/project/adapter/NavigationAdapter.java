package com.hjhq.teamface.project.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.NodeBean;

import java.util.List;

/**
 * 节点适配器
 * @author Administrator
 * @date 2018/4/16
 */

public class NavigationAdapter extends BaseItemDraggableAdapter<NodeBean, BaseViewHolder> {

    public NavigationAdapter(List<NodeBean> data) {
        super(R.layout.project_item_navigation, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NodeBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.addOnClickListener(R.id.iv_del);
    }

}
