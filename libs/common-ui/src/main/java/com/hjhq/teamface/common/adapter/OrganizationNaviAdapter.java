package com.hjhq.teamface.common.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.bean.OrganizationNaviData;

import java.util.List;


public class OrganizationNaviAdapter extends BaseQuickAdapter<OrganizationNaviData, BaseViewHolder> {


    public OrganizationNaviAdapter(List<OrganizationNaviData> data) {
        super(R.layout.organization_navi_bar_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrganizationNaviData item) {
        int position = helper.getAdapterPosition();
        if (position == 0) {
            helper.getView(R.id.iv_nav).setVisibility(View.INVISIBLE);
        } else {
            helper.setVisible(R.id.iv_nav, true);
        }
        helper.setText(R.id.tv_nav, item.getOrganizationName());
        if (getItemCount() != 1 && position == getItemCount() - 1) {
            helper.setTextColor(R.id.tv_nav, Color.parseColor("#4A4A4A"));
        } else {
            helper.setTextColor(R.id.tv_nav, Color.parseColor("#3689E9"));
        }
    }
}