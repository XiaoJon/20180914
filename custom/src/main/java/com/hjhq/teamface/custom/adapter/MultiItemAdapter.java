package com.hjhq.teamface.custom.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.bean.TransformationResultBean;

import java.util.List;

/**
 * 多选 Dialog适配器
 * Created by lx on 2017/3/28.
 */

public class MultiItemAdapter extends BaseQuickAdapter<TransformationResultBean.DataBean, BaseViewHolder> {


    public MultiItemAdapter(List<TransformationResultBean.DataBean> list) {
        super(R.layout.custom_item_dialog_multi, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransformationResultBean.DataBean item) {
        helper.setText(R.id.tv_item, item.getTitle());
        helper.addOnClickListener(R.id.check_box);
    }


}
