package com.hjhq.teamface.attendance.presenter;

import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AttendanceApprovalAdapter;
import com.hjhq.teamface.attendance.bean.AttendanceDataBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.WorkTimeManageDelegate;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.util.ToastUtils;
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
@RouteNode(path = "/view_approval", desc = "关联审批单")
public class AttendanceApprovalActivity extends ActivityPresenter<WorkTimeManageDelegate, AttendanceModel> implements View.OnClickListener {
    AttendanceApprovalAdapter mAdapter;
    List<AttendanceDataBean> dataList = new ArrayList<>();

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        viewDelegate.setTitle(R.string.attendance_rules_manage_title);
        viewDelegate.setRightMenuTexts(R.color.app_blue, getString(R.string.add));
        dataList.add(new AttendanceDataBean());
        dataList.add(new AttendanceDataBean());
        dataList.add(new AttendanceDataBean());
        dataList.add(new AttendanceDataBean());
        dataList.add(new AttendanceDataBean());
        dataList.add(new AttendanceDataBean());
        dataList.add(new AttendanceDataBean());
        mAdapter = new AttendanceApprovalAdapter(dataList);
        viewDelegate.setAdapter(mAdapter);
        viewDelegate.setItemListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.edit) {
                    ToastUtils.showToast(mContext, "修改" + position);
                } else if (view.getId() == R.id.delete) {
                    ToastUtils.showToast(mContext, "删除" + position);
                }
                super.onItemChildClick(adapter, view, position);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CommonUtil.startActivtiyForResult(mContext, AddApprovalActivity.class, Constants.REQUEST_CODE1);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
    }


    @Override
    public void onClick(View v) {

    }
}
