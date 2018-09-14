package com.hjhq.teamface.project.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 * @author lx
 * @date 2017/3/28
 */

public class TaskListPopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectedIndex = -1;

    public TaskListPopAdapter(List<String> list, int selectedIndex) {
        super(R.layout.project_item_task_list_pop, list);
        this.selectedIndex = selectedIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);

        boolean bl = helper.getAdapterPosition() == selectedIndex;
        ((TextView) helper.getView(R.id.tv_name)).setTextColor(ContextCompat.getColor(mContext, bl ? R.color.app_blue : R.color.black_4a));
        helper.setVisible(R.id.iv_select, bl);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
