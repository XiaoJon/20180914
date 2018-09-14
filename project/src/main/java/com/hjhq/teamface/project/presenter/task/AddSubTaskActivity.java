package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.ui.member.SelectRangeActivity;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.AddPersonalSubTaskRequestBean;
import com.hjhq.teamface.project.bean.AddSubTaskRequestBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.ui.task.AddSubTaskDelegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;


/**
 * 添加子任务
 * Created by Administrator on 2018/7/16.
 */

public class AddSubTaskActivity extends ActivityPresenter<AddSubTaskDelegate, ProjectModel> {
    private long taskId;
    private long projectId;
    private String checkStatus;
    private String checkMember;
    private String associateStatus;
    private String moduleBean;
    private Calendar calendar = Calendar.getInstance();
    private String projectCustomId;
    private String relationData;
    private String relationId;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0);
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);

            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            checkStatus = intent.getStringExtra(ProjectConstants.CHECK_STATUS);
            checkMember = intent.getStringExtra(ProjectConstants.CHECK_MEMBER);
            associateStatus = intent.getStringExtra(ProjectConstants.ASSOCIATE_STATUS);

            projectCustomId = intent.getStringExtra(ProjectConstants.PROJECT_CUSTOM_ID);
            relationData = intent.getStringExtra(ProjectConstants.RELATION_DATA);
            relationId = intent.getStringExtra(ProjectConstants.RELATION_ID);
        }
    }

    @Override
    public void init() {
        calendar.setTime(new Date());
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, calendar);
            bundle.putString(DateTimeSelectPresenter.FORMAT, DateTimeSelectPresenter.YYYY_MM_DD);
            CommonUtil.startActivtiyForResult(this, DateTimeSelectPresenter.class, Constants.REQUEST_CODE1, bundle);
        }, R.id.ll_deadline);
        viewDelegate.mMemberView.setOnAddMemberClickedListener(() -> {
            if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
                List<Member> members = viewDelegate.mMemberView.getMembers();
                for (Member member : members) {
                    member.setCheck(true);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
                CommonUtil.startActivtiyForResult(mContext, SelectMemberActivity.class, Constants.REQUEST_CODE2, bundle);
            } else {
                model.queryProjectMember(mContext, projectId, new ProgressSubscriber<ProjectMemberResultBean>(mContext) {
                    @Override
                    public void onNext(ProjectMemberResultBean baseBean) {
                        super.onNext(baseBean);
                        List<ProjectMemberResultBean.DataBean.DataListBean> data = baseBean.getData().getDataList();
                        ArrayList<Member> chooseRanger = new ArrayList<>();
                        if (!CollectionUtils.isEmpty(data)) {
                            for (ProjectMemberResultBean.DataBean.DataListBean projectMember : data) {
                                chooseRanger.add(new Member(projectMember.getEmployee_id(), projectMember.getEmployee_name(), C.EMPLOYEE));
                            }
                        }

                        List<Member> members = viewDelegate.mMemberView.getMembers();
                        Observable.from(members).subscribe(member -> member.setCheck(true));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.CHOOSE_RANGE_TAG, chooseRanger);
                        bundle.putSerializable(C.SELECTED_MEMBER_TAG, (Serializable) members);
                        CommonUtil.startActivtiyForResult(mContext, SelectRangeActivity.class, Constants.REQUEST_CODE2, bundle);
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addSubTask();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 新增子任务
     */
    private void addSubTask() {
        String subTitle = viewDelegate.getSubTitle();
        String deadline = viewDelegate.getDeadline();
        long timeInMillis = calendar.getTimeInMillis();
        String executorId = viewDelegate.getExecutorId();
        if (TextUtil.isEmpty(subTitle)) {
            ToastUtils.showError(mContext, "请输入子任务名称");
            return;
        }

        if (TextUtil.isEmpty(deadline)) {
            ToastUtils.showError(mContext, "请选择截止时间");
            return;
        }
        if (TextUtil.isEmpty(executorId)) {
            ToastUtils.showError(mContext, "请选择执行人");
            return;
        }


        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            AddPersonalSubTaskRequestBean bean = new AddPersonalSubTaskRequestBean();
            bean.setName(subTitle);
            bean.setEnd_time(timeInMillis + "");
            bean.setTask_id(taskId + "");
            bean.setExecutor_id(executorId);
            bean.setProject_custom_id(projectCustomId);
            bean.setRelation_data(relationData);
            bean.setRelation_id(relationId);
            model.addPersonalSubTask(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "新增成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            AddSubTaskRequestBean bean = new AddSubTaskRequestBean();
            bean.setName(subTitle);
            bean.setProjectId(projectId);
            bean.setBean(moduleBean);
            bean.setTaskId(taskId);
            bean.setExecutorId(executorId);
            bean.setEndTime(timeInMillis + "");
            bean.setCheckMember(checkMember);
            bean.setCheckStatus(checkStatus);
            bean.setAssociatesStatus(associateStatus);
            model.addSubTask(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "新增成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                this.calendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                viewDelegate.setDeadline(DateTimeUtil.longToStr(calendar.getTimeInMillis(), DateTimeSelectPresenter.YYYY_MM_DD));
            } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
                calendar.setTime(new Date());
                viewDelegate.setDeadline("");
            }
        } else if (requestCode == Constants.REQUEST_CODE2 && resultCode == RESULT_OK) {
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.mMemberView.setMembers(members);
        }
    }
}
