package com.hjhq.teamface.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.memo.MemoModel;
import com.hjhq.teamface.memo.R;
import com.hjhq.teamface.memo.view.MemoRemindModeDelegate;

import java.util.Calendar;

public class MemoRemindTimeAndModeActivity extends ActivityPresenter<MemoRemindModeDelegate, MemoModel> {
    private String datetimeType;
    private String datetimeType2;
    private String timeStr;
    private Calendar mCalendar;
    private int remindMode;
    private boolean selectedTimeBefore = false;


    @Override
    public void init() {
        initData();
        setListener();
    }

    private void initData() {
        datetimeType = TextUtil.getNonNull("yyyy-MM-dd HH:mm", null);
        datetimeType2 = "yyyy年MM月dd日 HH:mm";
        remindMode = MemoConstant.REMIND_MODE_SELF;
        mCalendar = Calendar.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedTimeBefore = extras.getBoolean(Constants.DATA_TAG3);
            mCalendar = (Calendar) extras.getSerializable(DateTimeSelectPresenter.CALENDAR);
            if (mCalendar == null) {
                mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis() + 10 * 60 * 1000);
                viewDelegate.setTime("");
            }
            if (selectedTimeBefore) {
                viewDelegate.setTime(DateTimeUtil.longToStr(mCalendar.getTimeInMillis(), datetimeType2));
            }
            remindMode = extras.getInt(Constants.DATA_TAG1, MemoConstant.REMIND_MODE_SELF);
            viewDelegate.setMode(remindMode);

        }

    }

    private void setListener() {
        viewDelegate.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, mCalendar);
                bundle.putString(DateTimeSelectPresenter.FORMAT, datetimeType);
                bundle.putLong(Constants.DATA_TAG1, System.currentTimeMillis());
                bundle.putString(Constants.DATA_TAG2, getString(R.string.memo_remind_time_warn));
                CommonUtil.startActivtiyForResult(MemoRemindTimeAndModeActivity.this, DateTimeSelectPresenter.class, Constants.REQUEST_CODE1, bundle);

            }
        });
        viewDelegate.setMode(remindMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (selectedTimeBefore) {
            if(mCalendar.getTimeInMillis()<System.currentTimeMillis()){
                ToastUtils.showToast(mContext, "请选择未来的时间");
            }
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA_TAG1, mCalendar);
            intent.putExtra(Constants.DATA_TAG2, viewDelegate.getMode());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            ToastUtils.showToast(mContext, "请选择时间");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                mCalendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                if (mCalendar != null) {
                    timeStr = DateTimeUtil.longToStr(mCalendar.getTime().getTime(), "yyyy年MM月dd日 HH:mm");
                    viewDelegate.setTime(timeStr);
                    selectedTimeBefore = true;
                }
            }
        } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
            viewDelegate.setTime("");
            mCalendar.setTimeInMillis(System.currentTimeMillis() + 10 * 60 * 1000);
            selectedTimeBefore = false;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
