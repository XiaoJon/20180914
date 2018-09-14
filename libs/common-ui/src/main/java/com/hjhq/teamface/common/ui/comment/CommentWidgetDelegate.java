package com.hjhq.teamface.common.ui.comment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.common.R;

/**
 * 评论视图
 *
 * @author lx
 * @date 2017/9/4
 */

public class CommentWidgetDelegate extends AppDelegate {
    RecyclerView mRecyclerView;
    private CommentView mCommentView;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_comment_layout;
    }

    @Override
    public boolean isToolBar() {
        return false;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mRecyclerView = get(R.id.fragment_move_detail_rv);
        mCommentView = get(R.id.comment_view);
        mCommentView.initActivity(mContext);
        mCommentView.initModule();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setNestedScrollingEnabled(false);
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
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
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

                    mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
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

    public void showMore(boolean isShow) {
        setVisibility(R.id.tv_show_more_comment, !isShow);
    }
}

