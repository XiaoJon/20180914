package com.hjhq.teamface.im.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.log.LogUtil;
import com.hjhq.teamface.im.R;

import java.util.List;

public class MessageReadAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    int type = 0;

    public MessageReadAdapter(List<Member> data) {
        super(R.layout.im_item_read_member, data);
    }

    public MessageReadAdapter(int type, List<Member> data) {
        super(R.layout.im_item_read_member, data);

        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        LogUtil.e("" + new Gson().toJson(item));
        helper.setText(R.id.tv_read_name, item.getName());
        helper.setText(R.id.tv_read_position, item.getPost_name());
        ImageLoader.loadCircleImage(mContext, item.getPicture(), ((ImageView) helper.getView(R.id.item_read_icon)), item.getName());

    }
}