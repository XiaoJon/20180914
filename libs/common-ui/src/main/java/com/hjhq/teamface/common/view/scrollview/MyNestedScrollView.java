package com.hjhq.teamface.common.view.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;


/**
 * @author Zhenhua on 2017/6/2 09:46.
 * @email zhshan@ctrip.com
 */

public class MyNestedScrollView extends NestedScrollView {

    private int scaledTouchSlop;
    private float xDistance, yDistance, xLast, yLast;
    private View fixView;
    private OnFixListener listener;
    private boolean fixed;
    private boolean isNeedScroll = true;


    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getScrollY() >= fixView.getTop()) {
            fix();
        } else {
            dismiss();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            LinearLayout linearLayout = (LinearLayout) getChildAt(0);
            if (linearLayout != null) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    if (linearLayout.getChildAt(i).getTag() != null && linearLayout.getChildAt(i).getTag().equals("fix")) {
                        fixView = linearLayout.getChildAt(i);
                    }
                }
            }
        }
    }


    public void setFixListener(OnFixListener listener) {
        this.listener = listener;
    }

    private void fix() {
        if (listener != null && !fixed) {
            listener.onFix();
            fixed = true;
        }
    }

    private void dismiss() {
        if (listener != null && fixed) {
            listener.onDismiss();
            fixed = false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                if (getScrollY() >= fixView.getTop()) {
//                    requestDisallowInterceptTouchEvent(true);
//                    return false;
//                } else {
//                    requestDisallowInterceptTouchEvent(false);
//                    return super.onInterceptTouchEvent(ev);
//                }

                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                Log.e("SiberiaDante", isNeedScroll + "---xDistance ：" + xDistance + "---yDistance:" + yDistance + "-----scaledTouchSlop:" + scaledTouchSlop);
                return !(xDistance > yDistance || yDistance < scaledTouchSlop) && isNeedScroll;
            case MotionEvent.ACTION_UP:
                float x = ev.getX();
                float y = ev.getY();
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnFixListener {
        void onFix();

        void onDismiss();
    }

    public void setNeedScroll(boolean isNeedScroll) {
        this.isNeedScroll = isNeedScroll;

    }
}
