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
import com.hjhq.teamface.project.bean.PersonalTaskRemindRequestBean;
import com.hjhq.teamface.project.bean.PersonalTaskRemindResultBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.TaskRemindDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;


/**
 * 个人任务提醒
 * Created by Administrator on 2018/7/17.
 */

public class PersonalTaskRemindActivity extends ActivityPresenter<TaskRemindDelegate, TaskModel> implements View.OnClickListener {
    /**
     * 提醒类型
     */
    private int remindType = 1;
    private Calendar calendar;
    private String deadline;
    private int deadlineUnit = -1;
    private ArrayList<Integer> remindWays = new ArrayList<>();

    /**
     * 任务类型 0 任务 1子任务
     */
    long fromType = 0;
    private long taskId;
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
        model.getPersonalTaskRemindList(mContext, taskId, fromType, new ProgressSubscriber<PersonalTaskRemindResultBean>(mContext) {
            @Override
            public void onNext(PersonalTaskRemindResultBean taskRemindListResultBean) {
                super.onNext(taskRemindListResultBean);
                List<PersonalTaskRemindResultBean.DataBean> remindData = taskRemindListResultBean.getData();
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
    private void loadRemind(List<PersonalTaskRemindResultBean.DataBean> remindData) {
        PersonalTaskRemindResultBean.DataBean dataBean = remindData.get(0);
        remindId = dataBean.getId();
        remindType = dataBean.getRemind_type();
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
            deadlineUnit = dataBean.getRemind_unit();
            deadline = dataBean.getBefore_deadline();
            setDeadline();
        }

        //提醒人
        viewDelegate.mMemberView.setMembers(dataBean.getReminder());

        //提醒方式
        remindWays.clear();
        String remindWay = dataBean.getRemind_way();
        String[] split = remindWay.split(",");
        for (int i = 0; i < split.length; i++) {
            int way = TextUtil.parseInt(split[i], -1);
            if (way < 0) {
                continue;
            }
            remindWays.add(way);
        }
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

        PersonalTaskRemindRequestBean bean = new PersonalTaskRemindRequestBean();
        bean.setId(remindId);
        if (remindType == 1) {
            bean.setRemind_time(calendar.getTimeInMillis());
        } else if (remindType == 2) {
            bean.setBefore_deadline(deadline);
            bean.setRemind_unit(deadlineUnit);
        }

        StringBuilder sb = new StringBuilder();
        Observable.from(remindWays).subscribe(way -> sb.append(",").append(way));
        sb.deleteCharAt(0);

        bean.setReminder(viewDelegate.mMemberView.getMembers());
        bean.setRemind_way(sb.toString());
        bean.setRemind_type(remindType);
        bean.setFrom_type(fromType);
        bean.setTask_id(taskId);

        model.editPersonalTaskRemind(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
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

        PersonalTaskRemindRequestBean bean = new PersonalTaskRemindRequestBean();
        if (remindType == 1) {
            bean.setRemind_time(calendar.getTimeInMillis());
        } else if (remindType == 2) {
            bean.setBefore_deadline(deadline);
            bean.setRemind_unit(deadlineUnit);
        }

        StringBuilder sb = new StringBuilder();
        Observable.from(remindWays).subscribe(way -> sb.append(",").append(way));
        sb.deleteCharAt(0);

        bean.setReminder(viewDelegate.mMemberView.getMembers());
        bean.setRemind_way(sb.toString());
        bean.setRemind_type(remindType);
        bean.setFrom_type(fromType);
        bean.setTask_id(taskId);

        model.addPersonalTaskRemind(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
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

        if (CollectionUtils.isEmpty(remindWays)) {
            ToastUtils.showError(mContext, "请选择提醒方式");
            return true;
        }
        if (CollectionUtils.isEmpty(viewDelegate.mMemberView.getMembers())) {
            ToastUtils.showError(mContext, "请选择被提醒人");
            return true;
        }
        return false;
    }
}
