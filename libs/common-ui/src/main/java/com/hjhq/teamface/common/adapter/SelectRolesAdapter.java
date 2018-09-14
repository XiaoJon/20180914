package com.hjhq.teamface.common.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.util.List;

/**
 * 角色适配器
 */
public class SelectRolesAdapter extends BaseQuickAdapter<RoleGroupResponseBean.DataBean.RolesBean, BaseViewHolder> {

    public SelectRolesAdapter(List<RoleGroupResponseBean.DataBean.RolesBean> data) {
        super(R.layout.item_organization, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, RoleGroupResponseBean.DataBean.RolesBean item) {
        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvTitle = helper.getView(R.id.title);
        helper.setVisible(R.id.iv_next, false);

        int selectState = item.getSelectState();
        if (MemberUtils.checkState(selectState, C.CAN_NOT_SELECT)) {
            if (item.isCheck()) {
                ivSelect.setImageResource(R.drawable.icon_prohibit_select);
            } else {
                ivSelect.setImageResource(R.drawable.icon_prohibit_unselect);
            }
        } else if (MemberUtils.checkState(selectState, C.FREE_TO_SELECT)) {
            if (item.isCheck()) {
                ivSelect.setImageResource(R.drawable.icon_selected);
            } else {
                ivSelect.setImageResource(R.drawable.icon_unselect);
            }
        }
        tvTitle.setText(item.getName());
    }
}