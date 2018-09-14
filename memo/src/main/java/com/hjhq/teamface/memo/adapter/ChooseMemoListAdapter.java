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

import java.util.ArrayList;
import java.util.List;


public class ChooseMemoListAdapter extends BaseQuickAdapter<MemoListItemBean, BaseViewHolder> {

    public ChooseMemoListAdapter(List<MemoListItemBean> data) {
        super(R.layout.memo_list_item_in_choose, data);

    }


    @Override
    protected void convert(BaseViewHolder helper, MemoListItemBean item) {
        helper.setIsRecyclable(false);
        helper.setText(R.id.tv_item_title, item.getTitle());
        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        ImageView image = helper.getView(R.id.memo_pic);
        ImageView avatar = helper.getView(R.id.avatar);


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
        if (item.isChecked()) {
            avatar.setImageResource(R.drawable.state_checked);
        } else {
            avatar.setImageResource(R.drawable.state_uncheck);
        }

        helper.getView(R.id.rl_check).setOnClickListener(v -> {
            item.setChecked(!item.isChecked());
            if (item.isChecked()) {
                avatar.setImageResource(R.drawable.state_checked);
            } else {
                avatar.setImageResource(R.drawable.state_uncheck);
            }

        });

    }

    /**
     * 获取已选择的数据
     *
     * @return
     */
    public ArrayList<MemoListItemBean> getChoosedItem() {
        ArrayList<MemoListItemBean> list = new ArrayList<>();
        List<MemoListItemBean> data = getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                list.add(data.get(i));
            }

        }
        return list;
    }
}