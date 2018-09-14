package com.hjhq.teamface.oa.main.adaper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.oa.login.bean.CompanyListBean;

import java.util.List;

/**
 * 选择公司
 *
 * @author lx
 * @date 2017/3/21
 */

public class SelectCompanyAdapter extends BaseQuickAdapter<CompanyListBean.DataBean, BaseViewHolder> {

    public SelectCompanyAdapter(List<CompanyListBean.DataBean> data) {
        super(R.layout.item_select_company, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyListBean.DataBean item) {
        helper.setText(R.id.tv_company_name, item.getCompany_name());
        helper.setVisible(R.id.iv_select, item.isSelect());
    }
}
