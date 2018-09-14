package com.hjhq.teamface.memo.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.bean.MemoLocationBean;

import java.util.List;


public class MemoLocationAdapter extends BaseQuickAdapter<MemoLocationBean, BaseViewHolder> {
    private int type = 0;
    private boolean isContentChanged = false;

    public MemoLocationAdapter(int type, List<MemoLocationBean> data) {
        super(R.layout.memo_content_location, data);
        this.type = type;

    }


    @Override
    protected void convert(BaseViewHolder helper, MemoLocationBean item) {
        helper.setText(R.id.tv_address, item.getName());
        helper.setText(R.id.tv_address_detail, item.getAddress());
        ImageView delete = helper.getView(R.id.iv_delete);
        if (type == 1) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v -> {
                remove(helper.getAdapterPosition());
                notifyDataSetChanged();
                isContentChanged = true;
            });
        }
    }

    public boolean isContentChanged() {


        return isContentChanged;
    }
}