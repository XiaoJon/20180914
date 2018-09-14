package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.project.adapter.MonthDateAdapter;
import com.hjhq.teamface.project.bean.MonthDateBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.SelectMonthRepeatDateDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * 每月重复日期
 * Created by Administrator on 2018/7/17.
 */

public class SelectMonthRepeatDateActivity extends ActivityPresenter<SelectMonthRepeatDateDelegate, TaskModel> {
    List<MonthDateBean> checkDate = new ArrayList<>();
    MonthDateAdapter dateAdapter = null;
    List<MonthDateBean> monthList = new ArrayList<>(31);

    @Override
    public void init() {
        for (int i = 1; i <= 31; i++) {
            monthList.add(new MonthDateBean(i));
        }

        Serializable serializableExtra = getIntent().getSerializableExtra(Constants.DATA_TAG1);
        if (serializableExtra != null) {
            checkDate = (List<MonthDateBean>) serializableExtra;
        }
        for (MonthDateBean checkBean : checkDate) {
            for (MonthDateBean monthDate : monthList) {
                if (checkBean.getDay() == monthDate.getDay()) {
                    monthDate.setCheck(true);
                    break;
                }
            }
        }
        initAdapter();
    }

    private void initAdapter() {
        dateAdapter = new MonthDateAdapter(monthList);
        viewDelegate.setAdapter(dateAdapter);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.mRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                MonthDateBean item = dateAdapter.getItem(position);
                item.setCheck(!item.isCheck());
                dateAdapter.notifyItemChanged(position);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        checkDate.clear();
        Observable.from(monthList).filter(MonthDateBean::isCheck).subscribe(data -> checkDate.add(data));
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_TAG1, (Serializable) checkDate);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
