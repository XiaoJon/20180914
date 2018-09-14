package com.hjhq.teamface.common.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.common.R;
import com.hjhq.teamface.common.utils.MemberUtils;

import java.util.List;

/**
 * 员工
 * Created by lx on 2017/5/15.
 */

public class EmployeeAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    public EmployeeAdapter(List<Member> data) {
        super(R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        TextView tvTitle = helper.getView(R.id.title);
        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvSubTitle = helper.getView(R.id.tv_sub_title);
        ImageView avatar = helper.getView(R.id.avatar_in_contacts_iv);

        helper.setVisible(R.id.catalog, false);

        int selectState = item.getSelectState();
        ivSelect.setVisibility(View.VISIBLE);
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
        } else {
            ivSelect.setVisibility(View.GONE);
        }

        tvTitle.setText(item.getName());
        tvSubTitle.setText(item.getPost_name());
        ImageLoader.loadCircleImage(mContext, item.getPicture(), avatar, item.getName());

    }
}
