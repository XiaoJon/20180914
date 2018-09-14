package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AttendanceRulesAdapter;
import com.hjhq.teamface.attendance.bean.AddDateBean;
import com.hjhq.teamface.attendance.bean.AttendanceRulesListBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.WorkTimeManageDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
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
@RouteNode(path = "/view_rules", desc = "打卡规则")
public class AttendanceRulesActivity extends ActivityPresenter<WorkTimeManageDelegate, AttendanceModel> implements View.OnClickListener {
    AttendanceRulesAdapter mAttendanceRulesAdapter;
    List<AddDateBean> dataList = new ArrayList<>();

    @Override
    public void init() {
        initView();
        getNetData(true);
    }

    private void initView() {
        viewDelegate.setTitle(R.string.attendance_rules_manage_title);
        viewDelegate.setRightMenuTexts(R.color.app_blue, getString(R.string.add));
        mAttendanceRulesAdapter = new AttendanceRulesAdapter(dataList);
        viewDelegate.setAdapter(mAttendanceRulesAdapter);
        viewDelegate.setItemListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DialogUtils.getInstance().sureOrCancel(mContext, "提示",
                        "修改后的考勤规则支持立即生效与明日生效两种设置，请选择您所需要的生效方式。",
                        viewDelegate.getRootView(), "明日生效", "立即生效", new DialogUtils.OnClickSureOrCancelListener() {
                            @Override
                            public void clickSure() {
                                //立即生效
                            }

                            @Override
                            public void clickCancel() {
                                //明日生效


                            }
                        });

                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                if (view.getId() == R.id.edit_rules) {
                    bundle.putString(Constants.DATA_TAG1, dataList.get(position).getId());
                    bundle.putInt(Constants.DATA_TAG2, AttendanceConstants.TYPE_EDIT);
                    CommonUtil.startActivtiyForResult(mContext, AddRulesActivity.class, Constants.REQUEST_CODE2, bundle);
                } else if (view.getId() == R.id.edit_members) {
                    bundle.putString(Constants.DATA_TAG1, dataList.get(position).getId());
                    bundle.putInt(Constants.DATA_TAG2, AttendanceConstants.TYPE_EDIT);
                    CommonUtil.startActivtiyForResult(mContext, AddGroupActivity.class, Constants.REQUEST_CODE2, bundle);
                } else if (view.getId() == R.id.delete) {
                    deleteRules(position);
                }
                super.onItemChildClick(adapter, view, position);
            }
        });

    }

    /**
     * 获取打卡规则列表
     *
     * @param flag
     */
    public void getNetData(boolean flag) {
        model.getAttendanceRulesList(mContext, new ProgressSubscriber<AttendanceRulesListBean>(mContext, flag) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(AttendanceRulesListBean baseBean) {
                super.onNext(baseBean);
                dataList.clear();
                if (baseBean.getData() != null && baseBean.getData().getDataList() != null) {
                    dataList.addAll(baseBean.getData().getDataList());

                }
                mAttendanceRulesAdapter.notifyDataSetChanged();

            }
        });

    }

    /**
     * 删除打卡规则
     *
     * @param position
     */
    private void deleteRules(int position) {
        DialogUtils.getInstance().sureOrCancel(mContext, "提示",
                "删除后，该考勤组成员的考勤将不会被系统智能标记迟到、早退等行为统计。是否继续？",
                viewDelegate.getRootView(), "确定", "取消", new DialogUtils.OnClickSureOrCancelListener() {
                    @Override
                    public void clickSure() {
                        model.delAttendanceRules(mContext, dataList.get(position).getId(), new ProgressSubscriber<BaseBean>(mContext) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                dataList.remove(position);
                                mAttendanceRulesAdapter.notifyDataSetChanged();

                            }
                        });
                    }

                    @Override
                    public void clickCancel() {



                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG2, AttendanceConstants.TYPE_ADD);
        CommonUtil.startActivtiyForResult(mContext, AddGroupActivity.class, Constants.REQUEST_CODE1, bundle);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindEvenListener() {


        super.bindEvenListener();
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
            default:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
