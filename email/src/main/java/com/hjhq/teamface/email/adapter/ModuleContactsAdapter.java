package com.hjhq.teamface.email.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.bean.MolduleListBean;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class ModuleContactsAdapter extends BaseQuickAdapter<MolduleListBean.DataBean, BaseViewHolder> {



    public ModuleContactsAdapter(List<MolduleListBean.DataBean> data) {
        super(R.layout.email_app_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MolduleListBean.DataBean item) {
        helper.setText(R.id.tv_nav, item.getChinese_name());
    }
}

