package com.hjhq.teamface.project.ui.task;

import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 设置重复
 * Created by Administrator on 2018/7/17.
 */

public class RepeatTaskDelegate extends AppDelegate {
    public EditText etHzValue;
    private TextView tvWeeklyRepeatDate;
    private TextView tvEndValue;
    private TextView tvHzUnit;
    private TextView tvRepeatDateHint;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_repeat_task;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_set_repeat);
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));

        etHzValue = get(R.id.et_hz_value);
        tvWeeklyRepeatDate = get(R.id.tv_weekly_repeat_date);
        tvEndValue = get(R.id.tv_end_value);
        tvHzUnit = get(R.id.tv_hz_unit);
        tvRepeatDateHint = get(R.id.tv_repeat_date_hint);
    }

    /**
     * 按天设置
     */
    public void setDayRepeatTask() {
        setVisibility(R.id.iv_task_repeat_day, true);
        setVisibility(R.id.iv_task_repeat_week, false);
        setVisibility(R.id.iv_task_repeat_month, false);
        setVisibility(R.id.iv_task_repeat_never, false);

        setVisibility(R.id.ll_hz, true);
        setVisibility(R.id.rl_weekly_repeat_date, false);
        setVisibility(R.id.ll_end, true);
        tvHzUnit.setText("天");
    }

    /**
     * 按周设置
     */
    public void setWeekRepeatTask() {
        setVisibility(R.id.iv_task_repeat_day, false);
        setVisibility(R.id.iv_task_repeat_week, true);
        setVisibility(R.id.iv_task_repeat_month, false);
        setVisibility(R.id.iv_task_repeat_never, false);

        setVisibility(R.id.ll_hz, true);
        setVisibility(R.id.rl_weekly_repeat_date, true);
        setVisibility(R.id.ll_end, true);
        tvHzUnit.setText("周");
        tvRepeatDateHint.setText("每周重复日期");
    }


    /**
     * 按月设置
     */
    public void setMonthRepeatTask() {
        setVisibility(R.id.iv_task_repeat_day, false);
        setVisibility(R.id.iv_task_repeat_week, false);
        setVisibility(R.id.iv_task_repeat_month, true);
        setVisibility(R.id.iv_task_repeat_never, false);

        setVisibility(R.id.ll_hz, true);
        setVisibility(R.id.rl_weekly_repeat_date, true);
        setVisibility(R.id.ll_end, true);
        tvHzUnit.setText("月");
        tvRepeatDateHint.setText("每月重复日期");
    }

    /**
     * 从不重复
     */
    public void setNeverRepeatTask() {
        setVisibility(R.id.iv_task_repeat_day, false);
        setVisibility(R.id.iv_task_repeat_week, false);
        setVisibility(R.id.iv_task_repeat_month, false);
        setVisibility(R.id.iv_task_repeat_never, true);

        setVisibility(R.id.ll_hz, false);
        setVisibility(R.id.rl_weekly_repeat_date, false);
        setVisibility(R.id.ll_end, false);
    }

    /**
     * 设置重复日期
     *
     * @param date
     */
    public void setRepeatTaskDate(String date) {
        TextUtil.setText(tvWeeklyRepeatDate, date);
    }

    public void setEndValue(String text) {
        TextUtil.setText(tvEndValue, text);
    }

    /**
     * 清楚数据
     */
    public void clearEndRepeat() {
        etHzValue.setText("");
        tvEndValue.setText("");
        tvWeeklyRepeatDate.setText("");
    }

    public void setRepeatUnit(String unit) {
        etHzValue.setText(unit);
    }

    public String getRepeatUnit() {
        return etHzValue.getText().toString().trim();
    }
}
