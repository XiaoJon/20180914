package com.hjhq.teamface.project.ui.add;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.view.TextWebView;
import com.hjhq.teamface.project.R;

/**
 * 新增项目/项目设置
 */

public class ProjectAddShareDelegate extends AppDelegate {

    private EditText mEtTitle;
    private TextView mTvTitle;
    private LinearLayout mLlLike;
    private ImageView mIvLike;
    private TextView mTvLikeNum;
    private TextView mTvLikeState;
    private FrameLayout mFrameLayout;
    RecyclerView mRvShareMember;
    TextWebView mWebView;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_share_activity_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mEtTitle = get(R.id.et_name);
        mWebView = get(R.id.rich_text);
        mTvTitle = get(R.id.tv_name_content);
        mLlLike = get(R.id.ll_like);
        mIvLike = get(R.id.like);
        mTvLikeState = get(R.id.tv_like);
        mTvLikeNum = get(R.id.tv_like_num);
        mFrameLayout = get(R.id.fl_can);
        mFrameLayout.setVisibility(View.GONE);

    }


    public String getTitle() {
        String title = mEtTitle.getText().toString();
        return title;
    }

    public String getContent() {
        return "";

    }

    public void setShareTitle(String share_title) {
        mEtTitle.setText(share_title + "");
        mTvTitle.setText(share_title + "");

    }

    public void setShareContent(String htmlText) {
        mWebView.setWebText(htmlText);
    }

    public void hideEditText() {

        mEtTitle.setVisibility(View.GONE);

        mTvTitle.setVisibility(View.VISIBLE);
    }

    public void showEditText() {

        mEtTitle.setVisibility(View.VISIBLE);
        mTvTitle.setVisibility(View.GONE);
    }

    public void hideLike() {
        mLlLike.setVisibility(View.GONE);
    }

    public void showLike() {
        mLlLike.setVisibility(View.VISIBLE);
    }

    public void setPraiseIcon(boolean equals) {
        if (equals) {
            mIvLike.setImageResource(R.drawable.project_heart_red);
        } else {
            mIvLike.setImageResource(R.drawable.project_heart_gray);
        }
        if (equals) {
            mTvLikeState.setText("已赞");
        } else {
            mTvLikeState.setText("未点赞");

        }

    }

    /**
     * 点赞人数
     *
     * @param num
     */
    public void setPraiseNum(String num) {
        mTvLikeNum.setText(String.format(getActivity().getResources().getString(R.string.project_share_num), num));
    }


    /**
     * @param view
     */
    public void setActionView(View view) {
        mFrameLayout.setVisibility(View.VISIBLE);
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(view);
    }

    public void showActionBtn() {
        mFrameLayout.setVisibility(View.VISIBLE);
    }

    public void hideActionBtn() {
        mFrameLayout.setVisibility(View.GONE);
    }
}
