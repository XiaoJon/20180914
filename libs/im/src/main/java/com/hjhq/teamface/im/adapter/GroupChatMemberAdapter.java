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

public class GroupChatMemberAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    boolean isCreator;

    public GroupChatMemberAdapter(boolean isCreator, List<Member> data) {
        super(R.layout.item_group, data);
        this.isCreator = isCreator;
    }

    @Override
    protected void convert(final BaseViewHolder helper, Member item) {
        // helper.setIsRecyclable(false);

        if (isCreator) {

        }
        if (item.getId() == -1L) {
            helper.setVisible(R.id.grid_name, false);
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_add_group_member, helper.getView(R.id.grid_avatar));
        } else if (item.getId() == -2L) {
            helper.setVisible(R.id.grid_name, false);
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_remove_member, helper.getView(R.id.grid_avatar));

        } else {
            helper.setVisible(R.id.grid_name, true);
            helper.setText(R.id.grid_name, item.getName());
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), helper.getView(R.id.grid_avatar), item.getName());
        }


    }
}
