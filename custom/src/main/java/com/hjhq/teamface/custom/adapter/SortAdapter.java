package com.hjhq.teamface.custom.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.custom.R;

import java.util.List;

/**
 *
 * @author lx
 * @date 2017/3/28
 */

public class SortAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectedIndex = -1;

    public SortAdapter(List<String> list, int selectedIndex) {
        super(R.layout.custom_item_pop_screen, list);
        this.selectedIndex = selectedIndex;
    }

    public SortAdapter(List<String> list) {
        super(R.layout.custom_item_pop_screen, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_screen_name, item);
        if (helper.getAdapterPosition() == selectedIndex) {
            ((TextView) helper.getView(R.id.tv_screen_name)).setTextColor(ContextCompat.getColor(mContext, R.color.main_green));
        } else {
            ((TextView) helper.getView(R.id.tv_screen_name)).setTextColor(ContextCompat.getColor(mContext, R.color.black_4a));
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
