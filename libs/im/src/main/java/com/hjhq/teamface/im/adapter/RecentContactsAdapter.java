package com.hjhq.teamface.im.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.view.SwipeMenuLayout;
import com.hjhq.teamface.im.ImLogic;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.db.DBManager;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class RecentContactsAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    public RecentContactsAdapter(List<Member> data) {
        super(R.layout.item_recent_contact, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        helper.setText(R.id.title, item.getName());
        if (!TextUtils.isEmpty(item.getPost_name())) {
            helper.setText(R.id.number, item.getPost_name() + " ");
        }
        SwipeMenuLayout sml = helper.getView(R.id.sml);
        sml.setSwipeEnable(false);
        ImageView avatar = helper.getView(R.id.avatar_in_contacts_iv);
        String avatarUrl = item.getPicture();
        String name = item.getEmployee_name();
        List<Member> members = DBManager.getInstance().getMemberBySignId(item.getSign_id());
        if (members != null && members.size() > 0) {
            name = members.get(0).getEmployee_name();
            avatarUrl = members.get(0).getPicture();
        }
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), avatarUrl, avatar, name);
        helper.getView(R.id.delete_item).setOnClickListener(v -> {
            Member member = new Member();
            member.setId(item.getId());
            member.setEmployee_name(item.getEmployee_name());
            ImLogic.getInstance().removeRecentContact(member);
            notifyDataSetChanged();
        });

    }
}

