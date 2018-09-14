package com.hjhq.teamface.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

/**
 * Created by lx on 2017/3/21.
 */

public class WithListenerScrollView extends NestedScrollView {
    public ScrollListener scrollListener;
    private int downX;

    private int downY;

    private int mTouchSlop;
    public WithListenerScrollView(Context context) {
        super(context);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();

    }

    public WithListenerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public WithListenerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

//    @TargetApi(21)
//    public WithListenerScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onScrolling(l, t, oldl, oldt);
        }
    }

    public ScrollListener getScrollListener() {
        return scrollListener;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {
        public void onScrolling(int l, int t, int oldl, int oldt);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY /2);//这里设置滑动的速度
    }

}
