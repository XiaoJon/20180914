package com.hjhq.teamface.memo.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.RowDataBean;
import com.hjhq.teamface.common.view.RichEditText;
import com.hjhq.teamface.memo.CustomDataUtil;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.bean.SearchModuleDataBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchDataListAdapter extends BaseQuickAdapter<SearchModuleDataBean.DataBean, BaseViewHolder> {
    private String keyword = "";

    public SearchDataListAdapter(List<SearchModuleDataBean.DataBean> data) {
        super(R.layout.memo_choose_relevance_item, data);


    }


    @Override
    protected void convert(BaseViewHolder helper, SearchModuleDataBean.DataBean item) {
        helper.getView(R.id.iv_nav).setVisibility(View.GONE);
        String title = "";
        List<RowDataBean> itemRow = item.getRow();
        if (itemRow != null && itemRow.size() > 0) {
            title = CustomDataUtil.getTextValue(itemRow.get(0));
        }
        SpannableString ss = new SpannableString(title);
        Pattern p = Pattern.compile(keyword);
        Matcher matcher = p.matcher(title);
        while (matcher.find()) {
            String str = matcher.group();
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            ss.setSpan(new RichEditText.TagSpan(str), matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.tv_nav, ss);
        //helper.setText(R.id.tv_nav, item.getRow().get(0).getStringValue());
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}