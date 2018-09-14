package com.hjhq.teamface.attendance.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.DataFragmentDelegate;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends FragmentPresenter<DataFragmentDelegate, AttendanceModel> {

    List<Fragment> mFragmentList = new ArrayList<>();

    static DataFragment newInstance(String tag) {
        DataFragment myFragment = new DataFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TAG1, tag);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mFragmentList.add(AttendanceDataFragment.newInstance(1));
        mFragmentList.add(AttendanceDataFragment.newInstance(2));
        mFragmentList.add(AttendanceDataFragment.newInstance(3));
    }

    @Override
    protected void init() {
        viewDelegate.setFragment(mFragmentList);
        viewDelegate.initNavigator(mFragmentList);
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
