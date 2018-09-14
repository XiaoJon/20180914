package com.hjhq.teamface.common.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;
import com.hjhq.teamface.common.R;

public class PullFooterView extends LinearLayout implements IFooterCallBack {
    private Context mContext;
    private View mContentView;
    private TextView mHintView;
    private TextView mClickView;
    private boolean showing = true;
    private boolean loadFinish = false;
    private boolean loadBefore = false;

    public PullFooterView(Context context) {
        super(context);
        this.initView(context);
    }

    public PullFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        ViewGroup moreView = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R.layout.refreshview_footer, this);
        moreView.setLayoutParams(new LayoutParams(-1, -2));
        this.mContentView = moreView.findViewById(R.id.xrefreshview_footer_content);
        this.mHintView = (TextView) moreView.findViewById(R.id.xrefreshview_footer_hint_textview);
        this.mClickView = (TextView) moreView.findViewById(R.id.xrefreshview_footer_click_textview);
        this.mHintView.setText("");
    }

    public void callWhenNotAutoLoadMore(final XRefreshView xRefreshView) {
        this.mClickView.setText(R.string.xrefreshview_footer_hint_click);
        this.mClickView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                xRefreshView.notifyLoadMore();
            }
        });
        Log.e("状态", "callWhenNotAutoLoadMore");
    }

    public void onStateReady() {
        this.mHintView.setVisibility(VISIBLE);
        this.mHintView.setText(R.string.xrefreshview_footer_hint_release);
        this.mClickView.setVisibility(GONE);
        Log.e("状态", "onStateReady");
    }

    public void onStateRefreshing() {
        if (!loadBefore) {
            this.mHintView.setText("");
            loadBefore = true;
        } else {
            this.mHintView.setText(R.string.xrefreshview_footer_hint_release);
        }
        if (loadFinish) {
            this.mHintView.setText(R.string.xrefreshview_footer_hint_complete);
        }
        this.mHintView.setVisibility(VISIBLE);
        this.mClickView.setVisibility(GONE);
        this.show(true);
        Log.e("状态", "onStateRefreshing");
    }

    public void onReleaseToLoadMore() {
        show(true);
        this.mHintView.setVisibility(VISIBLE);
        this.mHintView.setText(R.string.xrefreshview_header_hint_loading);
        this.mClickView.setVisibility(GONE);
        Log.e("状态", "onReleaseToLoadMore");
    }

    public void onStateFinish(boolean hideFooter) {
        if (hideFooter) {
            this.mHintView.setText(R.string.xrefreshview_footer_hint_normal);
        } else {
            this.mHintView.setText(R.string.xrefreshview_footer_hint_fail);
        }
        this.mHintView.setVisibility(VISIBLE);
        this.mClickView.setVisibility(GONE);
        Log.e("状态", "onStateFinish");
        show(true);
    }

    public void onStateComplete() {
        show(true);
        this.mHintView.setText(R.string.xrefreshview_footer_hint_complete);
        this.mHintView.setVisibility(VISIBLE);
        this.mClickView.setVisibility(GONE);
        loadFinish = true;
        Log.e("状态", "onStateComplete");
    }

    public void show(boolean show) {
        this.setVisibility(VISIBLE);
       /* this.showing = show;
        if (show) {
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(GONE);
        }*/
    }

    public boolean isShowing() {
        return false;
    }


    public int getFooterHeight() {
        return this.getMeasuredHeight();
    }
}