package com.hjhq.teamface.im.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Conversation;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.im.R;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class SearchGroupAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {
    private String keyword;

    public SearchGroupAdapter(List<Conversation> data) {
        super(R.layout.item_search_assistant, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        helper.setText(R.id.title, TextUtil.getSpannableString(keyword, item.getTitle()));
        helper.setText(R.id.number, item.getResultNum() + "条相关");
        switch (item.getGroupType()) {
            case 1:
                ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_first_group, helper.getView(R.id.icon));
                break;
            case 2:
                ImageLoader.loadCircleImage(helper.getConvertView().getContext(), R.drawable.icon_normal_group, helper.getView(R.id.icon));
                break;
            default:

                break;
        }
    }

    public void setKeyword(String keyword) {

        this.keyword = keyword;
    }


}


