package com.hjhq.teamface.common.wrapper;

import android.view.View;

/**
 * Created by Administrator on 2018/8/3.
 * Describe：
 */

public class ViewWrapper {
    private View mTarger;


    public ViewWrapper(View targer) {

        mTarger = targer;
    }

    public View getTarger() {

        return mTarger;
    }

    public int getWidth() {
        return mTarger.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarger.getLayoutParams().width = width;
        mTarger.requestLayout();
    }

    public int getHeight() {
        return mTarger.getLayoutParams().height;
    }

    public void setHeight(int height) {
        mTarger.getLayoutParams().height = height;
        mTarger.requestLayout();
    }

}
