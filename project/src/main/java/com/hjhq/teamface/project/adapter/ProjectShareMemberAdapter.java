package com.hjhq.teamface.project.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 * 项目列表适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectShareMemberAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    public ProjectShareMemberAdapter(List<Member> data) {
        super(R.layout.project_share_member_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        helper.setText(R.id.tv_nav, item.getName());
        ImageView avatar = helper.getView(R.id.iv_nav);
        ImageLoader.loadCircleImage(avatar.getContext(), item.getPicture(), avatar, item.getName());
    }
}
