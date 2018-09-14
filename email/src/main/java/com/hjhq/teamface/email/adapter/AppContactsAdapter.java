package com.hjhq.teamface.email.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.email.R;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class AppContactsAdapter extends BaseQuickAdapter<ModuleResultBean.DataBean, BaseViewHolder> {

    public AppContactsAdapter(List<ModuleResultBean.DataBean> data) {
        super(R.layout.email_app_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ModuleResultBean.DataBean item) {
        helper.setText(R.id.tv_nav, item.getName());

    }

}

