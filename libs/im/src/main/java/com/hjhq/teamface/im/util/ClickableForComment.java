package com.hjhq.teamface.im.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

public class ClickableForComment extends ClickableSpan implements
		OnClickListener {
	private final OnClickListener mListener;

	public ClickableForComment(OnClickListener l) {
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
		ds.setARGB(255, 0, 0, 0);
	}
}
