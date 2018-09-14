package com.hjhq.teamface.project.ui.filter.weight;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.project.bean.ConditionBean;

/**
 * Created by lx on 2017/6/20.
 * 基类控件
 */

public abstract class BaseFilterView {

    public int id = this.hashCode();
    protected int index = this.hashCode() % 10000;
    /**
     * 文本类型
     */
    public String type;

    /**
     * 字段名称
     */
    protected String title;
    /**
     * 编辑或详情的内容
     */
    protected String value;
    protected View mView;

    /**
     * 对应的key
     */
    protected String keyName;


    /**
     * 组件配置
     */
    protected ConditionBean bean;
    protected Activity mActivity;

    public BaseFilterView() {
    }

    public BaseFilterView(ConditionBean bean) {
        this.bean = bean;

        this.type = bean.getType();
        this.title = bean.getLabel();
        this.keyName = bean.getField();

    }


    /* 将文本View添加到父布局 */
    public void addView(LinearLayout parent, Activity activity) {
        mActivity = activity;
        int childCount = parent.getChildCount();
        addView(parent, activity, childCount);
    }


    /* 将文本View添加到父布局指定位置 */
    public abstract void addView(LinearLayout parent, Activity activity, int index);

    public void setVisibility(int visibility) {
        if (mView == null) {
            return;
        }
        mView.setVisibility(visibility);
    }

    public boolean getVisibility() {
        return mView.getVisibility() == View.VISIBLE;
    }

    /* 获取对应的view */
    public View getView() {
        return mView;
    }

    /**
     * 将文本内容设置到JSONObject中
     *
     * @param jsonObj JSONObject
     * @return 设置成功为true，失败为false
     */
    public abstract boolean put(JSONObject jsonObj) throws Exception;

    public abstract void reset();

    protected Activity getContext() {
        return (Activity) mView.getContext();
    }


    public void rotateCButton(Activity activity, final View v, float start,
                              float end, int duration, int endDrawable) {
        // TODO Auto-generated method stub

        Object object = v.getTag();

        final Drawable icon;
        RotateAnimation animation;
        if (null == object) {

            icon = activity.getResources().getDrawable(endDrawable);
            v.setTag(0);
            animation = new RotateAnimation(start, end,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            icon = activity.getResources().getDrawable(endDrawable);
            v.setTag(null);
            animation = new RotateAnimation(end, start,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        }

        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                v.setBackground(icon);

            }
        });

        v.startAnimation(animation);

    }
}
