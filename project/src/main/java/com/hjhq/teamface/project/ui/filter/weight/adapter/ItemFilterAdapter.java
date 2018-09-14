package com.hjhq.teamface.project.ui.filter.weight.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.project.bean.ConditionBean;

import java.util.List;

/**
 *
 * @author lx
 * @date 2017/3/28
 */

public class ItemFilterAdapter extends BaseQuickAdapter<ConditionBean.EntityBean, BaseViewHolder> {

    public ItemFilterAdapter(List<ConditionBean.EntityBean> list) {
        super(R.layout.custom_item_filter, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConditionBean.EntityBean item) {
        helper.setText(R.id.name, item.getName());
        helper.setChecked(R.id.check_null, item.isCheck());
        helper.addOnClickListener(R.id.check_null);
        CheckBox checkBox = helper.getView(R.id.check_null);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> item.setCheck(isChecked));
    }

}
