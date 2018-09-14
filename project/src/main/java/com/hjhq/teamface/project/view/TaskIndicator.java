package com.hjhq.teamface.project.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.basis.util.device.ScreenUtils;
import com.hjhq.teamface.project.R;

/**
 * 任务导航栏指示器
 *
 * @author Administrator
 * @date 2018/4/13
 */

public class TaskIndicator extends FrameLayout {
    private Context mContext;
    private LinearLayout llIndicator;
    private int textSize = 14;
    private int textColor;
    private int defaultTextColor;
    private OnItemClickListener mOnItemClickListener;
    /**
     * 当前下标
     */
    private int index = 0;
    private HorizontalScrollView mScrollView;
    /**
     * 是否平均分
     */
    private boolean mAdjustMode;

    public TaskIndicator(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TaskIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TaskIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        textColor = ColorUtils.resToColor(mContext, R.color.app_blue);
        defaultTextColor = ColorUtils.resToColor(mContext, R.color.gray_90);
        View inflate = View.inflate(context, R.layout.project_task_indicator, null);
        llIndicator = inflate.findViewById(R.id.ll_indicator);
        mScrollView = inflate.findViewById(R.id.scroll_view);
        addView(inflate);
    }

    public void setIndicators(String[] titles) {
        if (titles == null) {
            return;
        }
        llIndicator.removeAllViews();
        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(mContext);
            textView.setText(titles[i]);
            textView.setTextSize(textSize);
            textView.setTextColor(i == 0 ? textColor : defaultTextColor);
            textView.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams lp1;
            if (this.mAdjustMode) {
                lp1 = new LinearLayout.LayoutParams(0, -1);
                lp1.weight = 1;
            } else {
                lp1 = new LinearLayout.LayoutParams(-2, -1);
            }


            int finalI = i;
            textView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(finalI, textView);
                }
            });

            textView.setPadding(15, 0, 15, 0);
            llIndicator.addView(textView, lp1);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        TextView oldChildAt = (TextView) llIndicator.getChildAt(this.index);
        oldChildAt.setTextColor(defaultTextColor);
        this.index = index;
        TextView newChildAt = (TextView) llIndicator.getChildAt(index);
        newChildAt.setTextColor(textColor);

        int[] location = new int[2];
        newChildAt.getLocationOnScreen(location);
        int x = location[0];
        int width = newChildAt.getWidth();
        int screenWidth = (int) ScreenUtils.getScreenWidth(mContext);
//        LogUtil.d(x + "----" + screenWidth);
        if ((x - width) < 0) {
            mScrollView.smoothScrollBy(x - width, 0);
        } else if ((x + width) > screenWidth) {
            mScrollView.smoothScrollBy(x + width - screenWidth, 0);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int index, View view);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setAdjustMode(boolean is) {
        this.mAdjustMode = is;
    }
}
