package com.hjhq.teamface.common.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.common.R;

/**
 * Created by Administrator on 2017/9/18.
 * Describe：
 */

public class EmptyView extends RelativeLayout {
    private Context mContext;
    private TextView mTvMemo;
    private ImageView emptyImage;
    private TextView emptyTitle;
    private TextView newButton;
    private LinearLayout llAction;

    public EmptyView(Context context) {
        super(context, null);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_empty_view, null);
        addView(view);
        initView();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView() {
        emptyImage = findViewById(R.id.empty_iv);
        mTvMemo = findViewById(R.id.tv_memo);
        emptyTitle = findViewById(R.id.title_tv);
        newButton = findViewById(R.id.new_tv);
        llAction = findViewById(R.id.ll_new_project_btn);

    }

    /**
     * 设置提示语
     *
     * @param title
     */
    public void setEmptyTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            emptyTitle.setText("");
        } else {
            emptyTitle.setText(title);
        }

    }

    public void setEmptySubTitle(String title) {
        mTvMemo.setVisibility(VISIBLE);
        TextUtil.setText(mTvMemo, title);
    }

    /**
     * 显示副标题(备忘录)
     */
    public void showSubTitle() {
        mTvMemo.setVisibility(VISIBLE);
    }

    /**
     * 隐藏副标题(备忘录)
     */
    public void hideSubTitle() {
        mTvMemo.setVisibility(GONE);
    }

    /**
     * 设置图标
     *
     * @param id
     */
    public void setEmptyImage(@DrawableRes int id) {
        ImageLoader.loadImage(mContext, id, emptyImage);
    }

    /**
     * 设置点击事件
     *
     * @param text
     */
    public void initAction(String text) {
        if (!TextUtil.isEmpty(text)) {
            llAction.setVisibility(VISIBLE);
            newButton.setText(text);
            llAction.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onButtonClick();
                }
            });
        } else {
            llAction.setVisibility(GONE);
        }


    }

    public interface OnButtonClickListener {
        void onButtonClick();

    }

    private OnButtonClickListener mListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }


}
