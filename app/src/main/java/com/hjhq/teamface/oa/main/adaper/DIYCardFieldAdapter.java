package com.hjhq.teamface.oa.main.adaper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.oa.main.bean.DiyCardStyleBean;

import java.util.List;


public class DIYCardFieldAdapter extends BaseQuickAdapter<DiyCardStyleBean, BaseViewHolder> {


    public DIYCardFieldAdapter(List<DiyCardStyleBean> data) {
        super(R.layout.user_temp_diy_field_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, DiyCardStyleBean item) {
        if (item.isChecked()) {
            helper.setVisible(R.id.iv_check, true);
            helper.getView(R.id.rl_background).setBackgroundResource(R.drawable.card_diy_style_selected);
        } else {
            helper.getView(R.id.rl_background).setBackgroundResource(R.drawable.card_diy_style_unselected);
            helper.setVisible(R.id.iv_check, false);
        }
        helper.setText(R.id.tv_name, item.getName());
    }
}