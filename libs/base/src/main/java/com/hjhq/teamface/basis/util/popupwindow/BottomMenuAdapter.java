package com.hjhq.teamface.basis.util.popupwindow;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;

import java.util.List;

/**
 *
 * @author lx
 * @date 2017/3/28
 */
public class BottomMenuAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectedIndex = -1;

    BottomMenuAdapter(List<String> list, int selectedIndex) {
        super(R.layout.item_pop_text, list);
        this.selectedIndex = selectedIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv, item);
        if (helper.getAdapterPosition() == selectedIndex) {
            ((TextView) helper.getView(R.id.tv)).setTextColor(ContextCompat.getColor(mContext, R.color.main_green));
        } else {
            ((TextView) helper.getView(R.id.tv)).setTextColor(ContextCompat.getColor(mContext, R.color.black_4a));
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
