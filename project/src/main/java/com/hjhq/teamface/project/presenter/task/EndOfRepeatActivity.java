package com.hjhq.teamface.project.presenter.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.EndOfRepeatDelegate;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 结束重复
 * Created by Administrator on 2018/7/17.
 */

public class EndOfRepeatActivity extends ActivityPresenter<EndOfRepeatDelegate, TaskModel> implements View.OnClickListener {
    Calendar calendar;
    /**
     * 结束类型 0 永不 1次数 2日期
     */
    private int endRepeatType;
    /**
     * 结束次数
     */
    private int endRepeatCount;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            endRepeatType = getIntent().getIntExtra(Constants.DATA_TAG1, 0);
            endRepeatCount = getIntent().getIntExtra(Constants.DATA_TAG2, 0);
            Serializable serializableExtra = getIntent().getSerializableExtra(Constants.DATA_TAG3);
            if (serializableExtra != null) {
                calendar = (Calendar) serializableExtra;
            } else if (endRepeatType == 2 && serializableExtra == null) {
                ToastUtils.showError(mContext, "日期异常");
                finish();
            }
        }
    }

    @Override
    public void init() {
        switch (endRepeatType) {
            case 0:
                viewDelegate.setEndByNever();
                break;
            case 1:
                viewDelegate.setEndByCount();
                viewDelegate.setEndCount(endRepeatCount + "");
                break;
            case 2:
                viewDelegate.setEndByDate();
                viewDelegate.setEndDate(DateTimeUtil.longToStr_YYYY_MM_DD(calendar.getTimeInMillis()));
                break;
            default:
                break;
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.rl_never, R.id.rl_count,
                R.id.rl_date, R.id.ll_select_end_date);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_never) {
            endRepeatType = 0;
            viewDelegate.setEndByNever();
        } else if (id == R.id.rl_count) {
            endRepeatType = 1;
            viewDelegate.setEndByCount();
        } else if (id == R.id.rl_date) {
            endRepeatType = 2;
            viewDelegate.setEndByDate();
        } else if (id == R.id.ll_select_end_date) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, calendar == null ? Calendar.getInstance() : calendar);
            bundle.putString(DateTimeSelectPresenter.FORMAT, DateTimeSelectPresenter.YYYY_MM_DD);
            CommonUtil.startActivtiyForResult(this, DateTimeSelectPresenter.class, Constants.REQUEST_CODE1, bundle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        comfirm();
        return super.onOptionsItemSelected(item);
    }

    private void comfirm() {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, endRepeatType);
        switch (endRepeatType) {
            case 0:
                break;
            case 1:
                int endCount = viewDelegate.getEndCount();
                if (endCount <= 0) {
                    ToastUtils.showError(mContext, "请输入大于0的次数");
                    return;
                }
                intent.putExtra(Constants.DATA_TAG2, endCount);
                break;
            case 2:
                if (calendar == null) {
                    ToastUtils.showError(mContext, "请选择结束时间");
                    return;
                }
                intent.putExtra(Constants.DATA_TAG3, calendar);
                break;
            default:
                return;
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1) {
            if (resultCode == Activity.RESULT_OK) {
                calendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                viewDelegate.setEndDate(DateTimeUtil.longToStr_YYYY_MM_DD(calendar.getTimeInMillis()));
            } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
                calendar = null;
                viewDelegate.setEndDate("");
            }
        }
    }
}
