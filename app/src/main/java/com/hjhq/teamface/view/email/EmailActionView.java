package com.hjhq.teamface.view.email;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjhq.teamface.R;

/**
 * Created by Administrator on 2017/9/18.
 * Describe：
 */

public class EmailActionView extends RelativeLayout {
    private TextView actionName;
    private ImageView actionIcon;
    private String titleText;
    private int drawableId;
    private RelativeLayout rlLayout;
    private View padding;

    public EmailActionView(Context context, String title, int imageDrawableId) {
        super(context, null);
        this.titleText = title;
        this.drawableId = imageDrawableId;

        View view = LayoutInflater.from(context).inflate(R.layout.email_action_view, null);
        addView(view);
        initView();
    }


    public EmailActionView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public EmailActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView() {
        actionName = (TextView) findViewById(R.id.name);
        actionIcon = (ImageView) findViewById(R.id.icon);
        actionName.setText(titleText + " ");
        actionIcon.setImageResource(drawableId);
        rlLayout = (RelativeLayout) findViewById(R.id.rl_action);
        padding = findViewById(R.id.padding);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        rlLayout.setLayoutParams(param);
        param.weight = 1.0f;
        setLayoutParams(param);

//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlLayout.getLayoutParams();
//        layoutParams.setMargins((getWidth() - rlLayout.getWidth()) / 2, 0, 0, 0);
//        rlLayout.setLayoutParams(layoutParams);
//        padding.getLayoutParams().width = (getWidth() - rlLayout.getWidth()) / 2;
//        requestLayout();
    }

    private void setClickListener(OnClickListener listener) {
        setOnClickListener(listener);
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {


    }

    /**
     * 设置图标
     *
     * @param imageDrawableId
     */
    public void setImageDrawable(int imageDrawableId) {


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        padding.getLayoutParams().width = (getWidth() - rlLayout.getWidth()) / 2;
        super.onLayout(changed, l, t, r, b);
    }
}



