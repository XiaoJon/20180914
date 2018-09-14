package com.hjhq.teamface.project.ui.task;

import android.view.View;

import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 选择每周重复日期
 * Created by Administrator on 2018/7/17.
 */

public class SelectWeeklyRepeatDateDelegate extends AppDelegate {
    public int[] weeks = {R.id.iv_week1, R.id.iv_week2, R.id.iv_week3, R.id.iv_week4, R.id.iv_week5, R.id.iv_week6, R.id.iv_week7};
    public int[] rlWeeks = {R.id.rl_week1, R.id.rl_week2, R.id.rl_week3, R.id.rl_week4, R.id.rl_week5, R.id.rl_week6, R.id.rl_week7};

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_select_weekly_repeat_date;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_select_repeat_date);
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));

        for (int week : rlWeeks) {
            setOnClickListener((View.OnClickListener) mContext, week);
        }
    }

    public void setWeek(int week) {
        setVisibility(weeks[week], get(weeks[week]).getVisibility() != View.VISIBLE);
    }

}
