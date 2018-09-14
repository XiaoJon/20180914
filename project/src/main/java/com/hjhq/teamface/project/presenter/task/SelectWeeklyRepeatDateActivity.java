package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.SelectWeeklyRepeatDateDelegate;

/**
 * 每周重复日期
 * Created by Administrator on 2018/7/17.
 */

public class SelectWeeklyRepeatDateActivity extends ActivityPresenter<SelectWeeklyRepeatDateDelegate, TaskModel> implements View.OnClickListener {
    private String weeks;

    @Override
    public void init() {
        weeks = getIntent().getStringExtra(Constants.DATA_TAG1);
        if (weeks == null) {
            weeks = "";
        }
        if (!TextUtil.isEmpty(weeks)) {
            String[] split = weeks.split(",");
            for (String week : split) {
                viewDelegate.setWeek(TextUtil.parseInt(week) - 1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        for (int i = 0; i < viewDelegate.rlWeeks.length; i++) {
            if (id == viewDelegate.rlWeeks[i]) {
                int i1 = i + 1;
                if (!weeks.contains(i1 + "")) {
                    weeks += "," + i1;
                } else {
                    weeks = weeks.replace(i1 + "", "").replace(",,", ",");
                }
                if (weeks.startsWith(",")) {
                    StringBuilder sb = new StringBuilder(weeks);
                    sb.deleteCharAt(0);
                    weeks = sb.toString();
                }
                viewDelegate.setWeek(i);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, weeks);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
