package com.hjhq.teamface.project.adapter;

import android.text.SpannableString;
import android.text.Spanned;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.common.view.RichEditText;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.ProjectShareListBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目分享列表适配器
 * Created by Administrator on 2018/4/10.
 */

public class ProjectShareRelevantAdapter extends BaseQuickAdapter<ProjectShareListBean.DataBean.DataListBean, BaseViewHolder> {
    private String keyword = "";

    public ProjectShareRelevantAdapter(List<ProjectShareListBean.DataBean.DataListBean> data) {
        super(R.layout.project_share_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectShareListBean.DataBean.DataListBean item) {
        helper.setVisible(R.id.iv_my_shared, SPHelper.getEmployeeId().equals(item.getId()));
        SpannableString ss = new SpannableString(item.getShare_title());
        Pattern p = Pattern.compile(keyword);
        Matcher matcher = p.matcher(item.getShare_title());
        while (matcher.find()) {
            String str = matcher.group();
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            ss.setSpan(new RichEditText.TagSpan(str), matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.tv_title, ss);
        SpannableString ss2 = new SpannableString(item.getShare_title());
        Pattern p2 = Pattern.compile(keyword);
        Matcher matcher2 = p2.matcher(item.getShare_title());
        while (matcher.find()) {
            String str = matcher.group();
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            ss2.setSpan(new RichEditText.TagSpan(str), matcherStart, matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.tv_content, ss2);


    }

    public void setKeyword(String keyword) {

        this.keyword = keyword;
    }
}
