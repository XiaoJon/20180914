package com.hjhq.teamface.oa.friends;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

import com.hjhq.teamface.MyApplication;
import com.hjhq.teamface.basis.util.ColorUtils;
import com.hjhq.teamface.im.R;


public class Clickable extends ClickableSpan implements OnClickListener {
    private final OnClickListener mListener;

    public Clickable(OnClickListener l) {
        // TODO Auto-generated constructor stub
        mListener = l;
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        // TODO Auto-generated method stub
        super.updateDrawState(ds);
        // 设置没有下划线
        ds.setUnderlineText(false);
        // 设置颜色高亮
        ds.setColor(ColorUtils.resToColor(MyApplication.getInstance(), R.color.main_green));
    }
}
