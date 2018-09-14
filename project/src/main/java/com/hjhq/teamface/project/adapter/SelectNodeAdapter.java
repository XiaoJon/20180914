package com.hjhq.teamface.project.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.NodeBean;

import java.util.List;

/**
 * 选择节点适配器
 *
 * @author Administrator
 * @date 2018/4/16
 */

public class SelectNodeAdapter extends BaseItemDraggableAdapter<NodeBean, BaseViewHolder> {
    public SelectNodeAdapter(List<NodeBean> data) {
        super(R.layout.project_item_select_node, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NodeBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.getView(R.id.iv_select).setSelected(item.isCheck());
    }

}
