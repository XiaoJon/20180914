package com.hjhq.teamface.memo.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.basis.bean.MemoListItemBean;

import java.util.List;


public class SearchMemoListAdapter extends BaseQuickAdapter<MemoListItemBean, BaseViewHolder> {
    private int type;
    private boolean editState = false;

    public SearchMemoListAdapter(int type, int layoutRes, List<MemoListItemBean> data) {
        super(layoutRes, data);
        this.type = type;

    }


    @Override
    protected void convert(BaseViewHolder helper, MemoListItemBean item) {
        helper.setIsRecyclable(true);
        helper.setText(R.id.tv_item_title, item.getTitle());
        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        ImageView image = helper.getView(R.id.memo_pic);
        ImageView avatar = helper.getView(R.id.avatar);
        if (item.getCreateObj() != null) {
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(),
                    item.getCreateObj().getPicture(),
                    avatar,
                    item.getCreateObj().getEmployee_name());
        } else {
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(),
                    "",
                    avatar,
                    "(:");
        }

        if ("0".equals(item.getRemind_time()) || TextUtils.isEmpty(item.getRemind_time())) {
            helper.setVisible(R.id.iv_remind, false);
        } else {
            helper.setVisible(R.id.iv_remind, true);
        }
        if (TextUtils.isEmpty(item.getShare_ids())) {
            helper.setVisible(R.id.iv_share, false);
        } else {
            helper.setVisible(R.id.iv_share, true);
        }
        if (TextUtils.isEmpty(item.getPic_url())) {
            image.setVisibility(View.INVISIBLE);
        } else {
            image.setVisibility(View.VISIBLE);
            ImageLoader.loadImage(helper.getConvertView().getContext(), item.getPic_url(), image);
        }

    }



}