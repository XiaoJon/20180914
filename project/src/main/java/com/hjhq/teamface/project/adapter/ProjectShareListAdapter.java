package com.hjhq.teamface.project.adapter;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.view.SwipeMenuLayout;
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

public class ProjectShareListAdapter extends BaseQuickAdapter<ProjectShareListBean.DataBean.DataListBean, BaseViewHolder> {
    private String keyword = "";

    public ProjectShareListAdapter(List<ProjectShareListBean.DataBean.DataListBean> data) {
        super(R.layout.project_share_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectShareListBean.DataBean.DataListBean item) {

        ImageView avatar = helper.getView(R.id.iv_conversation_avatar);
        ImageLoader.loadCircleImage(avatar.getContext(), item.getEmployee_pic(), avatar, item.getEmployee_name());
        helper.setIsRecyclable(false);
        helper.setVisible(R.id.iv_my_shared, !SPHelper.getEmployeeId().equals(item.getCreate_by()));
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
        String content = Html.fromHtml(item.getShare_content()).toString();
        helper.setText(R.id.tv_content, content);
        helper.setText(R.id.tv_time, DateTimeUtil.fromTime(TextUtil.parseLong(item.getCreate_time())));
        if ("1".equals(item.getShare_top_status())) {
            helper.setText(R.id.put_on_top, "取消置顶");
        } else {
            helper.setText(R.id.put_on_top, "置顶");
        }
        helper.getView(R.id.put_on_top).setOnClickListener(v -> {
            SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.sml);
            swipeMenuLayout.quickClose();
            EventBusUtils.sendEvent(new MessageBean(helper.getAdapterPosition(), ProjectConstants.PUT_ON_TOP_FLAG, null));
        });


    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
