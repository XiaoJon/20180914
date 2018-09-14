package com.hjhq.teamface.oa.main.adaper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.oa.main.bean.DiyCardStyleBean;

import java.util.List;


public class DIYCardStyleAdapter extends BaseQuickAdapter<DiyCardStyleBean, BaseViewHolder> {


    public DIYCardStyleAdapter(List<DiyCardStyleBean> data) {
        super(R.layout.user_temp_diy_style_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, DiyCardStyleBean item) {
        if (item.isChecked()) {
            helper.getView(R.id.rl_background).setBackgroundResource(R.drawable.card_diy_style_selected);
        } else {
            helper.getView(R.id.rl_background).setBackgroundResource(R.drawable.card_diy_style_unselected);
        }
        helper.setImageResource(R.id.iv_style, item.getRes());
    }
}