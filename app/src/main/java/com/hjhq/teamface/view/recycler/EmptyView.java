package com.hjhq.teamface.view.recycler;

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

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.image.ImageLoader;

/**
 * Created by Administrator on 2017/9/18.
 * Describe：
 */

public class EmptyView extends RelativeLayout {
    private Context mContext;

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

    private ImageView emptyImage;
    private TextView emptyTitle;
    private TextView newButton;
    private LinearLayout llAction;

    private void initView() {
        emptyImage = (ImageView) findViewById(R.id.empty_iv);
        emptyTitle = (TextView) findViewById(R.id.title_tv);
        newButton = (TextView) findViewById(R.id.new_tv);
        llAction = (LinearLayout) findViewById(R.id.ll_new_project_btn);

    }

    public void setEmptyTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            emptyTitle.setText("什么都木有!!!❀");
        } else {
            emptyTitle.setText(title);
        }

    }

    public void setEmptyImage(@DrawableRes int id) {
        ImageLoader.loadImage(mContext, id, emptyImage);
    }

    public void initAction(String text) {
        llAction.setVisibility(VISIBLE);
        newButton.setText(text);
        llAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onButtonClick();
                }
            }
        });


    }

    public interface OnButtonClickListener {
        void onButtonClick();

    }

    private OnButtonClickListener mListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
    }


}
