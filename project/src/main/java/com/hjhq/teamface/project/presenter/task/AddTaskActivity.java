package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.CommonNewResultBean;
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
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.bean.ProjectLabelBean;
import com.hjhq.teamface.common.ui.member.SelectRangeActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.bean.ProjectLabelsListBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.SaveTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.SaveTaskResultBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.ui.task.AddTaskDelegate;
import com.hjhq.teamface.project.widget.utils.ProjectCustomUtil;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 新建任务
 *
 * @author Administrator
 * @date 2018/4/10
 */
public class AddTaskActivity extends ActivityPresenter<AddTaskDelegate, TaskModel> {

    private String moduleBean;
    public long projectId;
    private ArrayList<ProjectLabelBean> projectLables;
    private List<EntryBean> entrys;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            moduleBean = getIntent().getStringExtra(Constants.MODULE_BEAN);
            projectId = getIntent().getLongExtra(ProjectConstants.PROJECT_ID, 0);
        }
    }

    @Override
    public void init() {
        //个人任务
        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            viewDelegate.hideCheckView();
        }
        viewDelegate.setDefaultCheckOne(new Member(TextUtil.parseLong(SPHelper.getEmployeeId()), SPHelper.getUserName(), C.EMPLOYEE));
        getTaskLayout();
    }

    /**
     * 获取任务布局
     */
    private void getTaskLayout() {
        //个人任务
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
                viewDelegate.drawLayout(taskLayoutResultBean.getData().getEnableLayout(), null, CustomConstants.ADD_STATE, ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean));
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
        SaveTaskLayoutRequestBean bean = new SaveTaskLayoutRequestBean();

        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            bean.setBean(ProjectConstants.PERSONAL_TASK_BEAN);
        } else {
            if (viewDelegate.sBtnTaskCheck.isChecked() && CollectionUtils.isEmpty(viewDelegate.memberView.getMembers())) {
                ToastUtils.showError(mContext, "校验人不能为空");
                return;
            }
            bean.setBean(ProjectConstants.PROJECT_TASK_MOBULE_BEAN + projectId);
        }

        JSONObject json = new JSONObject();
        boolean put = ProjectCustomUtil.put(mContext, viewDelegate.listView, json);
        if (!put) {
            return;
        }
        bean.setData(json);

        if (ProjectConstants.PERSONAL_TASK_BEAN.equals(moduleBean)) {
            json.put("participants_only", viewDelegate.getOnlyParticipantStatus());
            model.addPersonalTask(mContext, bean, new ProgressSubscriber<CommonNewResultBean>(mContext) {
                @Override
                public void onNext(CommonNewResultBean baseBean) {
                    super.onNext(baseBean);
                    ToastUtils.showSuccess(mContext, "新增成功");
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, baseBean.getData().getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } else {
            model.saveTaskLayout(mContext, bean, new ProgressSubscriber<SaveTaskResultBean>(mContext) {
                @Override
                public void onNext(SaveTaskResultBean saveTaskResultBean) {
                    super.onNext(saveTaskResultBean);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA_TAG1, saveTaskResultBean.getData().getId());
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
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }


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
