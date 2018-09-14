package com.hjhq.teamface.basis.listener;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.hjhq.teamface.basis.util.UrlUtil;

public class LinkClickableSpan extends ClickableSpan {

    private Context mContext;

    public LinkClickableSpan(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View widget) {
        UrlUtil.openUrlInDefaultBrower(((Activity) widget.getContext()), toString());
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(0xff507daf);
        ds.setUnderlineText(true);
    }
}