package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.custom.R;

import java.util.List;

/**
 * Created by lx on 2017/3/28.
 */

public class PickListAdapter extends BaseQuickAdapter<EntryBean, BaseViewHolder> {

    public PickListAdapter(List<EntryBean> list) {
        super(R.layout.custom_item_pick_list, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntryBean item) {
        helper.setText(R.id.tv, item.getLabel());
        helper.setVisible(R.id.iv_select, item.isCheck());
    }

}
