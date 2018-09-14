package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.WorkTimeManageListAdapter;
import com.hjhq.teamface.attendance.bean.AddDateBean;
import com.hjhq.teamface.attendance.bean.WorkTimeListBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.WorkTimeManageDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/work_time_manage", desc = "班次管理")
public class WorkTimeManageActivity extends ActivityPresenter<WorkTimeManageDelegate, AttendanceModel> implements View.OnClickListener {

    WorkTimeManageListAdapter mAdapter;
    List<AddDateBean> dataList = new ArrayList<>();

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        viewDelegate.setTitle(R.string.attendance_work_time_manage_title2);
        viewDelegate.setRightMenuTexts(R.color.app_blue, getString(R.string.add));
        setAdapter();
        getNetData(true);


    }

    private void setAdapter() {
        mAdapter = new WorkTimeManageListAdapter(dataList);
        viewDelegate.setAdapter(mAdapter);

    }

    @Override
    protected void bindEvenListener() {
        viewDelegate.setItemListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                viewSchedule(position);
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_delete) {
                    deleteItem(position);
                }
                super.onItemChildClick(adapter, view, position);
            }
        });

        super.bindEvenListener();
    }

    /**
     * 查看班次
     *
     * @param position
     */
    private void viewSchedule(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, AttendanceConstants.TYPE_EDIT);
        bundle.putString(Constants.DATA_TAG2, dataList.get(position).getId());
        CommonUtil.startActivtiyForResult(mContext, AddWorkScheduleActivity.class, Constants.REQUEST_CODE1, bundle);
    }

    /**
     * 删除班次
     *
     * @param position
     */
    private void deleteItem(int position) {
        model.deleteAttendanceTime(mContext, dataList.get(position).getId(), new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                dataList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //添加班次
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, AttendanceConstants.TYPE_ADD);
        CommonUtil.startActivtiyForResult(mContext, AddWorkScheduleActivity.class, Constants.REQUEST_CODE1, bundle);

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取班次列表
     */
    private void getNetData(boolean flag) {
        model.getAttendanceTimeList(mContext, new ProgressSubscriber<WorkTimeListBean>(mContext, flag) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(WorkTimeListBean workTimeListBean) {
                super.onNext(workTimeListBean);
                dataList.clear();
                dataList.addAll(workTimeListBean.getData().getDataList());
                mAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                getNetData(false);
                break;
            case Constants.REQUEST_CODE2:

                break;
            default:

                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
