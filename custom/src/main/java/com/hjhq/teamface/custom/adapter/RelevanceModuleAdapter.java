package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.custom.R;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/12/12
 */

public class RelevanceModuleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RelevanceModuleAdapter(List<String> data) {
        super(R.layout.custom_item_relevance_module, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_module,item);
    }
}
