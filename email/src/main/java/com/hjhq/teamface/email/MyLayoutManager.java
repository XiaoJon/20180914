package com.hjhq.teamface.email;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hjhq.teamface.basis.util.log.LogUtil;

public class MyLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        LogUtil.e("EditText == MyLayoutManager");
        int sumWidth = getWidth();
        LogUtil.e("当前宽度==" + sumWidth);
        int curLineWidth = 0, curLineTop = 0;
        int lastLineMaxHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            ViewGroup.LayoutParams layoutParams1 = view.getLayoutParams();
            LogUtil.e(i + "改变前宽高==" + layoutParams1.width + "---" + layoutParams1.height);

            int width = 0;
            int height = 0;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (i == getItemCount() - 1 && view.findViewById(R.id.et_content).getVisibility() == View.VISIBLE) {
                EditText et = (EditText) view.findViewById(R.id.et_content);

                if (curLineWidth <= sumWidth - 100 && et.getTextSize() * et.getText().length() < 100) {
                    layoutParams.width = sumWidth - curLineWidth;
                    view.setLayoutParams(layoutParams);
                    addView(view);
                    measureChildWithMargins(view, 0, 0);
                    width = getDecoratedMeasuredWidth(view);
                    height = getDecoratedMeasuredHeight(view);
                    LogUtil.e(i + "当前宽度1==" + curLineWidth);
                    curLineWidth = sumWidth;
                    LogUtil.e(i + "当前宽度2==" + curLineWidth);
                } else {
                    layoutParams.width = -1;
                    view.setLayoutParams(layoutParams);
                    addView(view);
                    measureChildWithMargins(view, 0, 0);
                    width = getDecoratedMeasuredWidth(view);
                    height = getDecoratedMeasuredHeight(view);
                    LogUtil.e(i + "当前宽度4==" + sumWidth);
                    curLineWidth = sumWidth + 1;
                    LogUtil.e(i + "当前宽度5==" + sumWidth);
                }
                LogUtil.e(i + "改变后宽高==" + layoutParams.width + "---" + layoutParams.height);

            } else {
                if (i == 0) {
                    layoutParams.width = -2;
                } else {
                    layoutParams.width = -2;
                }

                view.setLayoutParams(layoutParams);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                width = getDecoratedMeasuredWidth(view);
                height = getDecoratedMeasuredHeight(view);
                curLineWidth += width;
            }


            if (curLineWidth <= sumWidth) {//不需要换行  
                layoutDecorated(view, curLineWidth - width, curLineTop, curLineWidth, curLineTop + height);
                //比较当前行多有item的最大高度  
                lastLineMaxHeight = Math.max(lastLineMaxHeight, height);
            } else {//换行  
                curLineWidth = width;
                if (lastLineMaxHeight == 0) {
                    lastLineMaxHeight = height;
                }
                //记录当前行top  
                curLineTop += lastLineMaxHeight;

                layoutDecorated(view, 0, curLineTop, width, curLineTop + height);
                lastLineMaxHeight = height;
            }
        }

    }

}  