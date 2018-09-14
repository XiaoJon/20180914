package com.hjhq.teamface.common.view.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.hjhq.teamface.common.R;

/**
 * Created by Administrator on 2018/8/31.
 * Describe：企信下拉加载头部视图
 */

public class PullHeaderView extends LinearLayout implements IHeaderCallBack {
    LinearLayout llRoot;
    TextView mTextView;
    int hight = 0;

    public PullHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public PullHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public PullHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private void initView(Context context) {
        View mView = LayoutInflater.from(context).inflate(R.layout.chat_view_refresh_header, this);
        llRoot = mView.findViewById(R.id.ll_root);
        mTextView = mView.findViewById(R.id.tv_title);
    }

    @Override
    public void onStateNormal() {

    }

    @Override
    public void onStateReady() {

    }

    @Override
    public void onStateRefreshing() {
        this.mTextView.setText("正在加载");

    }

    @Override
    public void onStateFinish(boolean b) {
        this.mTextView.setText("加载完成");

    }

    @Override
    public void onHeaderMove(double v, int i, int i1) {

    }

    @Override
    public void setRefreshTime(long l) {

    }

    @Override
    public void hide() {
        this.setVisibility(GONE);

    }

    @Override
    public void show() {
        this.setVisibility(VISIBLE);
    }

    @Override
    public int getHeaderHeight() {
        return this.getMeasuredHeight();
    }
}
