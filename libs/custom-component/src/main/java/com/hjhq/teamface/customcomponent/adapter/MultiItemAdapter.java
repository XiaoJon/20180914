package com.hjhq.teamface.customcomponent.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.customcomponent.R;

import java.util.List;

/**
 * 多选 Dialog适配器
 * Created by lx on 2017/3/28.
 */

public class MultiItemAdapter extends BaseQuickAdapter<EntryBean, BaseViewHolder> {


    public MultiItemAdapter(List<EntryBean> list) {
        super(R.layout.custom_item_multi_select, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntryBean item) {
        helper.setText(R.id.tv_item,item.getLabel());

        helper.getView(R.id.iv_select).setSelected(item.isCheck());
        helper.addOnClickListener(R.id.iv_select);
    }
}
