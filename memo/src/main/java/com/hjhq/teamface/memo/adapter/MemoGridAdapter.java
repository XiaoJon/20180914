package com.hjhq.teamface.memo.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.device.DeviceUtils;
import com.hjhq.teamface.common.view.RoundImageView;
import com.hjhq.teamface.memo.R;

import java.util.List;


public class MemoGridAdapter extends BaseQuickAdapter<MemoListItemBean, BaseViewHolder> {
    private int type;
    private boolean editState = false;
    private boolean isMenuOpen = false;

    public MemoGridAdapter(int type, List<MemoListItemBean> data) {
        super(R.layout.memo_grid_item, data);
        this.type = type;

    }

    @Override
    protected void convert(BaseViewHolder helper, MemoListItemBean item) {

        helper.setVisible(R.id.tv_item_title, false);
        String title = item.getTitle();
        SpannableString ss = new SpannableString(title);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#333333"));
        if (!TextUtils.isEmpty(title)) {
            helper.setVisible(R.id.tv_item_title, true);
            if (item.getTitle().contains("\n")) {
                int i = item.getTitle().indexOf("\n");
                ss.setSpan(fcs, 0, i, 0);
                ss.setSpan(new AbsoluteSizeSpan((int) DeviceUtils.dpToPixel(helper.getConvertView().getContext(), 16)), 0, i, 0);
                helper.setText(R.id.tv_item_title, ss);
            } else {
                ss.setSpan(fcs, 0, title.length(), 0);
                ss.setSpan(new AbsoluteSizeSpan((int) DeviceUtils.dpToPixel(helper.getConvertView().getContext(), 16)), 0, title.length(), 0);
                helper.setText(R.id.tv_item_title, ss);
            }

        } else {
            if (!TextUtils.isEmpty(item.getPic_url())) {
                helper.setVisible(R.id.tv_item_title, false);
            } else {
                helper.setVisible(R.id.tv_item_title, true);
            }

        }

        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        RoundImageView image = helper.getView(R.id.memo_pic);
        image.setBorderRadius((int) DeviceUtils.dpToPixel(image.getContext(), 3));

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
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            ImageLoader.loadImageNoAnimCenterCrop(helper.getConvertView().getContext(), item.getPic_url(), image);
        }


    }

    public void setType(int type) {
        this.type = type;
    }
}