package com.hjhq.teamface.basis.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.FolderNaviData;

import java.util.List;


public class FileNaviAdapter extends BaseQuickAdapter<FolderNaviData, BaseViewHolder> {


    public FileNaviAdapter(List<FolderNaviData> data) {
        super(R.layout.file_navi_bar_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FolderNaviData item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.iv_nav, false);
        } else {
            helper.setVisible(R.id.iv_nav, true);
        }
        helper.setText(R.id.tv_nav, item.getFolderName());
        if (helper.getAdapterPosition() == getItemCount() - 1) {
            helper.setTextColor(R.id.tv_nav, Color.parseColor("#4A4A4A"));
        } else {
            helper.setTextColor(R.id.tv_nav, Color.parseColor("#3689E9"));

        }


    }


}