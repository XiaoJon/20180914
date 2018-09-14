package com.hjhq.teamface.email.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.email.R;
import com.hjhq.teamface.email.bean.EmailAccountListBean;

import java.util.List;

/**
 * @author Administrator
 * @description 邮箱账户适配器
 */
public class EmailAccountAdapter extends BaseQuickAdapter<EmailAccountListBean.DataBean, BaseViewHolder> {


    public EmailAccountAdapter(List<EmailAccountListBean.DataBean> data) {
        super(R.layout.email_account_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, EmailAccountListBean.DataBean item) {
        helper.setText(R.id.account, item.getAccount());
        if (item.isChecked()) {
            helper.setImageResource(R.id.iv_select, R.drawable.icon_selected);
        } else {
            helper.setImageResource(R.id.iv_select, R.drawable.icon_unselect);

        }

    }
}

