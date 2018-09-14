package com.hjhq.teamface.bean;

import android.text.TextPaint;

public class TextTag {

	public int paddingLeft;
	public int paddingRight;
	public TextPaint paint;
	public float moreText;
	public float textSize;
	public int width;
	
	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public TextPaint getPaint() {
		return paint;
	}

	public void setPaint(TextPaint paint) {
		this.paint = paint;
	}

	public float getMoreText() {
		return moreText;
	}

	public void setMoreText(float moreText) {
		this.moreText = moreText;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

}
