package com.hjhq.teamface.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.R;

import java.util.List;


/**
 * 组织架构适配器
 *
 * @author Administrator
 */
public class CompanyOrganizationAdapter extends BaseQuickAdapter<GetDepartmentStructureBean.MemberBean, BaseViewHolder> {
    private int checkType;

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public CompanyOrganizationAdapter(List<GetDepartmentStructureBean.MemberBean> data) {
        super(R.layout.custom_item_organization, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GetDepartmentStructureBean.MemberBean item) {
        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvTitle = helper.getView(R.id.title);
        TextView tvSubTitle = helper.getView(R.id.tv_sub_title);


        if (checkType == C.RADIO_SELECT) {
            ivSelect.setVisibility(View.GONE);
        } else if (checkType == C.MULTIL_SELECT) {
            ivSelect.setVisibility(View.VISIBLE);
            if (item.isCheck()) {
                ivSelect.setImageResource(R.drawable.icon_selected);
            } else {
                ivSelect.setImageResource(R.drawable.icon_unselect);
            }
            helper.addOnClickListener(R.id.iv_select);
        }

        TextUtil.setText(tvTitle, item.getName());
        TextUtil.setText(tvSubTitle, "(" + (item.getParentId() == 0 ? item.getCompany_count() : item.getCount()) + ")");
    }
}