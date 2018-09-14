package com.hjhq.teamface.oa.main.adaper;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.BaseActivity;
import com.hjhq.teamface.oa.login.bean.CompanyStructureBean;

import java.util.List;


public class NaviAdapter extends BaseQuickAdapter<CompanyStructureBean.DataBean, BaseViewHolder> {
    int openitemPosition = -1;
    BaseActivity mBaseActivity;


    public NaviAdapter(BaseActivity activity, List<CompanyStructureBean.DataBean> data) {
        super(R.layout.file_navi_bar_item, data);
        this.mBaseActivity = activity;
    }


    @Override
    protected void convert(BaseViewHolder helper, CompanyStructureBean.DataBean item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.iv_nav, false);
        } else {
            helper.setVisible(R.id.iv_nav, true);
        }
        helper.setText(R.id.tv_nav, item.getName());
        if (helper.getAdapterPosition() == getItemCount() - 1) {
            helper.setTextColor(R.id.tv_nav, Color.parseColor("#4A4A4A"));
        } else {
            helper.setTextColor(R.id.tv_nav, Color.parseColor("#3689E9"));

        }


    }


}