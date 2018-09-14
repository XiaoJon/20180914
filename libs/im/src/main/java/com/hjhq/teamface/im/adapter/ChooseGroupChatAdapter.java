package com.hjhq.teamface.im.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.im.R;
import com.hjhq.teamface.im.bean.GetGroupListBean;

import java.util.List;

/**
 * 群列表适配器
 */

public class ChooseGroupChatAdapter extends BaseQuickAdapter<GetGroupListBean.DataBean, BaseViewHolder> {

    public ChooseGroupChatAdapter(List<GetGroupListBean.DataBean> data) {
        super(R.layout.im_item_choose_group_chat, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, GetGroupListBean.DataBean item) {
        String peoples = item.getPeoples();
        String[] p = peoples.split(",");
        if (p != null && p.length >= 1) {
            helper.setText(R.id.number, String.format(helper.getConvertView().getContext().getString(R.string.department_member_num), p.length + ""));
        }
        helper.setText(R.id.name, item.getName());
        if ("0".equals(item.getType())) {
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(),
                    R.drawable.icon_first_group, helper.getView(R.id.avatar));
        } else {
            ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_normal_group, helper.getView(R.id.avatar));
            /*ImageLoader.loadCircleImage(mContext, item.getName(), helper.getView(R.id.avatar),
                    item.getName());*/
        }


    }
}
