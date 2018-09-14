package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.custom.R;

import java.util.List;

/**
 * Created by Ked ,the Administrator, on 2017/6/19 14:33 .
 *
 * @name teamFace
 * @class name：com.hjhq.teamface.feature.notice.adapter
 * @class 选择统计类型Adapter
 * @anthor
 * @time 2017/6/19 14:33
 * @change
 * @chang time
 * @class describe
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public SearchHistoryAdapter(List<String> data) {
        super(R.layout.custom_item_crm_search_goods_history_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.name, item);
        helper.addOnClickListener(R.id.delete);
    }
}
