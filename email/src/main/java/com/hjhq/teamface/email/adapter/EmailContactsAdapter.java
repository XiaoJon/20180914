package com.hjhq.teamface.email.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.common.view.RichEditText;
import com.hjhq.teamface.email.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @description 最近联系人适配器
 */
public class EmailContactsAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {

    String keyword = "";

    public EmailContactsAdapter(List<Member> data) {
        super(R.layout.email_contact_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        helper.setText(R.id.title, item.getName());
        SpannableString ss = new SpannableString(item.getEmail());
        Pattern p = Pattern.compile(keyword);
        Matcher matcher = p.matcher(item.getEmail());
        while (matcher.find()) {
            String str = matcher.group();
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            ss.setSpan(new RichEditText.TagSpan(str), matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.number, ss);

        ImageView avatar = helper.getView(R.id.avatar_in_contacts_iv);
        if (TextUtils.isEmpty(item.getEmployee_name())) {
            ImageLoader.loadHoleImage(helper.getConvertView().getContext(), item.getPicture(), avatar, item.getEmail());
        } else {
            ImageLoader.loadHoleImage(helper.getConvertView().getContext(), item.getPicture(), avatar, item.getEmployee_name());
        }

        if (item.isCheck()) {
            ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.icon_selected, helper.getView(R.id.iv_select));
        } else {
            ImageLoader.loadImage(helper.getConvertView().getContext(), R.drawable.icon_unselect, helper.getView(R.id.iv_select));
        }
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

