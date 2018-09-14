package com.hjhq.teamface.basis.view.member;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;

import java.util.List;

/**
 * 人员控件适配器
 *
 * @author lx
 * @date 2017/3/29
 */

public class HorizontalAddMemberAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    public HorizontalAddMemberAdapter(List<Member> data) {
        super(R.layout.item_horizontal_member_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        TextView avatarTv = helper.getView(R.id.avatar_tv);
        TextUtil.setText(avatarTv, item.getName());
        ImageLoader.loadCircleImage(mContext, item.getPicture(), helper.getView(R.id.avatar_iv), item.getName());
    }


}
