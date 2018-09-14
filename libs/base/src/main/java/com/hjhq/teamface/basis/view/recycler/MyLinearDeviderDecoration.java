package com.hjhq.teamface.basis.view.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.util.device.DeviceUtils;


/**
 * Created by Samda on 2017/3/7.
 */
public class MyLinearDeviderDecoration extends RecyclerView.ItemDecoration {
    private int mydevider;
    private Paint dividerPaint;
    private Drawable mDivider;
    private Paint mPaint;
    private int mDividerHeight = 2;
    private int marginStart = 0;
    private boolean horizontal = true;
    private int dividerColorRes = -1;
    private int dividerHeight = -1;
    private Context mContext;

    public MyLinearDeviderDecoration(Context context) {
        mContext = context;
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(R.color.transparent));


        //设置分割线宽度

        mydevider = (int) DeviceUtils.dpToPixel(context, 1);

    }

    /**
     * 设置分割线颜色
     *
     * @param dividerColorRes
     */
    public void setDividerColorRes(int dividerColorRes) {
        this.dividerColorRes = dividerColorRes;
        dividerPaint.setColor(mContext.getResources().getColor(dividerColorRes));
    }

    /**
     * 设置分割线高度
     *
     * @param dp
     */
    public void setDividerHeight(int dp) {
        this.dividerHeight = dp;
        mydevider = (int) DeviceUtils.dpToPixel(mContext, dividerHeight);
    }

    public MyLinearDeviderDecoration(Context context, int color) {
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(color));
        //设置分割线宽度
        mydevider = (int) DeviceUtils.dpToPixel(context, 1);

    }

    public MyLinearDeviderDecoration(Context context, int color, boolean horizontal) {
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(color));
        //设置分割线宽度
        mydevider = (int) DeviceUtils.dpToPixel(context, 1);
        this.horizontal = horizontal;

    }

    public MyLinearDeviderDecoration(Context context, int color, int deviderHeight) {
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(color));
        //设置分割线宽度
        mydevider = deviderHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mydevider;
    }

    public void setMarginStart(int marginStart) {
        this.marginStart = marginStart;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
       /* int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + mydevider;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }*/
        if (horizontal) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    // 绘制水平线
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin + marginStart;
            final int right = child.getRight() + params.rightMargin + mDividerHeight;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    // 绘制垂直线
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin + marginStart;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

}