package com.hjhq.teamface.common.ui.comment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * 评论视图
 *
 * @author lx
 * @date 2017/9/4
 */

public class CommentDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshWidget;
    LinearLayout llSort;
    private CommentView mCommentView;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_comment_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.fragment_move_detail_rv);
        swipeRefreshWidget = get(R.id.swipe_refresh_widget);
        llSort = get(R.id.ll_sort);
        mCommentView = get(R.id.comment_view);
        mCommentView.initActivity(mContext);
        mCommentView.initModule();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshWidget.setColorSchemeResources(R.color.main_green);

    }

    /**
     * 设置数量显示
     */
    public void setSortInfo(int count) {
        TextView sortCount = get(R.id.tv_sort_count);
        if (count == 0) {
            sortCount.setVisibility(View.GONE);
        } else {
            sortCount.setVisibility(View.VISIBLE);
            TextUtil.setText(sortCount, count + "条");
        }
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        View emptyView = mContext.getLayoutInflater().inflate(R.layout.view_workbench_empty, null);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
    }


    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public CommentView getCommentView() {
        return mCommentView;
    }

    public void showSoftInputAndDismissMenu() {
        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mCommentView.invisibleMoreMenu();
        mCommentView.getInputView().requestFocus();
        InputMethodManager mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImm.showSoftInput(mCommentView.getInputView(),
                InputMethodManager.SHOW_FORCED);//强制显示键盘

        mCommentView.setMoreMenuHeight();
    }

    public void dismissSoftInputAndShowMenu() {
        //隐藏软键盘
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mCommentView.showMoreMenu();
        InputMethodManager mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImm.hideSoftInputFromWindow(mCommentView.getInputView().getWindowToken(), 0); //强制隐藏键盘
        mCommentView.setMoreMenuHeight();
    }

    public void onKeyBoardStateChange(int state) {
        switch (state) {
            case CommentView.KEYBOARD_STATE_INIT:
                InputMethodManager mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mImm.isActive();
                if (mCommentView.getMoreMenu().getVisibility() == View.INVISIBLE
                        || mCommentView.getMoreMenu().getVisibility() == View.GONE) {

                    ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCommentView.getMoreMenu().setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加@文字
     *
     * @param mentions
     */
    public void appendMention(String[] mentions) {
        mCommentView.getInputView().appendMention(mentions);
        mCommentView.getInputView().setSelection(mCommentView.getInputView().getText().length());
    }
}

