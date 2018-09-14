package com.hjhq.teamface.im.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.im.R;

import java.util.List;

/**
 * 群组成员适配器
 * Created by lx on 2017/5/15.
 */

public class GroupMemberAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    public GroupMemberAdapter(List<Member> data) {
        super(R.layout.im_item_group_member, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Member item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.department_position, "" + item.getPost_name());
        ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.avatar), item.getName());
        if (item.isCheck()) {
            helper.setVisible(R.id.choosed, true);
        } else {
            helper.setVisible(R.id.choosed, false);
        }

    }
}
