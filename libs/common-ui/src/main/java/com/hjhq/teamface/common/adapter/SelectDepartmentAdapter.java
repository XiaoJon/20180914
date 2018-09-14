package com.hjhq.teamface.common.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.util.List;


public class SelectDepartmentAdapter extends BaseQuickAdapter<GetDepartmentStructureBean.MemberBean, BaseViewHolder> {

    public SelectDepartmentAdapter(List<GetDepartmentStructureBean.MemberBean> data) {
        super(R.layout.item_organization, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GetDepartmentStructureBean.MemberBean item) {
        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvTitle = helper.getView(R.id.title);
        TextView tvSubTitle = helper.getView(R.id.tv_sub_title);


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

        helper.addOnClickListener(R.id.iv_select);

        tvTitle.setText(item.getName());
        tvSubTitle.setText("(" + item.getCount() + ")");

        if (CollectionUtils.isEmpty(item.getChildList()) && item.getCount() == 0) {
            helper.setVisible(R.id.iv_next, false);
        } else {
            helper.setVisible(R.id.iv_next, false);
        }
    }
}