package com.hjhq.teamface.project.adapter;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.common.view.CircularRingPercentageView;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 * 项目列表适配器
 *
 * @author Administrator
 * @date 2018/4/10
 */

public class ProjectAdapter extends BaseQuickAdapter<ProjectListBean.DataBean.DataListBean, BaseViewHolder> {
    public ProjectAdapter(List<ProjectListBean.DataBean.DataListBean> data) {
        super(R.layout.project_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectListBean.DataBean.DataListBean item) {
        CircularRingPercentageView circularRingPercentageView = helper.getView(R.id.circle_iv);
        TextView tvProgress = helper.getView(R.id.tv_progress);

        int progress = 0;
        if (ProjectConstants.PROJECT_PROGRESS_STATUS_INPUT.equals(item.getProject_progress_status())) {
            //手动填写
            progress = TextUtil.parseInt(item.getProject_progress_content());
        } else {
            //自动计算
            int taskCompleteCount = TextUtil.parseInt(item.getTask_complete_count());
            int taskCount = TextUtil.parseInt(item.getTask_count());
            if (taskCount != 0) {
                progress = (int) (taskCompleteCount * 100.0 / taskCount);
            }
        }

        //进度
        String progressContent = progress + "%";
        circularRingPercentageView.setProgress(progress);
        SpannableString spannableString = new SpannableString(progressContent);
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, progressContent.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(12, true), progressContent.length() - 1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProgress.setText(spannableString);


        //是否收藏
        helper.setText(R.id.tv_name, item.getName());
        ImageLoader.loadImage(mContext,
                "1".equals(item.getStar_level()) ? R.drawable.project_icon_mark_selecter : R.drawable.project_icon_mark_unselecter,
                helper.getView(R.id.iv_star));


        ImageView ivState = helper.getView(R.id.iv_state);
        if ("1".equals(item.getProject_status())) {
            //归档
            ivState.setVisibility(View.VISIBLE);
            ivState.setImageResource(R.drawable.project_icon_finish_state);
        } else if ("1".equals(item.getDeadline_status())) {
            //是否超期
            ivState.setVisibility(View.VISIBLE);
            ivState.setImageResource(R.drawable.project_icon_over_state);
        } else {
            ivState.setVisibility(View.GONE);
        }

        int tempId = item.getTemp_id();
        ImageView ivTemp = helper.getView(R.id.iv_project_temp);
        //模板
        if (tempId < 1) {
            ImageLoader.loadImage(mContext, R.drawable.project_temp_bg_1, ivTemp);
        } else if (tempId > 16) {
            ImageLoader.loadImage(mContext, item.getPic_url(), R.drawable.project_temp_bg_1, ivTemp);
        } else {
            String iconName = "project_temp_bg_" + tempId;
            int drawable = mContext.getResources().getIdentifier(iconName, "drawable", mContext.getPackageName());
            ImageLoader.loadImage(mContext, drawable, ivTemp);
        }
    }
}
