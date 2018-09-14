package com.hjhq.teamface.basis.view.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

import com.hjhq.teamface.basis.R;
import com.hjhq.teamface.basis.util.device.DeviceUtils;

/**
 * Created by lx on 2017/3/24.
 */

public class MyGridDividerDecoration extends ItemDecoration{
    private int myDividerWidth;
    private Paint dividerPaint;

    public MyGridDividerDecoration(Context context) {
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(R.color.transparent));
        //设置分割线宽度
        myDividerWidth = 1;
    }

    public MyGridDividerDecoration(Context context, int color) {
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(color));
        //设置分割线宽度
        myDividerWidth = (int)DeviceUtils.dpToPixel(context,1);
    }

    public MyGridDividerDecoration(Context context, int color, int deviderHeight) {
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(color));
        //设置分割线宽度
        myDividerWidth = deviderHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = myDividerWidth;
        outRect.right = myDividerWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + myDividerWidth;
            if (childCount%2==0){
                right = right + myDividerWidth;
            }
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
    
}
