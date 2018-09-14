package com.hjhq.teamface.common.weight.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;

import java.util.List;

/**
 * @author lx
 * @date 2017/3/28
 */

public class MemberFilterAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    public MemberFilterAdapter(List<Member> list) {
        super(R.layout.custom_item_filter, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        helper.setVisible(R.id.photo, true);
        helper.setText(R.id.name, item.getName());
        helper.setChecked(R.id.check_null, item.isCheck());

        CheckBox checkBox = helper.getView(R.id.check_null);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> item.setCheck(isChecked));

        ImageView photoView = helper.getView(R.id.photo);
        ImageLoader.loadCircleImage(mContext, item.getPicture(), photoView, item.getName());
    }

}
