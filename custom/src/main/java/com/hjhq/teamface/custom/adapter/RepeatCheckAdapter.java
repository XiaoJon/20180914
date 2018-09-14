package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.bean.RepeatCheckResponseBean;

import java.util.List;

/**
 * 查重适配器
 * Created by lx on 2017/3/28.
 */

public class RepeatCheckAdapter extends BaseQuickAdapter<RepeatCheckResponseBean.DataBean, BaseViewHolder> {
    public RepeatCheckAdapter(List<RepeatCheckResponseBean.DataBean> list) {
        super(R.layout.custom_repeat_check_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RepeatCheckResponseBean.DataBean item) {
        List<RepeatCheckResponseBean.DataBean.RowBean> row = item.getRow();
        if (!CollectionUtils.isEmpty(row)) {
            RepeatCheckResponseBean.DataBean.RowBean rowBean = row.get(0);
            helper.setText(R.id.tv_name1, rowBean.getLabel());
            helper.setText(R.id.tv_value1, rowBean.getValue());
            if (row.size() > 1) {
                RepeatCheckResponseBean.DataBean.RowBean rowBean2 = row.get(1);
                helper.setText(R.id.tv_name2, rowBean2.getLabel());
                helper.setText(R.id.tv_value2, rowBean2.getValue());
            }
        }
    }
}
