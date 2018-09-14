package com.hjhq.teamface.statistic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.statistic.R;
import com.hjhq.teamface.statistic.bean.MenuBean;

import java.util.List;

/**
 * 仪表盘选择列表
 *
 * @author lx
 * @date 2017/3/28
 */

public class SortAdapter extends BaseQuickAdapter<MenuBean, BaseViewHolder> {

    public SortAdapter(int layoutResId, List<MenuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setVisible(R.id.iv_select, item.isCheck());
    }
}

