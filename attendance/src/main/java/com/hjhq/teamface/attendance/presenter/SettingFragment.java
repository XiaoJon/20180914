package com.hjhq.teamface.attendance.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.adapter.AttendanceSettingListAdapter;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.SettingFragmentDelegate;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends FragmentPresenter<SettingFragmentDelegate, AttendanceModel> {

    AttendanceSettingListAdapter mAdapter;
    List<String> dataList = new ArrayList<>();

    static SettingFragment newInstance(String tag) {
        SettingFragment myFragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, tag);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();


    }

    @Override
    protected void init() {
        dataList.add("打卡规则");
        dataList.add("考勤方式");
        dataList.add("班次管理");
        dataList.add("班次详情");
        dataList.add("其他设置");
        dataList.add("关联审批单");
        dataList.add("插件管理");
        mAdapter = new AttendanceSettingListAdapter(dataList);
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setItemListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                switch (position) {
                    case 0:
                        //打卡规则
                        CommonUtil.startActivtiy(getActivity(), AttendanceRulesActivity.class);
                        break;
                    case 1:
                        //考勤方式
                        CommonUtil.startActivtiy(getActivity(), AttendanceTypeActivity.class);
                        break;
                    case 2:
                        //班次管理
                        CommonUtil.startActivtiy(getActivity(), WorkTimeManageActivity.class);
                        break;
                    case 3:
                        //班次详情
                        CommonUtil.startActivtiy(getActivity(), ScheduleDetailActivity.class);
                        break;
                    case 4:
                        //其他设置
                        CommonUtil.startActivtiy(getActivity(), AdditionalSettingsActivity.class);
                        break;
                    case 5:
                        //关联审批单
                        CommonUtil.startActivtiy(getActivity(), AttendanceApprovalActivity.class);
                        break;
                    case 6:
                        //插件管理
                        break;

                }
            }


        });


    }


    public void refreshData() {
        //state = Constants.REFRESH_STATE;
        getNetData();

    }


    public void getNetData() {
        /*currentPageNo = state == Constants.LOAD_STATE ? currentPageNo + 1 : 1;
        pageSize = Constants.PAGESIZE;
        model.getEmailList(((ActivityPresenter) getActivity()), currentPageNo, pageSize, "", boxTag, new ProgressSubscriber<EmailListBean>(getActivity(), isFirstLoad) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isFirstLoad = false;
            }

            @Override
            public void onNext(EmailListBean emailListBean) {
                super.onNext(emailListBean);
                mEmptyView.setEmptyTitle("无数据");
                showData(emailListBean);
                isFirstLoad = false;
            }
        });*/
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
