package com.hjhq.teamface.project.presenter.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.TaskRemindResultBean;
import com.hjhq.teamface.project.bean.TaskRemindRequestBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.TaskRemindDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;


/**
 * 任务提醒
 * Created by Administrator on 2018/7/17.
 */

public class TaskRemindActivity extends ActivityPresenter<TaskRemindDelegate, TaskModel> implements View.OnClickListener {
    /**
     * 提醒类型
     */
    private int remindType = 1;
    private Calendar calendar;
    private String deadline;
    private int deadlineUnit = -1;
    private ArrayList<Integer> remindWays = new ArrayList<>();

    /**
     * 任务类型 1 任务 2子任务
     */
    long fromType = 1;
    private long taskId;
    private long projectId;
    /**
     * 提醒id
     */
    private long remindId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0);
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
            fromType = intent.getLongExtra(ProjectConstants.TASK_FROM_TYPE, 1);
        }
    }

    @Override
    public void init() {
        //默认
        defaultRemind();
        getTaskRemindList();
    }

    /**
     * 默认提醒
     */
    private void defaultRemind() {
        setCustomRemind();
        List<Member> members = new ArrayList<>();
        members.add(new Member(TextUtil.parseLong(SPHelper.getEmployeeId()), SPHelper.getUserName(), C.EMPLOYEE, SPHelper.getUserAvatar()));
        viewDelegate.mMemberView.setMembers(members);
        //提醒方式
        remindWays.add(0);
        setRemindWay();
    }

    private void getTaskRemindList() {
        model.getTaskRemindList(mContext, taskId, fromType, new ProgressSubscriber<TaskRemindResultBean>(mContext) {
            @Override
            public void onNext(TaskRemindResultBean taskRemindListResultBean) {
                super.onNext(taskRemindListResultBean);
                List<TaskRemindResultBean.DataBean> remindData = taskRemindListResultBean.getData();
                if (!CollectionUtils.isEmpty(remindData)) {
                    loadRemind(remindData);
                }
            }
        });
    }

    /**
     * 加载提醒
     *
     * @param remindData
     */
    private void loadRemind(List<TaskRemindResultBean.DataBean> remindData) {
        TaskRemindResultBean.DataBean dataBean = remindData.get(0);
        remindId = dataBean.getId();
        remindType = TextUtil.parseInt(dataBean.getRemind_type(), 1);
        if (remindType == 1) {
            setCustomRemind();
            //提醒时间
            long remindTime = TextUtil.parseLong(dataBean.getRemind_time());
            if (remindTime == 0) {
                calendar = null;
                viewDelegate.setRemindTime("");
            } else {
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(remindTime);
                viewDelegate.setRemindTime(DateTimeUtil.longToStr_YYYY_MM_DD_HH_MM(calendar.getTimeInMillis()));
            }
        } else {
            setDeadlineRemind();
            //截止时间
            deadlineUnit = TextUtil.parseInt(dataBean.getRemind_unit(), -1);
            deadline = dataBean.getBefore_deadline();
            setDeadline();
        }

        //提醒人
        List<TaskRemindResultBean.DataBean.ReminderBean> reminders = dataBean.getReminder();
        if (!CollectionUtils.isEmpty(reminders)) {
            List<Member> members = new ArrayList<>();
            Observable.from(reminders).subscribe(reminder -> {
                Member member = new Member(reminder.getEmployee_id(), reminder.getEmployee_name(), C.EMPLOYEE);
                member.setPicture(reminder.getEmployee_pic());
                members.add(member);
            });
            viewDelegate.mMemberView.setMembers(members);
        }

        //提醒方式
        int remindWay = TextUtil.parseInt(dataBean.getRemind_way());
        remindWays.clear();
        remindWays.add(remindWay);
        setRemindWay();
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.rl_custom_remind, R.id.rl_deadline_remind,
                R.id.ll_remind_time, R.id.ll_remind_way, R.id.ll_before_deadline);
        viewDelegate.mMemberView.setOnAddMemberClickedListener(() -> {
            List<Member> members = viewDelegate.mMemberView.getMembers();
            for (Member member : members) {
                member.setCheck(true);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
            CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        int id = v.getId();
        if (id == R.id.rl_custom_remind) {
            setCustomRemind();
        } else if (id == R.id.rl_deadline_remind) {
            setDeadlineRemind();
        } else if (id == R.id.ll_remind_time) {
            bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, calendar == null ? Calendar.getInstance() : calendar);
            bundle.putString(DateTimeSelectPresenter.FORMAT, DateTimeSelectPresenter.YYYY_MM_DD_HH_MM);
            CommonUtil.startActivtiyForResult(this, DateTimeSelectPresenter.class, Constants.REQUEST_CODE2, bundle);
        } else if (id == R.id.ll_remind_way) {
            bundle.putSerializable(Constants.DATA_TAG1, remindWays);
            CommonUtil.startActivtiyForResult(mContext, RemindWayActivity.class, Constants.REQUEST_CODE3, bundle);
        } else if (id == R.id.ll_before_deadline) {
            bundle.putString(Constants.DATA_TAG1, deadline);
            bundle.putInt(Constants.DATA_TAG2, deadlineUnit);
            CommonUtil.startActivtiyForResult(mContext, BeforeDeadlineActivity.class, Constants.REQUEST_CODE4, bundle);
        }
    }

    /**
     * 设置截止时间提醒
     */
    private void setDeadlineRemind() {
//        if (remindType != 2) {
//            clearRemind();
//        }
        remindType = 2;
        viewDelegate.setDeadlineRemind();
    }

    /**
     * 设置自定义提醒
     */
    private void setCustomRemind() {
//        if (remindType != 1) {
//            clearRemind();
//        }
        remindType = 1;
        viewDelegate.setCustomRemind();
    }

    private void clearRemind() {
        remindWays.clear();
        viewDelegate.clearRemind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            List<Member> members = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.mMemberView.setMembers(members);
        } else if (requestCode == Constants.REQUEST_CODE2) {
            if (resultCode == Activity.RESULT_OK) {
                calendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                viewDelegate.setRemindTime(DateTimeUtil.longToStr_YYYY_MM_DD_HH_MM(calendar.getTimeInMillis()));
            } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
                calendar = null;
                viewDelegate.setRemindTime("");
            }
        } else if (requestCode == Constants.REQUEST_CODE3 && resultCode == RESULT_OK) {
            remindWays = (ArrayList<Integer>) data.getSerializableExtra(Constants.DATA_TAG1);
            setRemindWay();
        } else if (requestCode == Constants.REQUEST_CODE4 && resultCode == RESULT_OK) {
            deadline = data.getStringExtra(Constants.DATA_TAG1);
            deadlineUnit = data.getIntExtra(Constants.DATA_TAG2, -1);
            setDeadline();
        }
    }

    /**
     * 设置截止时间
     */
    private void setDeadline() {
        String timeUnit = deadlineUnit == 0 ? getString(R.string.minute) : (deadlineUnit == 1 ? getString(R.string.hour) : getString(R.string.day));
        viewDelegate.setDeadline(deadline, timeUnit);
    }

    /**
     * 设置提醒方式
     */
    private void setRemindWay() {
        StringBuilder sb = new StringBuilder();
        for (int way : remindWays) {
            switch (way) {
                case 0:
                    sb.append("、").append("企信");
                    break;
                case 1:
                    sb.append("、").append("企业微信");
                    break;
                case 2:
                    sb.append("、").append("钉钉");
                    break;
                case 3:
                    sb.append("、").append("邮件");
                    break;
                default:
                    break;
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        viewDelegate.setRemindWay(sb.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (remindId != 0) {
            editRemind();
        } else {
            addRemind();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 编辑提醒
     */
    private void editRemind() {
        if (nullCheck()) {
            return;
        }

        TaskRemindRequestBean bean = new TaskRemindRequestBean();
        bean.setId(remindId);
        if (remindType == 1) {
            bean.setRemind_time(calendar.getTimeInMillis());
        } else if (remindType == 2) {
            bean.setBefore_deadline(deadline);
            bean.setRemind_unit(deadlineUnit);
        }

        StringBuilder sb = new StringBuilder();
        Observable.from(viewDelegate.mMemberView.getMembers()).subscribe(member -> sb.append(",").append(member.getId()));
        sb.deleteCharAt(0);

        bean.setReminder(sb.toString());
        bean.setRemind_way(remindWays.get(0));
        bean.setRemind_type(remindType);
        bean.setFrom_type(fromType);
        bean.setProject_id(projectId);
        bean.setTask_id(taskId);

        model.editTaskRemind(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "编辑成功");
                finish();
            }
        });
    }

    /**
     * 新增提醒
     */
    private void addRemind() {
        if (nullCheck()) {
            return;
        }

        TaskRemindRequestBean bean = new TaskRemindRequestBean();
        if (remindType == 1) {
            bean.setRemind_time(calendar.getTimeInMillis());
        } else if (remindType == 2) {
            bean.setBefore_deadline(deadline);
            bean.setRemind_unit(deadlineUnit);
        }

        StringBuilder sb = new StringBuilder();
        Observable.from(viewDelegate.mMemberView.getMembers()).subscribe(member -> sb.append(",").append(member.getId()));
        sb.deleteCharAt(0);

        bean.setReminder(sb.toString());
        bean.setRemind_way(remindWays.get(0));
        bean.setRemind_type(remindType);
        bean.setFrom_type(fromType);
        bean.setProject_id(projectId);
        bean.setTask_id(taskId);

        model.addTaskRemind(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "新增成功");
                finish();
            }
        });
    }

    /**
     * 空值检查
     */
    private boolean nullCheck() {
        if (projectId == 0) {
            ToastUtils.showError(mContext, "项目Id为NULL");
            return true;
        }
        if (remindType == 1) {
            if (calendar == null || calendar.getTimeInMillis() == 0) {
                ToastUtils.showError(mContext, "请设置提醒时间");
                return true;
            }

        } else if (remindType == 2) {
            if (TextUtil.isEmpty(deadline) || deadlineUnit < 0) {
                ToastUtils.showError(mContext, "请设置截止时间");
                return true;
            }
        } else {
            ToastUtils.showError(mContext, "请选择提醒类型");
            return true;
        }

        if (CollectionUtils.isEmpty(viewDelegate.mMemberView.getMembers())) {
            ToastUtils.showError(mContext, "请选择被提醒人");
            return true;
        }
        if (CollectionUtils.isEmpty(remindWays)) {
            ToastUtils.showError(mContext, "请选择提醒方式");
            return true;
        }

        return false;
    }
}
