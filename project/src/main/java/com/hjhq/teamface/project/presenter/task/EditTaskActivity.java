package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.EntryBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.bean.ProjectLabelBean;
import com.hjhq.teamface.common.ui.member.SelectRangeActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.EditTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.ProjectLabelsListBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.QueryTaskCompleteAuthResultBean;
import com.hjhq.teamface.project.bean.SavePersonalTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.AddTaskDelegate;
import com.hjhq.teamface.project.widget.utils.ProjectCustomUtil;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 编辑任务
 *
 * @author Administrator
 * @date 2018/4/10
 */
public class EditTaskActivity extends ActivityPresenter<AddTaskDelegate, TaskModel> {
    private HashMap detailMap;
    private String moduleBean;
    public long projectId;
    private ArrayList<ProjectLabelBean> projectLables;
    private List<EntryBean> entrys;
    private long taskId;

    private String checkStatus;
    private String checkMember;
    private String associateStatus;

    private String projectCustomId;
    private String relationData;
    private String relationId;
    /**
     * 1 项目 主任务 2 项目子任务
     */
    private long taskType;
    private long layoutId;
    private String updateDeadlineAuth;
    private String oldDeadline;


    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            detailMap = (HashMap) intent.getSerializableExtra(Constants.DATA_TAG1);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0);
            taskType = intent.getLongExtra(ProjectConstants.TASK_FROM_TYPE, 1);
            layoutId = intent.getLongExtra(ProjectConstants.LAYOUT_ID, 0);

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
        viewDelegate.setTitle(R.string.project_edit_task);
        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            //个人任务
            viewDelegate.hideCheckView();
        } else {
            if ("1".equals(checkStatus)) {
                viewDelegate.openTaskStatus();
            }
            viewDelegate.setDefaultCheckOne(new Member(TextUtil.parseLong(SPHelper.getEmployeeId()), SPHelper.getUserName(), C.EMPLOYEE));
            oldDeadline = detailMap.get(ProjectConstants.PROJECT_TASK_DEADLINE) + "";
            queryEditDeadlineAuth();
        }
        viewDelegate.setOnlyParticipantStatus("1".equals(associateStatus));
        getTaskLayout();
    }

    /**
     * 修改截止时间权限
     */
    private void queryEditDeadlineAuth() {
        model.queryTaskCompleteAuth(mContext, projectId, taskType, taskId, new ProgressSubscriber<QueryTaskCompleteAuthResultBean>(mContext) {
            @Override
            public void onNext(QueryTaskCompleteAuthResultBean queryTaskAuthResultBean) {
                super.onNext(queryTaskAuthResultBean);
                updateDeadlineAuth = queryTaskAuthResultBean.getData().getProject_time_status();
            }
        });
    }

    /**
     * 获取任务布局
     */
    private void getTaskLayout() {
        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            model.getAllLabel(this, null, 2, new ProgressSubscriber<ProjectLabelsListBean>(this) {
                @Override
                public void onNext(ProjectLabelsListBean projectLabelsListBean) {
                    super.onNext(projectLabelsListBean);
                    projectLables = projectLabelsListBean.getData();
                    fillLable();
                }
            });
        } else {
            model.getProjectLabel(this, projectId, 0, new ProgressSubscriber<ProjectLabelsListBean>(this) {
                @Override
                public void onNext(ProjectLabelsListBean projectLabelsListBean) {
                    super.onNext(projectLabelsListBean);
                    projectLables = projectLabelsListBean.getData();
                    fillLable();
                }
            });
        }
        model.getTaskLayout(this, moduleBean, new ProgressSubscriber<TaskLayoutResultBean>(this) {
            @Override
            public void onNext(TaskLayoutResultBean taskLayoutResultBean) {
                super.onNext(taskLayoutResultBean);

                TaskLayoutResultBean.DataBean.EnableLayoutBean enableLayout = taskLayoutResultBean.getData().getEnableLayout();
                List<CustomBean> rows = enableLayout.getRows();
                if (CollectionUtils.isEmpty(rows)) {
                    return;
                }

                for (CustomBean layoutBean : rows) {
                    if (ProjectConstants.PROJECT_TASK_LABEL.equals(layoutBean.getName())) {
                        entrys = layoutBean.getEntrys();
                        entrys.clear();
                        fillLable();
                    }
                    layoutBean.setModuleBean(moduleBean);
                }
                viewDelegate.drawLayout(taskLayoutResultBean.getData().getEnableLayout(), detailMap, CustomConstants.EDIT_STATE, ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean));
            }
        });
    }

    /**
     * 填充标签
     */
    private void fillLable() {
        if (entrys != null && projectLables != null) {
            if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
                Observable.from(projectLables)
                        .subscribe(lable -> entrys.add(new EntryBean(lable.getId(), lable.getName(), lable.getColour())));
            } else {
                Observable.from(projectLables)
                        .filter(lable -> !CollectionUtils.isEmpty(lable.getChildList()))
                        .flatMap(new Func1<ProjectLabelBean, Observable<ProjectLabelBean>>() {
                            @Override
                            public Observable<ProjectLabelBean> call(ProjectLabelBean projectLabelBean) {
                                return Observable.from(projectLabelBean.getChildList());
                            }
                        })
                        .subscribe(lable -> entrys.add(new EntryBean(lable.getId(), lable.getName(), lable.getColour())));
            }
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.sBtnTaskCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewDelegate.switchTaskCheck(isChecked);
        });
        //查重
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_REPEAT_CHECK_CODE, tag -> UIRouter.getInstance().openUri(mContext, "DDComp://custom/repeatCheck", (Bundle) tag));
        //关联列表
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_REFERENCE_TEMP_CODE, tag -> {
            MessageBean messageBean = (MessageBean) tag;
            UIRouter.getInstance().openUri(mContext, "DDComp://custom/referenceTemp", (Bundle) messageBean.getObject(), messageBean.getCode());
        });

        viewDelegate.memberView.setOnAddMemberClickedListener(() ->
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

                        List<Member> members = viewDelegate.memberView.getMembers();
                        Bundle bundle = new Bundle();
                        bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
                        bundle.putSerializable(C.CHOOSE_RANGE_TAG, chooseRanger);
                        bundle.putSerializable(C.SELECTED_MEMBER_TAG, (Serializable) members);
                        //TODO 需要使用项目成员列表界面
                        CommonUtil.startActivtiyForResult(mContext, SelectRangeActivity.class, Constants.REQUEST_CODE1, bundle);
                    }
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveTaskLayoutData();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存任务布局信息
     */
    private void saveTaskLayoutData() {
        if (!ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            if (viewDelegate.sBtnTaskCheck.isChecked() && CollectionUtils.isEmpty(viewDelegate.memberView.getMembers())) {
                ToastUtils.showError(mContext, "校验人不能为空");
                return;
            }
        }

        JSONObject json = new JSONObject();
        boolean put = ProjectCustomUtil.put(mContext, viewDelegate.listView, json);
        if (!put) {
            return;
        }

        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            //个人任务
            json.put("id", projectCustomId);
            SavePersonalTaskLayoutRequestBean personalBean = new SavePersonalTaskLayoutRequestBean();
            personalBean.setCustomLayout(json);
            personalBean.setBean_name(ProjectConstants.PERSONAL_TASK_BEAN);
            personalBean.setId(taskId);
            personalBean.setParticipants_only(viewDelegate.getOnlyParticipantStatus());
            personalBean.setRelation_id(relationId);
            personalBean.setRelation_data(relationData);
            personalBean.setName(json.getString(ProjectConstants.PROJECT_TASK_NAME));
            model.editPersonalTask(mContext, personalBean, new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onNext(BaseBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "编辑成功");
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            //项目任务
            String newDeadline = json.get(ProjectConstants.PROJECT_TASK_DEADLINE) + "";
            if (!oldDeadline.equals(newDeadline)) {
                if (TextUtil.isEmpty(updateDeadlineAuth)) {
                    ToastUtils.showError(mContext, "未获取到截止时间相关权限");
                } else if ("0".equals(updateDeadlineAuth)) {
                    DialogUtils.getInstance().inputDialog(mContext, "修改原因", null, "必填", viewDelegate.getRootView(), content -> {
                        if (TextUtil.isEmpty(content)) {
                            ToastUtils.showError(mContext, "请输入修改原因");
                            return false;
                        }
                        editTask(json, content);
                        return true;
                    });
                } else {
                    editTask(json, null);
                }
            } else {
                editTask(json, null);
            }
        }

    }

    /**
     * 修改
     *
     * @param json
     */
    private void editTask(final JSONObject json, String remark) {
        EditTaskLayoutRequestBean bean = new EditTaskLayoutRequestBean();
        bean.setBean(ProjectConstants.PROJECT_TASK_MOBULE_BEAN + projectId);
        bean.setId(layoutId);
        bean.setTask_id(taskId);
        bean.setType_status(taskType);
        bean.setData(json);
        model.editTaskLayout(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                Intent intent = new Intent();
                intent.putExtra(Constants.DATA_TAG2, viewDelegate.getTaskCheckStatus());
                intent.putExtra(Constants.DATA_TAG3, viewDelegate.getCheckOneId());
                intent.putExtra(Constants.DATA_TAG4, viewDelegate.getOnlyParticipantStatus());
                for (String key : json.keySet()) {
                    switch (key) {
                        case ProjectConstants.PROJECT_TASK_DEADLINE:
                            intent.putExtra(Constants.DATA_TAG5, json.getString(key));
                            break;
                        case ProjectConstants.PROJECT_TASK_EXECUTOR:
                            intent.putExtra(Constants.DATA_TAG6, json.getString(key));
                            break;
                        case ProjectConstants.PROJECT_TASK_STARTTIME:
                            intent.putExtra(Constants.DATA_TAG7, json.getString(key));
                            break;
                        case ProjectConstants.PROJECT_TASK_NAME:
                            intent.putExtra(Constants.DATA_TAG8, json.getString(key));
                            break;
                    }
                }
                intent.putExtra(Constants.DATA_TAG9, remark);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE1) {
            //检验人员
            List<Member> members = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.memberView.setMembers(members);
        }
    }
}
