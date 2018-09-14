package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.MonthDateBean;
import com.hjhq.teamface.project.bean.TaskRepeatRequestBean;
import com.hjhq.teamface.project.bean.TaskRepeatResultBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.RepeatTaskDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import rx.Observable;

/**
 * 重复任务
 * Created by Administrator on 2018/7/17.
 */

public class RepeatTaskActivity extends ActivityPresenter<RepeatTaskDelegate, TaskModel> implements View.OnClickListener {
    private static final int[] WEEK_DAY = {R.string.project_week1, R.string.project_week2, R.string.project_week3, R.string.project_week4, R.string.project_week5, R.string.project_week6, R.string.project_week7};
    /**
     * 每周重复日期
     */
    private String weeks;
    private List<MonthDateBean> monthDateList = new ArrayList<>();
    /**
     * 结束类型 0 永不 1次数 2日期
     */
    private int endRepeatType = -1;
    /**
     * 结束次数
     */
    private int endRepeatCount;
    /**
     * 结束日期
     */
    private Calendar endRepeatDate;
    /**
     * 0:天 1:周2:月 3:从不重复
     */
    private int repeatType = -1;
    private long taskId;
    /**
     * 重复id
     */
    private long repeatId;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            taskId = getIntent().getLongExtra(ProjectConstants.TASK_ID, 0);
        }
    }

    @Override
    public void init() {
        getTaskRepeatList();
    }

    private void getTaskRepeatList() {
        model.getTaskRepeatList(mContext, taskId, new ProgressSubscriber<TaskRepeatResultBean>(mContext) {
            @Override
            public void onNext(TaskRepeatResultBean taskRepeatResultBean) {
                super.onNext(taskRepeatResultBean);
                List<TaskRepeatResultBean.DataBean> repeatData = taskRepeatResultBean.getData();
                if (!CollectionUtils.isEmpty(repeatData)) {
                    loadRepeat(repeatData);
                }
            }
        });
    }

    /**
     * 加载重复设置
     *
     * @param repeatData
     */
    private void loadRepeat(List<TaskRepeatResultBean.DataBean> repeatData) {
        TaskRepeatResultBean.DataBean dataBean = repeatData.get(0);
        repeatId = dataBean.getId();

        repeatType = dataBean.getRepeat_type();
        switch (repeatType) {
            case 0:
                //按天
                setDayRepeatTask();
                break;
            case 1:
                //按周
                setWeekRepeatTask();
                break;
            case 2:
                //按月
                setMonthRepeatTask();
                break;
            case 3:
                //从不重复
                setNeverRepeatTask();
                break;
        }

        if (repeatType == 0 || repeatType == 1 || repeatType == 2) {
            //频率
            String repeatUnit = dataBean.getRepeat_unit();
            viewDelegate.setRepeatUnit(repeatUnit);

            //结束
            endRepeatType = dataBean.getEnd_way();
            endRepeatCount = dataBean.getEnd_of_times();
            long endTime = TextUtil.parseLong(dataBean.getEnd_time());
            if (endTime == 0) {
                endRepeatDate = null;
            } else {
                endRepeatDate = Calendar.getInstance();
                endRepeatDate.setTimeInMillis(endTime);
            }
            setEndTime();

            //每周 每月 重复日期
            String frequencyUnit = dataBean.getFrequency_unit();
            if (!TextUtil.isEmpty(frequencyUnit)) {
                if (repeatType == 1) {
                    weeks = frequencyUnit;
                    setWeekRepeatDate();
                } else if (repeatType == 1) {
                    String[] split = frequencyUnit.split(",");
                    for (String aSplit : split) {
                        MonthDateBean monthDateBean = new MonthDateBean();
                        int day = TextUtil.parseInt(aSplit);
                        if (day == 0) {
                            continue;
                        }
                        monthDateBean.setDay(day);
                        monthDateList.add(monthDateBean);
                    }
                    setMonthRepeatDate();
                }
            }
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.rl_task_repeat_day, R.id.rl_task_repeat_week,
                R.id.rl_task_repeat_month, R.id.rl_task_repeat_never, R.id.rl_weekly_repeat_date, R.id.ll_end);

        viewDelegate.etHzValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                viewDelegate.setVisibility(R.id.tv_hz_unit, !TextUtil.isEmpty(s1));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        int id = v.getId();
        if (id == R.id.rl_task_repeat_day) {
            setDayRepeatTask();
        } else if (id == R.id.rl_task_repeat_week) {
            setWeekRepeatTask();
        } else if (id == R.id.rl_task_repeat_month) {
            setMonthRepeatTask();
        } else if (id == R.id.rl_task_repeat_never) {
            setNeverRepeatTask();
        } else if (id == R.id.rl_weekly_repeat_date) {
            if (viewDelegate.get(R.id.iv_task_repeat_week).getVisibility() == View.VISIBLE) {
                bundle.putString(Constants.DATA_TAG1, weeks);
                CommonUtil.startActivtiyForResult(mContext, SelectWeeklyRepeatDateActivity.class, Constants.REQUEST_CODE1, bundle);
            } else {
                bundle.putSerializable(Constants.DATA_TAG1, (Serializable) monthDateList);
                CommonUtil.startActivtiyForResult(mContext, SelectMonthRepeatDateActivity.class, Constants.REQUEST_CODE2, bundle);
            }
        } else if (id == R.id.ll_end) {
            bundle.putInt(Constants.DATA_TAG1, endRepeatType);
            bundle.putInt(Constants.DATA_TAG2, endRepeatCount);
            bundle.putSerializable(Constants.DATA_TAG3, endRepeatDate);
            CommonUtil.startActivtiyForResult(mContext, EndOfRepeatActivity.class, Constants.REQUEST_CODE3, bundle);
        }
    }

    /**
     * 从不重复
     */

    private void setNeverRepeatTask() {
        if (repeatType != 3) {
            claerEndRepeat();
        }
        repeatType = 3;
        viewDelegate.setNeverRepeatTask();
    }

    /**
     * 按月重复
     */
    private void setMonthRepeatTask() {
        if (repeatType != 2) {
            claerEndRepeat();
        }
        repeatType = 2;
        viewDelegate.setMonthRepeatTask();
    }

    /**
     * 按周重复
     */
    private void setWeekRepeatTask() {
        if (repeatType != 1) {
            claerEndRepeat();
        }
        repeatType = 1;
        viewDelegate.setWeekRepeatTask();
    }

    /**
     * 按天重复
     */
    private void setDayRepeatTask() {
        if (repeatType != 0) {
            claerEndRepeat();
        }
        repeatType = 0;
        viewDelegate.setDayRepeatTask();
    }

    private void claerEndRepeat() {
        endRepeatType = -1;
        endRepeatCount = 0;
        endRepeatDate = null;
        viewDelegate.clearEndRepeat();
        weeks = "";
        monthDateList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK) {
            //每周重复日期
            weeks = data.getStringExtra(Constants.DATA_TAG1);
            setWeekRepeatDate();
        } else if (requestCode == Constants.REQUEST_CODE2 && resultCode == RESULT_OK) {
            //每月重复日期
            monthDateList = (List<MonthDateBean>) data.getSerializableExtra(Constants.DATA_TAG1);
            setMonthRepeatDate();
        } else if (requestCode == Constants.REQUEST_CODE3 && resultCode == RESULT_OK) {
            endRepeatType = data.getIntExtra(Constants.DATA_TAG1, 0);
            endRepeatCount = data.getIntExtra(Constants.DATA_TAG2, 0);
            endRepeatDate = (Calendar) data.getSerializableExtra(Constants.DATA_TAG3);
            setEndTime();
        }
    }

    /**
     * 设置结束时间
     */
    private void setEndTime() {
        switch (endRepeatType) {
            case 0:
                viewDelegate.setEndValue("永不");
                break;
            case 1:
                viewDelegate.setEndValue(endRepeatCount + "次");
                break;
            case 2:
                viewDelegate.setEndValue(DateTimeUtil.longToStr_YYYY_MM_DD(endRepeatDate.getTimeInMillis()));
                break;
            default:
                break;
        }
    }

    /**
     * 设置每月重复日期
     */
    private void setMonthRepeatDate() {
        StringBuilder sb = new StringBuilder();
        for (MonthDateBean date : monthDateList) {
            sb.append("、").append(date.getDay());
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        viewDelegate.setRepeatTaskDate(sb.toString());
    }

    /**
     * 设置每周重复日期
     */
    private void setWeekRepeatDate() {
        String[] split = weeks.split(",");
        List<Integer> list = new ArrayList<>(split.length);
        StringBuilder sb = new StringBuilder();

        for (String week : split) {
            int weekDay = TextUtil.parseInt(week);
            list.add(weekDay);
        }
        Collections.sort(list);
        for (int week : list) {
            sb.append("、").append(getString(WEEK_DAY[week - 1]));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        viewDelegate.setRepeatTaskDate(sb.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (repeatId == 0) {
            addRepeat();
        } else {
            editRepeat();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 新增重复
     */
    private void addRepeat() {
        TaskRepeatRequestBean bean = new TaskRepeatRequestBean();
        if (nullCheck(bean)) {
            return;
        }
        model.addTaskRepeat(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "新增成功");
                finish();
            }
        });
    }


    /**
     * 编辑重复
     */
    private void editRepeat() {
        TaskRepeatRequestBean bean = new TaskRepeatRequestBean();
        bean.setId(repeatId);
        if (nullCheck(bean)) {
            return;
        }
        model.editTaskRepeat(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "编辑成功");
                finish();
            }
        });
    }

    /**
     * 空值检查
     */
    private boolean nullCheck(TaskRepeatRequestBean bean) {
        bean.setRepeat_type(repeatType);
        bean.setTask_id(taskId);
        switch (repeatType) {
            case 0:
                break;
            case 1:
                if (TextUtil.isEmpty(weeks)) {
                    ToastUtils.showError(mContext, "请设置每周重复日期");
                    return true;
                }
                bean.setFrequency_unit(weeks);
                break;
            case 2:
                if (CollectionUtils.isEmpty(monthDateList)) {
                    ToastUtils.showError(mContext, "请设置每月重复日期");
                    return true;
                }
                StringBuilder sb = new StringBuilder();
                Observable.from(monthDateList).subscribe(monthDateBean -> sb.append(",").append(monthDateBean.getDay()));
                sb.deleteCharAt(0);
                bean.setFrequency_unit(sb.toString());
                break;
            case 3:
                return false;
            default:
                ToastUtils.showError(mContext, "请选择重复类型");
                return true;
        }

        //频率
        String repeatUnit = viewDelegate.getRepeatUnit();
        if (TextUtil.isEmpty(repeatUnit)) {
            ToastUtils.showError(mContext, "请设置频率");
            return true;
        }
        bean.setRepeat_unit(repeatUnit);

        //结束
        switch (endRepeatType) {
            case 0:
                break;
            case 1:
                if (endRepeatCount <= 0) {
                    ToastUtils.showError(mContext, "结束次数不能小于0");
                    return true;
                }
                bean.setEnd_of_times(endRepeatCount);
                break;
            case 2:
                if (endRepeatDate == null || endRepeatDate.getTimeInMillis() == 0) {
                    ToastUtils.showError(mContext, "请选择结束日期");
                    return true;
                }
                bean.setEnd_time(endRepeatDate.getTimeInMillis());
                break;
            default:
                ToastUtils.showError(mContext, "请设置结束方式");
                return true;
        }
        bean.setEnd_way(endRepeatType);
        return false;
    }
}
