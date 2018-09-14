package com.hjhq.teamface.attendance.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AttendanceDataListAdapter;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AttendanceDataFragmentDelegate;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDataFragment extends FragmentPresenter<AttendanceDataFragmentDelegate, AttendanceModel> {
    AttendanceDataListAdapter mAdapter;
    int type;
    List<String> dataList = new ArrayList<>();

    static AttendanceDataFragment newInstance(int type) {
        AttendanceDataFragment myFragment = new AttendanceDataFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, type);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        this.type = arguments.getInt(Constants.DATA_TAG1);
        getNetData();
        mAdapter = new AttendanceDataListAdapter(dataList);

    }

    @Override
    protected void init() {
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setItemListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                CommonUtil.startActivtiy(getActivity(), AttendanceNumActivity.class);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        });
        switch (type) {
            case 1:
                //日统计
                viewDelegate.get(R.id.header2).setVisibility(View.GONE);

                break;
            case 2:
                //月统计
                viewDelegate.get(R.id.header2).setVisibility(View.GONE);
                break;
            case 3:
                //我的
                viewDelegate.get(R.id.header2).setVisibility(View.VISIBLE);
                viewDelegate.get(R.id.header1_text2).setOnClickListener(v -> {
                    CommonUtil.startActivtiy(getActivity(), MonthlyDataActivity.class);
                });
                break;
        }

    }


    public void refreshData() {
        //state = Constants.REFRESH_STATE;
        getNetData();

    }


    public void getNetData() {
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
        dataList.add("打卡人数");
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            refreshData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
