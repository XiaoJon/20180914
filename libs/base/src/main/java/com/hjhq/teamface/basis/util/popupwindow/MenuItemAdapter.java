package com.hjhq.teamface.basis.util.popupwindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.bean.ToolMenu;

import java.util.List;

/**
 * 默认更多 popupwindow 适配器
 *
 * @author lx
 * @date 2017/3/28
 */

public class MenuItemAdapter extends BaseQuickAdapter<ToolMenu, BaseViewHolder> {
    MenuItemAdapter(List<ToolMenu> list) {
        super(R.layout.item_pop_menu, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ToolMenu item) {
        helper.setText(R.id.tv_title, item.getTitle());
        Integer icon = item.getIcon();
        if (icon != null && item.getIcon() != null) {
            helper.setImageResource(R.id.iv_icon, item.getIcon());
        }
        /*if (helper.getAdapterPosition() == getData().size() - 1) {
            helper.setVisible(R.id.line, false);
        } else {
            helper.setVisible(R.id.line, true);
        }*/
    }
}
