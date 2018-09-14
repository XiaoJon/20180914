package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.bean.SeasPoolResponseBean;

import java.util.List;

/**
 * 动态适配器
 *
 * @author lx
 * @date 2017/4/10
 */

public class SeasPoolAdapter extends BaseQuickAdapter<SeasPoolResponseBean.DataBean, BaseViewHolder> {

    public SeasPoolAdapter(List<SeasPoolResponseBean.DataBean> data) {
        super(R.layout.custom_seas_pool_item, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, SeasPoolResponseBean.DataBean item) {
        helper.setText(R.id.tv_title, item.getTitle());

        helper.setImageResource(R.id.iv_select, item.isCheck() ? R.drawable.icon_selected : R.drawable.icon_unselect);
    }
}
