package com.hjhq.teamface.im.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.im.R;

import java.util.List;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class SearchContactsAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    private String keyword;
    private int maxItemNum = -1;
    private boolean isMultiCheck = false;

    public SearchContactsAdapter(List<Member> data) {
        super(R.layout.item_search_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        helper.setVisible(R.id.iv_select, isMultiCheck);
        if (item.isCheck()) {
            helper.setImageResource(R.id.iv_select, R.drawable.state_checked);
        }else{
            helper.setImageResource(R.id.iv_select, R.drawable.icon_unselect);
        }
        helper.setText(R.id.title, TextUtil.getSpannableString(keyword, item.getName()));
        if (TextUtils.isEmpty(item.getPost_name())) {
            helper.setText(R.id.number, "");
        } else {
            helper.setText(R.id.number, item.getPost_name() + "");
        }

        ImageView avatar = helper.getView(R.id.avatar_in_contacts_iv);
        ImageLoader.loadCircleImage(helper.getConvertView().getContext(), item.getPicture(), avatar, item.getEmployee_name());
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isMultiCheck() {
        return isMultiCheck;
    }

    public void setMultiCheck(boolean multiCheck) {
        isMultiCheck = multiCheck;
    }
}

