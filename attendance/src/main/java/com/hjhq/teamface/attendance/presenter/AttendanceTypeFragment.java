package com.hjhq.teamface.attendance.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AttendanceLocationAdapter;
import com.hjhq.teamface.attendance.adapter.AttendanceWifiAdapter;
import com.hjhq.teamface.attendance.bean.AttendanceTypeBean;
import com.hjhq.teamface.attendance.bean.AttendanceTypeListBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AttendanceDataFragmentDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.FragmentPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class AttendanceTypeFragment extends FragmentPresenter<AttendanceDataFragmentDelegate, AttendanceModel> {
    BaseQuickAdapter mAdapter;
    int type;
    List<AttendanceTypeListBean> dataList = new ArrayList<>();

    static AttendanceTypeFragment newInstance(int type) {
        AttendanceTypeFragment myFragment = new AttendanceTypeFragment();
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
        if (type == 0) {
            mAdapter = new AttendanceLocationAdapter(dataList);
        } else if (type == 1) {
            mAdapter = new AttendanceWifiAdapter(dataList);
        }
    }

    @Override
    protected void init() {
        viewDelegate.get(R.id.header1).setVisibility(View.GONE);
        viewDelegate.get(R.id.header2).setVisibility(View.GONE);
        viewDelegate.setAdapter(mAdapter);
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
        model.getAttendanceTypeList(((ActivityPresenter) getActivity()), type, new ProgressSubscriber<AttendanceTypeBean>(getActivity()) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(AttendanceTypeBean baseBean) {
                super.onNext(baseBean);
                dataList.clear();
                if (baseBean.getData() != null && baseBean.getData().getDataList() != null) {
                    dataList.addAll(baseBean.getData().getDataList());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    protected void bindEvenListener() {
        viewDelegate.setItemClickListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type == 0) {
                    Bundle bundle = new Bundle();
                    AttendanceTypeListBean.LocationBean locationBean = dataList.get(position).getLocation().get(0);
                    bundle.putString(Constants.DATA_TAG1, dataList.get(position).getAddress());
                    bundle.putDouble(Constants.DATA_TAG2, TextUtil.parseDouble(locationBean.getLat()));
                    bundle.putDouble(Constants.DATA_TAG3, TextUtil.parseDouble(locationBean.getLng()));
                    bundle.putString(Constants.DATA_TAG4, dataList.get(position).getName());
                    bundle.putString(Constants.DATA_TAG5, dataList.get(position).getId());
                    bundle.putString(Constants.DATA_TAG6, dataList.get(position).getEffective_range());
                    CommonUtil.startActivtiyForResult(getActivity(), AddLocationActivity.class, Constants.REQUEST_CODE1, bundle);
                }
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    model.delAttendanceType(((ActivityPresenter) getActivity()),
                            dataList.get(position).getId(),
                            new ProgressSubscriber<BaseBean>(getActivity(), true) {
                                @Override
                                public void onCompleted() {
                                    super.onCompleted();
                                    dataList.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }
                            });
                }
                super.onItemChildClick(adapter, view, position);
            }
        });
        super.bindEvenListener();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE1) {
                refreshData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
