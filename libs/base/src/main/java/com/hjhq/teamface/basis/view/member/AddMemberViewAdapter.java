package com.hjhq.teamface.basis.view.member;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lx on 2017/3/29.
 */

public class AddMemberViewAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    @Override
    public void setNewData(List<Member> data) {
        this.mData = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    public AddMemberViewAdapter(List<Member> data) {
        super(R.layout.item_add_member_view_list, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {

        ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.avatar_iv), item.getEmployee_name());
        helper.setText(R.id.avatar_tv, item.getEmployee_name());
    }
}
