package com.hjhq.teamface.project.ui.task;

import android.widget.TextView;

import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

/**
 * 任务提醒
 * Created by Administrator on 2018/7/17.
 */

public class TaskRemindDelegate extends AppDelegate {
    private TextView tvRemindWay;
    public MembersView mMemberView;
    private TextView tvRemindTime;
    private TextView tvBeforeDeadline;
    private TextView tvBeforeDeadlineUnit;
    private TextView tvHint;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_task_remind;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_remind);
        setRightMenuTexts(R.color.app_blue, mContext.getString(R.string.confirm));

        tvRemindWay = get(R.id.tv_remind_way);
        mMemberView = get(R.id.member_view);
        tvHint = get(R.id.tv_hint);

        tvBeforeDeadline = get(R.id.tv_before_deadline);
        tvBeforeDeadlineUnit = get(R.id.tv_before_deadline_unit);
        tvRemindTime = get(R.id.tv_remind_time);
    }

    /**
     * 自定义提醒
     */
    public void setCustomRemind() {
        setVisibility(R.id.iv_custom_remind, true);
        setVisibility(R.id.iv_deadline_remind, false);
        setVisibility(R.id.tv_hint, true);

        setVisibility(R.id.ll_remind_time, true);
        setVisibility(R.id.ll_be_reminder, true);
        setVisibility(R.id.ll_remind_way, true);
        setVisibility(R.id.ll_before_deadline, false);
    }

    /**
     * 截止时间提醒
     */
    public void setDeadlineRemind() {
        setVisibility(R.id.iv_custom_remind, false);
        setVisibility(R.id.iv_deadline_remind, true);
        setVisibility(R.id.tv_hint, true);

        setVisibility(R.id.ll_remind_time, false);
        setVisibility(R.id.ll_be_reminder, true);
        setVisibility(R.id.ll_remind_way, true);
        setVisibility(R.id.ll_before_deadline, true);
    }

    public void clearRemind() {
        tvBeforeDeadline.setText("");
        tvBeforeDeadlineUnit.setText("");
        tvRemindTime.setText("");
        mMemberView.setMembers(null);
        tvRemindWay.setText("");
    }

    public void setRemindTime(String time) {
        TextUtil.setText(tvRemindTime, time);
    }

    public void setDeadline(String deadline, String timeUnit) {
        TextUtil.setText(tvBeforeDeadline, deadline);
        TextUtil.setText(tvBeforeDeadlineUnit, timeUnit);
    }

    public void setRemindWay(String remindWay) {
        TextUtil.setText(tvRemindWay, remindWay);
    }
}
