package com.hjhq.teamface.oa.approve.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean;

import java.util.List;

/**
 * Created by lx on 2017/5/15.
 */

public class SelectTaskAdapter extends BaseQuickAdapter<RejectTypeResponseBean.DataBean.HistoricTaskListBean, BaseViewHolder> {

    public SelectTaskAdapter(List<RejectTypeResponseBean.DataBean.HistoricTaskListBean> data) {
        super(R.layout.custom_item_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RejectTypeResponseBean.DataBean.HistoricTaskListBean item) {
        TextView tvTitle = helper.getView(R.id.tv_title);
        ImageView ivSelect = helper.getView(R.id.iv_select);

        String label = item.getTaskName();
        ivSelect.setSelected(item.isCheck());
        tvTitle.setText(label);
    }
}
