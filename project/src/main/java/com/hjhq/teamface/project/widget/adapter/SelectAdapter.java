package com.hjhq.teamface.project.widget.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 *
 * @author lx
 * @date 2017/5/15
 */

public class SelectAdapter extends BaseQuickAdapter<EntryBean, BaseViewHolder> {

    public SelectAdapter(List<EntryBean> data) {
        super(R.layout.custom_item_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntryBean item) {
        TextView tvTitle = helper.getView(R.id.tv_title);
        ImageView ivSelect = helper.getView(R.id.iv_select);

        String label = item.getLabel();
        ivSelect.setSelected(item.isCheck());
        tvTitle.setText(label);
    }
}
