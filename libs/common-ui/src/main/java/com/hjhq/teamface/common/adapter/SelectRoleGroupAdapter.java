package com.hjhq.teamface.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.common.R;

import java.util.List;

/**
 * 角色组适配器
 */
public class SelectRoleGroupAdapter extends BaseQuickAdapter<RoleGroupResponseBean.DataBean, BaseViewHolder> {

    private int checkType;

    public SelectRoleGroupAdapter(List<RoleGroupResponseBean.DataBean> data, int checkType) {
        super(R.layout.item_organization, data);
        this.checkType = checkType;
    }


    @Override
    protected void convert(BaseViewHolder helper, RoleGroupResponseBean.DataBean item) {
        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvTitle = helper.getView(R.id.title);
        TextView tvSubTitle = helper.getView(R.id.tv_sub_title);


        if (checkType == C.RADIO_SELECT) {
            ivSelect.setVisibility(View.GONE);
        } else {
            helper.addOnClickListener(R.id.iv_select);
            ivSelect.setVisibility(View.VISIBLE);
        }
        if (item.isCheck()) {
            ivSelect.setImageResource(R.drawable.icon_selected);
        } else {
            ivSelect.setImageResource(R.drawable.icon_unselect);
        }

        tvTitle.setText(item.getName());

        List<RoleGroupResponseBean.DataBean.RolesBean> roles = item.getRoles();
        int count = 0;
        if (!CollectionUtils.isEmpty(roles)) {
            count = roles.size();
        }
        tvSubTitle.setText("(" + count + ")");
    }
}