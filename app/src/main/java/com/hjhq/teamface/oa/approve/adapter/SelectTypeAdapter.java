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

public class SelectTypeAdapter extends BaseQuickAdapter<RejectTypeResponseBean.DataBean.RejectTypeBean, BaseViewHolder> {

    public SelectTypeAdapter(List<RejectTypeResponseBean.DataBean.RejectTypeBean> data) {
        super(R.layout.custom_item_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RejectTypeResponseBean.DataBean.RejectTypeBean item) {
        TextView tvTitle = helper.getView(R.id.tv_title);
        ImageView ivSelect = helper.getView(R.id.iv_select);

        String label = item.getLabel();
        ivSelect.setSelected(item.isCheck());
        tvTitle.setText(label);
    }
}
