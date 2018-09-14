package com.hjhq.teamface.basis.util.popupwindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;

import java.util.List;

/**
 * Created by lx on 2017/3/28.
 */

public class MessageMenuItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    MessageMenuItemAdapter(List<String> list) {
        super(R.layout.im_message_menu_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.menu_name, item);
    }
}
