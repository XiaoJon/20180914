package com.hjhq.teamface.oa.approve.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;

import java.util.List;

/**
 * 抄送人
 */
public class ApproveCcAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    public ApproveCcAdapter(List<Member> data) {
        super(R.layout.approve_cc_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        ImageView imageView = helper.getView(R.id.iv_avatar);
        TextView tvPosition = helper.getView(R.id.tv_position);
        TextView tvName = helper.getView(R.id.tv_name);


        TextUtil.setText(tvPosition, item.getPost_name());
        TextUtil.setText(tvName, item.getName());
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), imageView, item.getName());
    }
}