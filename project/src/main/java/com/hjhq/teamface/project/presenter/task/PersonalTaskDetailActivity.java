package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.ApproveListBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.MemoListItemBean;
import com.hjhq.teamface.basis.bean.MessageBean;
import com.hjhq.teamface.basis.bean.ModuleBean;
import com.hjhq.teamface.basis.constants.ApproveConstants;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.constants.MemoConstant;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.rxbus.RxManager;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.EventBusUtils;
import com.hjhq.teamface.basis.util.JsonParser;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.bean.ProjectLabelBean;
import com.hjhq.teamface.common.bean.TaskInfoBean;
import com.hjhq.teamface.common.ui.comment.CommentActivity;
import com.hjhq.teamface.common.ui.dynamic.DynamicActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.adapter.PersonalSubTaskAdapter;
import com.hjhq.teamface.project.adapter.RelationTaskAdapter;
import com.hjhq.teamface.project.adapter.TaskItemAdapter;
import com.hjhq.teamface.project.bean.PersonalSubListResultBean;
import com.hjhq.teamface.project.bean.PersonalSubTaskBean;
import com.hjhq.teamface.project.bean.PersonalTaskDetailResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskLikeBean;
import com.hjhq.teamface.project.bean.PersonalTaskListResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskMembersResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskRoleResultBan;
import com.hjhq.teamface.project.bean.RelationListResultBean;
import com.hjhq.teamface.project.bean.SavePersonalRelationRequestBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.bean.TaskLikeBean;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.presenter.SelectModuleActivity;
import com.hjhq.teamface.project.ui.task.TaskDetailDelegate;
import com.hjhq.teamface.project.util.ProjectDialog;
import com.hjhq.teamface.project.util.TaskHelper;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.woxthebox.draglistview.DragItemAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * 个人任务详情
 *
 * @author Administrator
 * @date 2018/6/25
 */
@RouteNode(path = "/personal_task_detail", desc = "个人任务详情")
public class PersonalTaskDetailActivity extends ActivityPresenter<TaskDetailDelegate, TaskModel> implements View.OnClickListener {
    private long taskId;
    /**
     * 任务自定义布局 的bean
     */
    private String moduleBean;

    /**
     * 任务类型 0 任务 1子任务
     */
    int fromType = 0;
    private PersonalSubTaskAdapter subTaskAdapter;
    private RelationTaskAdapter taskItemAdapter;
    private RelationTaskAdapter beTaskItemAdapter;
    /**
     * 已完成状态
     */
    private boolean completeStatus;
    /**
     * 点赞人
     */
    private List<PersonalTaskLikeBean> shareArr;
    private String taskName;
    private Map<String, Object> detailMap = new HashMap<>();
    private String associatesStatus;
    /**
     * 自定义任务id
     */
    private String projectCustomId;
    /**
     * "项目" 关联的模块数据名称
     */
    private String relationData;
    /**
     * 模块数据id
     */
    private String relationId;
    /**
     * 0创建人 1执行人 2协作人
     */
    private String taskRole;
    /**
     * 编辑协作人
     */
    private boolean editAssociates;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0);
            taskName = intent.getStringExtra(ProjectConstants.TASK_NAME);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            fromType = intent.getIntExtra(ProjectConstants.TASK_FROM_TYPE, 0);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTaskTitle(taskName);
        viewDelegate.hideNavigator();

        subTaskAdapter = new PersonalSubTaskAdapter(null);
        setSubTaskHead();
        viewDelegate.setSubTaskAdapter(subTaskAdapter);
        taskItemAdapter = new RelationTaskAdapter();
        viewDelegate.setRelevanceAdapter(taskItemAdapter);
        beTaskItemAdapter = new RelationTaskAdapter();
        viewDelegate.setBeRelevanceAdapter(beTaskItemAdapter);
        initData();
    }

    private void initData() {
        //获取详情
        queryTaskDetail();

        //获取协助人列表（子任务或者任务）
        queryPersonalTaskMembers();

        if (fromType == 0) {
            //子任务
            querySubList();
        } else {
            viewDelegate.hideSubTask();
        }
        //关联任务
        queryRelationList();

        if (fromType == 0) {
            //被关联任务
            queryPersonalByRelations();
        } else {
            viewDelegate.hideBeRelevance();
        }
        //点赞列表
        queryShareList();
        //权限
        queryPersonalTaskRole();
    }

    /**
     * 获取权限
     */
    private void queryPersonalTaskRole() {
        model.queryPersonalTaskRole(mContext, taskId, fromType, new ProgressSubscriber<PersonalTaskRoleResultBan>(mContext) {
            @Override
            public void onNext(PersonalTaskRoleResultBan baseBean) {
                super.onNext(baseBean);
                taskRole = baseBean.getData().getRole();
                if ("2".equals(taskRole)) {
                    viewDelegate.showMenu();
                } else {
                    viewDelegate.showMenu(0);
                    editAssociates = true;
                    viewDelegate.sbtn.setEnabled(true);
                }
            }
        });
    }

    /**
     * 查询协作人列表
     */
    private void queryPersonalTaskMembers() {
        model.queryPersonalTaskMembers(this, taskId, fromType, new ProgressSubscriber<PersonalTaskMembersResultBean>(this) {
            @Override
            public void onNext(PersonalTaskMembersResultBean taskMemberListResultBean) {
                super.onNext(taskMemberListResultBean);

                List<Member> members = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskMemberListResultBean.getData())) {
                    for (PersonalTaskMembersResultBean.DataListBean dataBean : taskMemberListResultBean.getData()) {
                        Member member = new Member(dataBean.getEmployee_id(), dataBean.getEmployee_name(), C.EMPLOYEE);
                        members.add(member);
                    }
                }
                viewDelegate.setAssociates(members);
            }
        });
    }

    /**
     * 获取子任务
     */
    private void querySubList() {
        model.queryPersonalSubList(this, taskId, new ProgressSubscriber<PersonalSubListResultBean>(this) {
            @Override
            public void onNext(PersonalSubListResultBean baseBean) {
                super.onNext(baseBean);
                //子任务
                List<PersonalSubTaskBean> subTaskArr = baseBean.getData();
                CollectionUtils.notifyDataSetChanged(subTaskAdapter, subTaskAdapter.getData(), subTaskArr);
                setSubTaskHead();
            }
        });
    }

    /**
     * 获取关联任务
     */
    private void queryRelationList() {
        model.queryPersonalRelations(this, taskId, fromType, new ProgressSubscriber<RelationListResultBean>(mContext) {
            @Override
            public void onNext(RelationListResultBean baseBean) {
                super.onNext(baseBean);
                //关联任务
                List<RelationListResultBean.DataBean.ModuleDataBean> relationArr = baseBean.getData().getDataList();
                CollectionUtils.notifyDataSetChanged(taskItemAdapter, taskItemAdapter.getData(), relationArr);
            }
        });
    }

    /**
     * 获取被关联列表
     */
    private void queryPersonalByRelations() {
        model.queryPersonalByRelations(this, taskId, new ProgressSubscriber<RelationListResultBean>(mContext) {
            @Override
            public void onNext(RelationListResultBean baseBean) {
                super.onNext(baseBean);
                //被关联
                List<RelationListResultBean.DataBean.ModuleDataBean> relationArr2 = baseBean.getData().getDataList();
                CollectionUtils.notifyDataSetChanged(beTaskItemAdapter, beTaskItemAdapter.getData(), relationArr2);
            }
        });
    }

    /**
     * 点赞列表
     */
    private void queryShareList() {
        model.praisePersonQueryList(this, taskId, fromType, new ProgressSubscriber<PersonalTaskListResultBean>(mContext, false) {
            @Override
            public void onNext(PersonalTaskListResultBean baseBean) {
                super.onNext(baseBean);
                shareArr = baseBean.getData();
                if (shareArr == null) {
                    shareArr = new ArrayList<>();
                }
                viewDelegate.setLikeNum(shareArr.size());

                String employeeId = SPHelper.getEmployeeId();
                Observable.from(shareArr)
                        .filter(share -> employeeId.equals(share.getEmployee_id()))
                        .subscribe(share -> viewDelegate.setLikeStatus(true));
            }
        });
    }

    /**
     * 查询任务详情
     */
    private void queryTaskDetail() {
        if (fromType == 0) {
            model.queryPersonalTaskDetail(this, taskId, moduleBean, (queryTaskDetailResultBean, taskLayoutResultBean) -> {
                //将详情设置到布局中
                handleTaskDetail(queryTaskDetailResultBean, taskLayoutResultBean);
                return taskLayoutResultBean;
            }, new ProgressSubscriber<TaskLayoutResultBean>(this) {
                @Override
                public void onNext(TaskLayoutResultBean taskLayoutResultBean) {
                    super.onNext(taskLayoutResultBean);
                    viewDelegate.drawLayout(taskLayoutResultBean.getData().getEnableLayout(), moduleBean, true);
                }
            });
        } else if (fromType == 1) {
            model.queryPersonalSubTaskDetail(this, taskId, moduleBean, (queryTaskDetailResultBean, taskLayoutResultBean) -> {
                //将详情设置到布局中
                handleTaskDetail(queryTaskDetailResultBean, taskLayoutResultBean);
                return taskLayoutResultBean;
            }, new ProgressSubscriber<TaskLayoutResultBean>(this) {
                @Override
                public void onNext(TaskLayoutResultBean taskLayoutResultBean) {
                    super.onNext(taskLayoutResultBean);
                    viewDelegate.drawLayout(taskLayoutResultBean.getData().getEnableLayout(), moduleBean, true);
                }
            });
        }
    }

    /**
     * 将详情设置到布局中
     *
     * @param queryTaskDetailResultBean
     * @param taskLayoutResultBean
     */
    private void handleTaskDetail(PersonalTaskDetailResultBean queryTaskDetailResultBean, TaskLayoutResultBean taskLayoutResultBean) {
        //将详情设置到布局中
        TaskLayoutResultBean.DataBean.EnableLayoutBean enableLayout = taskLayoutResultBean.getData().getEnableLayout();
        List<CustomBean> rows = enableLayout.getRows();
        PersonalTaskDetailResultBean.DataBean taskData = queryTaskDetailResultBean.getData();

        Observable.just(1)
                .compose(TransformerHelper.applySchedulers())
                .subscribe(i ->
                        setDetail(taskData));


        detailMap = taskData.getCustomLayout();
        if (!CollectionUtils.isEmpty(rows)) {
            for (CustomBean customBean : rows) {
                Object value = detailMap.get(customBean.getName());
                if (ProjectConstants.PROJECT_TASK_RELATION.equals(customBean.getName())) {
                    //关联关系
                    String relationId = taskData.getRelation_id();
                    String relationData = taskData.getRelation_data();
                    String from_status = taskData.getFrom_status();
                    String bean_name = taskData.getBean_name();
                    if (TextUtil.isEmpty(relationId) && TextUtil.isEmpty(relationData)) {
                        detailMap.put(ProjectConstants.PROJECT_TASK_RELATION, "");
                        customBean.setValue("");
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", relationId);
                        map.put("name", relationData);
                        map.put("from_status", from_status);
                        map.put("bean_name", bean_name);
                        List<Map> list = new ArrayList<>();
                        list.add(map);
                        detailMap.put(ProjectConstants.PROJECT_TASK_RELATION, list);
                        customBean.setValue(list);
                    }
                } else if (ProjectConstants.PROJECT_TASK_LABEL.equals(customBean.getName())) {
                    List<Map> list = new ArrayList<>();
                    List<ProjectLabelBean> projectLabelBeen = new JsonParser<ProjectLabelBean>().jsonFromList(value, ProjectLabelBean.class);
                    Observable.from(projectLabelBeen).subscribe(label -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("value", label.getId());
                        map.put("label", label.getName());
                        map.put("color", label.getColour());
                        list.add(map);
                    });
                    detailMap.put(ProjectConstants.PROJECT_TASK_LABEL, list);
                    customBean.setValue(list);
                } else {
                    customBean.setValue(value);
                }
            }
        }
    }


    public void setSubTaskHead() {
        List<PersonalSubTaskBean> data = subTaskAdapter.getData();
        final int[] completeSize = {0};
        Observable.from(data)
                .filter(bean -> "1".equals(bean.getComplete_status()))
                .subscribe(bean -> completeSize[0] += 1);

        viewDelegate.setSubTaskHead(subTaskAdapter, completeSize[0]);
    }

    /**
     * 设置任务详情数据
     *
     * @param taskData
     */
    private void setDetail(PersonalTaskDetailResultBean.DataBean taskData) {
        if (taskData == null) {
            return;
        }
        //协作人是否可见
        associatesStatus = taskData.getParticipants_only();
        viewDelegate.setCheckedImmediatelyNoEvent("1".equals(associatesStatus));
        completeStatus = "1".equals(taskData.getComplete_status());
        viewDelegate.setTaskStatus(completeStatus);

        projectCustomId = taskData.getProject_custom_id();
        relationData = taskData.getRelation_data();
        relationId = taskData.getRelation_id();

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.ll_associates_control, R.id.ll_sub_task_control, R.id.ll_relevance_control
                , R.id.ll_be_relevance_control, R.id.tv_comment, R.id.tv_dynamic, R.id.tv_look_status, R.id.iv_like
                , R.id.tv_add_relevance, R.id.iv_task_status, R.id.ll_check, R.id.tv_add_sub_task);
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_DATA_DETAIL_CODE, tag -> UIRouter.getInstance().openUri(mContext, "DDComp://custom/detail", (Bundle) tag));
        RxManager.$(hashCode()).on(CustomConstants.MESSAGE_FILE_DETAIL_CODE, tag -> UIRouter.getInstance().openUri(mContext, "DDComp://custom/file", (Bundle) tag));

        viewDelegate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ArrayList<TaskLikeBean> list = new ArrayList<>();
            for (PersonalTaskLikeBean bean : shareArr) {
                TaskLikeBean like = new TaskLikeBean();
                like.setName(bean.getEmployee_name());
                like.setId(bean.getEmployee_id());
                list.add(like);
            }
            bundle.putSerializable(Constants.DATA_TAG1, list);
            CommonUtil.startActivtiy(mContext, TaskThumbUpActivity.class, bundle);
        }, R.id.tv_like_num);
        //子任务
        viewDelegate.subTaskRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                PersonalSubTaskBean item = (PersonalSubTaskBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putLong(ProjectConstants.TASK_ID, item.getId());
                bundle.putLong(ProjectConstants.TASK_FROM_TYPE, 1);
                bundle.putString(ProjectConstants.TASK_NAME, item.getName());
                bundle.putString(Constants.MODULE_BEAN, item.getBean_name());
                CommonUtil.startActivtiy(PersonalTaskDetailActivity.this, PersonalTaskDetailActivity.class, bundle);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                PersonalSubTaskBean item = subTaskAdapter.getItem(position);
                boolean complete = "1".equals(item.getComplete_status());
                updateSubTaskStatus(item.getId(), complete);
            }
        });


        //协作人
        viewDelegate.setOnClickListener(v -> {
            if (!editAssociates) {
                ToastUtils.showError(mContext, "没有权限！");
                return;
            }
            List<Member> members = viewDelegate.membersView.getMembers();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) members);
            bundle.putLong(ProjectConstants.TASK_ID, taskId);
            bundle.putLong(ProjectConstants.TASK_FROM_TYPE, fromType);
            CommonUtil.startActivtiyForResult(mContext, TaskMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        }, R.id.add_member_ll);

        //协作人可见
        viewDelegate.sbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.updatePersonalTaskAssociatesState(mContext, taskId, isChecked ? 1 : 0, fromType, new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    viewDelegate.setCheckedImmediatelyNoEvent(!isChecked);
                }
            });
        });

        //关联任务
        taskItemAdapter.setOnItemClickListener(new TaskItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DragItemAdapter adapter, View view, int position) {
                TaskInfoBean item = (TaskInfoBean) adapter.getItemList().get(position);
                TaskHelper.INSTANCE.clickTaskItem(mContext, item);
            }

            @Override
            public void onItemChildClick(DragItemAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemLongClick(DragItemAdapter adapter, View view, int position) {
                if (!checkRole()) return;
                //取消关联
                DialogUtils.getInstance().sureOrCancel(mContext, "确定取消该关联任务吗？", null, view.getRootView(), () -> {
                    TaskInfoBean item = (TaskInfoBean) adapter.getItemList().get(position);
                    //个人任务
                    new TaskModel().canclePersonalRelation(mContext, item.getId(), taskId, fromType, new ProgressSubscriber<BaseBean>(mContext) {
                        @Override
                        public void onNext(BaseBean baseBean) {
                            super.onNext(baseBean);
                            queryRelationList();
                            ToastUtils.showSuccess(mContext, "取消成功");
                        }
                    });
                });
            }
        });
        //被关联任务
        beTaskItemAdapter.setOnItemClickListener(new TaskItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DragItemAdapter adapter, View view, int position) {
                TaskInfoBean item = (TaskInfoBean) adapter.getItemList();
                TaskHelper.INSTANCE.clickTaskItem(mContext, item);
            }

            @Override
            public void onItemChildClick(DragItemAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemLongClick(DragItemAdapter adapter, View view, int position) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bundle bundle = new Bundle();
        if (id == R.id.ll_associates_control) {
            //协作人显示切换
            viewDelegate.switchAssociates();
        } else if (id == R.id.tv_add_sub_task) {
            //添加子任务
            addSubTaskClick();
        } else if (id == R.id.ll_sub_task_control) {
            //显示切换子任务
            subTaskClick();
        } else if (id == R.id.ll_relevance_control) {
            //显示切换关联
            relevanceClick();
        } else if (id == R.id.ll_be_relevance_control) {
            //显示切换被关联
            beRelevanceClick();
        } else if (id == R.id.tv_comment) {
            //评论
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, taskId + "");
            CommonUtil.startActivtiy(this, CommentActivity.class, bundle);
        } else if (id == R.id.tv_dynamic) {
            //动态
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, taskId + "");
            CommonUtil.startActivtiy(this, DynamicActivity.class, bundle);
        } else if (id == R.id.tv_look_status) {
            //查看状态
            bundle.putLong(ProjectConstants.TASK_ID, taskId);
            bundle.putLong(ProjectConstants.TASK_FROM_TYPE, fromType);
            CommonUtil.startActivtiy(mContext, TaskStatusActivity.class, bundle);
        } else if (id == R.id.iv_like) {
            //点赞
            likeOrUnLike();
        } else if (id == R.id.tv_add_relevance) {
            //添加关联
            addRelevanceClick();
        } else if (id == R.id.iv_task_status) {
            //完成、激活任务
            updateStatusClick();
        }
    }


    /**
     * 添加子任务点击
     */
    private void addSubTaskClick() {
        if (!checkRole()) return;
        Bundle bundle = new Bundle();
        bundle.putLong(ProjectConstants.TASK_ID, taskId);
        bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN);

        bundle.putString(ProjectConstants.PROJECT_CUSTOM_ID, projectCustomId);
        bundle.putString(ProjectConstants.RELATION_DATA, relationData);
        bundle.putString(ProjectConstants.RELATION_ID, relationId);
        CommonUtil.startActivtiyForResult(mContext, AddSubTaskActivity.class, ProjectConstants.ADD_SUB_TASK_REQUEST_CODE, bundle);
    }

    /**
     * 判断权限
     *
     * @return
     */
    private boolean checkRole() {
        if (taskRole == null) {
            ToastUtils.showError(mContext, "未获取到权限");
            return false;
        }
        if ("2".equals(taskRole)) {
            ToastUtils.showError(mContext, "权限不足");
            return false;
        }
        return true;
    }

    /**
     * 添加关联点击
     */
    private void addRelevanceClick() {
        if (!checkRole()) return;
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), null, new String[]{"新建", "引用"}, p -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA_TAG1, Constants.SELECT_TYPE);
            if (p == 0) {
                CommonUtil.startActivtiyForResult(mContext, SelectModuleActivity.class, ProjectConstants.ADD_TASK_REQUEST_CODE, bundle);
            } else {
                CommonUtil.startActivtiyForResult(mContext, SelectModuleActivity.class, ProjectConstants.QUOTE_TASK_REQUEST_CODE, bundle);
            }
            return false;
        });
    }

    /**
     * 完成、激活任务或子任务 点击
     */
    private void updateStatusClick() {
        if (!checkRole()) return;
        ProjectDialog.updatePersonalTaskStatus(fromType == 1, mContext, viewDelegate.getRootView(), taskId, completeStatus, new ProgressSubscriber<BaseBean>(mContext, 1) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                queryTaskDetail();
                EventBusUtils.sendEvent(new MessageBean(ProjectConstants.PERSONAL_TASK_REFRESH_CODE, taskId + "", null));
            }
        });
    }

    /**
     * 完成、激活子任务
     */
    private void updateSubTaskStatus(long taskId, boolean completeStatus) {
        if (!checkRole()) return;
        ProjectDialog.updatePersonalTaskStatus(true, mContext, viewDelegate.getRootView(), taskId, completeStatus, new ProgressSubscriber<BaseBean>(mContext, 1) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                querySubList();
            }
        });
    }


    /**
     * 点赞
     */
    private void likeOrUnLike() {
        viewDelegate.setLikeStatus();
        viewDelegate.setLikeNum(shareArr.size() + (viewDelegate.isLike() ? 1 : -1));
        JSONObject jsonObject = new JSONObject();

        //（分享，任务，子任务）记录id
        jsonObject.put("task_id", taskId);
        // 0不点赞  1点赞
        jsonObject.put("status", viewDelegate.isLike() ? 1 : 0);
        //点赞类型，0 任务，1子任务
        jsonObject.put("from_type", fromType);
        model.sharePersonalPraise(this, jsonObject, new ProgressSubscriber<BaseBean>(mContext, false) {
            @Override
            public void onNext(BaseBean o) {
                super.onNext(o);
                queryShareList();
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                if (viewDelegate.isLike()) {
                    ToastUtils.showError(mContext, "点赞失败");
                } else {
                    ToastUtils.showError(mContext, "取消点赞失败");
                }
            }
        });
    }

    /**
     * 被关联任务
     */
    private void beRelevanceClick() {
        viewDelegate.switchBeRelevance();
    }

    /**
     * 关联任务
     */
    private void relevanceClick() {
        viewDelegate.switchRelevance();
    }

    /**
     * 子任务点击
     */
    private void subTaskClick() {
        viewDelegate.switchSubTask();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE1) {
            queryPersonalTaskMembers();
        } else if (requestCode == ProjectConstants.ADD_TASK_REQUEST_CODE) {
            //新增
            TaskNew(data);
        } else if (requestCode == ProjectConstants.ADD_TASK_TASK_REQUEST_CODE) {
            //新增任务
            AddRelevaceByNewTask(data);
        } else if (requestCode == CustomConstants.REQUEST_ADDCUSTOM_CODE) {
            //新增自定义模块
            AddRelevaceByNewCustom(data);
        } else if (requestCode == ProjectConstants.ADD_TASK_MEMO_REQUEST_CODE) {
            //新增备忘录
            AddRelevaceByNewMemo(data);
        } else if (requestCode == ProjectConstants.ADD_TASK_APPROVE_REQUEST_CODE) {
            //新增审批
            AddRelevaceByNewApprove(data);
        } else if (requestCode == ProjectConstants.QUOTE_TASK_REQUEST_CODE) {
            //引用
            taskQuote(data);
        } else if (requestCode == ProjectConstants.QUOTE_TASK_MEMO_REQUEST_CODE) {
            //引用备忘录
            quoteMemo(data);
        } else if (requestCode == ProjectConstants.QUOTE_TASK_APPROVE_REQUEST_CODE) {
            //引用审批
            quoteApprove(data);
        } else if (requestCode == ProjectConstants.QUOTE_TASK_TASK_REQUEST_CODE) {
            //引用任务
            quoteTask(data);
        } else if (requestCode == ProjectConstants.QUOTE_TASK_CUSTOM_REQUEST_CODE) {
            //引用自定义
            quoteCustom(data);
        } else if (requestCode == ProjectConstants.EDIT_TASK_REQUEST_CODE) {
            //编辑任务
            queryTaskDetail();
        } else if (requestCode == ProjectConstants.ADD_SUB_TASK_REQUEST_CODE) {
            //新增子任务
            querySubList();
            queryPersonalTaskMembers();
        }
    }

    /**
     * 任务新增
     *
     * @param data
     */
    private void TaskNew(Intent data) {
        AppModuleBean appModeluBean = (AppModuleBean) data.getSerializableExtra(Constants.DATA_TAG1);

        Bundle bundle = new Bundle();
        String moduleBean = appModeluBean.getEnglish_name();
        switch (moduleBean) {
            case ProjectConstants.TASK_MODULE_BEAN:
                //任务
                bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN);
                CommonUtil.startActivtiyForResult(mContext, AddTaskActivity.class, ProjectConstants.ADD_TASK_TASK_REQUEST_CODE, bundle);
                break;
            case MemoConstant.BEAN_NAME:
                //备忘录
                UIRouter.getInstance().openUri(mContext, "DDComp://memo/add", bundle, ProjectConstants.ADD_TASK_MEMO_REQUEST_CODE);
                break;
            default:
                if (ApproveConstants.APPROVAL_MODULE_BEAN.equals(appModeluBean.getApplication_id())) {
                    //审批下面的模块
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/add", bundle, ProjectConstants.ADD_TASK_APPROVE_REQUEST_CODE);
                } else {
                    //自定义
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/add", bundle, CustomConstants.REQUEST_ADDCUSTOM_CODE);
                }
                break;
        }
    }

    /**
     * 通过新增任务添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewTask(Intent data) {
        long id = data.getLongExtra(Constants.DATA_TAG1, 0);
        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();

        bean.setBean_name(ProjectConstants.PERSONAL_TASK_BEAN);
        bean.setRelation_id(id + "");
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);
        addRelevance(bean);
    }

    /**
     * 通过新增自定义添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewCustom(Intent data) {
        ModuleBean.DataBean moduleBean = (ModuleBean.DataBean) data.getSerializableExtra(Constants.DATA_TAG1);
        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();

        bean.setBean_name(moduleBean.getBean());
        bean.setRelation_id(moduleBean.getDataId() + "");
        bean.setModule_name(moduleBean.getModuleName());
        bean.setModule_id(moduleBean.getModuleId());
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);
        addRelevance(bean);
    }

    /**
     * 通过新增备忘录添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewMemo(Intent data) {
        long dataId = data.getLongExtra(Constants.DATA_TAG1, 0);
        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();
        bean.setBean_name(MemoConstant.BEAN_NAME);
        bean.setRelation_id(dataId + "");
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);

        addRelevance(bean);
    }

    /**
     * 通过新增审批添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewApprove(Intent data) {
        ModuleBean.DataBean moduleBean = (ModuleBean.DataBean) data.getSerializableExtra(Constants.DATA_TAG1);
        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();

        bean.setBean_name(moduleBean.getBean());
        bean.setRelation_id(moduleBean.getDataId() + "");
        bean.setModule_name(moduleBean.getModuleName());
        bean.setModule_id(moduleBean.getModuleId());
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);
        addRelevance(bean);
    }

    /**
     * 任务引用
     *
     * @param data
     */
    private void taskQuote(Intent data) {
        AppModuleBean appModeluBean = (AppModuleBean) data.getSerializableExtra(Constants.DATA_TAG1);

        String moduleBean = appModeluBean.getEnglish_name();

        Bundle bundle = new Bundle();
        switch (moduleBean) {
            case ProjectConstants.TASK_MODULE_BEAN:
                //任务
                CommonUtil.startActivtiyForResult(mContext, QuoteTaskActivity.class, ProjectConstants.QUOTE_TASK_TASK_REQUEST_CODE, bundle);
                break;
            case MemoConstant.BEAN_NAME:
                UIRouter.getInstance().openUri(mContext, "DDComp://memo/choose_memo", bundle, ProjectConstants.QUOTE_TASK_MEMO_REQUEST_CODE);
                break;
            default:
                if (ApproveConstants.APPROVAL_MODULE_BEAN.equals(appModeluBean.getApplication_id())) {
                    //审批下面的模块
                    bundle.putString(Constants.DATA_TAG1, moduleBean);
                    bundle.putString(Constants.DATA_TAG2, appModeluBean.getChinese_name());
                    bundle.putString(Constants.DATA_TAG3, appModeluBean.getId());
                    UIRouter.getInstance().openUri(mContext, "DDComp://app/approve/select", bundle, ProjectConstants.QUOTE_TASK_APPROVE_REQUEST_CODE);
                } else {
                    //自定义模块
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    bundle.putString(Constants.MODULE_ID, appModeluBean.getId());
                    bundle.putString(Constants.NAME, appModeluBean.getChinese_name());
                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/select", bundle, ProjectConstants.QUOTE_TASK_CUSTOM_REQUEST_CODE);
                }
                break;
        }
    }

    /**
     * 引用备忘录
     *
     * @param data
     */
    private void quoteMemo(Intent data) {
        ArrayList<MemoListItemBean> choosedItem = (ArrayList<MemoListItemBean>) data.getSerializableExtra(Constants.DATA_TAG1);
        if (CollectionUtils.isEmpty(choosedItem)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (MemoListItemBean memo : choosedItem) {
            sb.append("," + memo.getId());
        }
        sb.deleteCharAt(0);

        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();
        bean.setBean_name(MemoConstant.BEAN_NAME);
        bean.setRelation_id(sb.toString());
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);

        addRelevance(bean);
    }

    /**
     * 引用审批
     *
     * @param data
     */
    private void quoteApprove(Intent data) {
        ArrayList<ApproveListBean> datas = (ArrayList<ApproveListBean>) data.getSerializableExtra(Constants.DATA_TAG1);
        String moduleName = data.getStringExtra(Constants.DATA_TAG2);
        String moduleId = data.getStringExtra(Constants.DATA_TAG3);
        String moduleBean = data.getStringExtra(Constants.DATA_TAG4);
        if (CollectionUtils.isEmpty(datas)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (ApproveListBean approve : datas) {
            sb.append("," + approve.getApproval_data_id());
        }
        sb.deleteCharAt(0);

        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();
        bean.setBean_name(moduleBean);
        bean.setModule_id(moduleId);
        bean.setModule_name(moduleName);
        bean.setRelation_id(sb.toString());
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);

        addRelevance(bean);
    }

    /**
     * 引用任务
     *
     * @param data
     */
    private void quoteTask(Intent data) {
        String ids = data.getStringExtra(Constants.DATA_TAG1);
        String beanName = data.getStringExtra(Constants.DATA_TAG2);

        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();
        bean.setBean_name(beanName);
        bean.setRelation_id(ids);
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);

        addRelevance(bean);
    }

    /**
     * 引用自定义
     *
     * @param data
     */
    private void quoteCustom(Intent data) {
        String moduleBean = data.getStringExtra(Constants.MODULE_BEAN);
        ArrayList<DataTempResultBean.DataBean.DataListBean> datas = (ArrayList<DataTempResultBean.DataBean.DataListBean>) data.getSerializableExtra(Constants.DATA_TAG1);
        String moduleName = data.getStringExtra(Constants.NAME);
        String moduleId = data.getStringExtra(Constants.MODULE_ID);
        if (CollectionUtils.isEmpty(datas)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (DataTempResultBean.DataBean.DataListBean dataListBean : datas) {
            sb.append("," + dataListBean.getId().getValue());
        }
        sb.deleteCharAt(0);

        SavePersonalRelationRequestBean bean = new SavePersonalRelationRequestBean();
        bean.setBean_name(moduleBean);
        bean.setModule_id(moduleId);
        bean.setModule_name(moduleName);
        bean.setRelation_id(sb.toString());
        bean.setTask_id(taskId);
        bean.setFrom_type(fromType);

        addRelevance(bean);
    }

    /**
     * 通过新增 添加关联
     *
     * @param bean
     */
    private void addRelevance(SavePersonalRelationRequestBean bean) {
        model.addRelevanceByPersonal(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "添加成功!");
                queryRelationList();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuClick();
        return super.onOptionsItemSelected(item);
    }

    private void menuClick() {
        List<String> menuList = new ArrayList<>();
        if (!completeStatus && ("0".equals(taskRole) || "1".equals(taskRole))) {
            menuList.add("编辑任务");
        }
        if ("0".equals(taskRole) || "1".equals(taskRole)) {
            menuList.add("设置任务提醒");
        }
        if ("0".equals(taskRole) || "1".equals(taskRole)) {
            menuList.add("设置重复任务");
        }
        if (!completeStatus && ("0".equals(taskRole) || "1".equals(taskRole))) {
            menuList.add("删除任务");
        }
        if (CollectionUtils.isEmpty(menuList)) {
            return;
        }
        String[] menus = menuList.toArray(new String[menuList.size()]);
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), null, menus, p -> {
            Bundle bundle = new Bundle();
            switch (p) {
                case 0:
                    bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PERSONAL_TASK_BEAN);
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    bundle.putSerializable(Constants.DATA_TAG1, (Serializable) detailMap);
                    bundle.putString(ProjectConstants.ASSOCIATE_STATUS, associatesStatus);
                    bundle.putString(ProjectConstants.PROJECT_CUSTOM_ID, projectCustomId);
                    bundle.putString(ProjectConstants.RELATION_DATA, relationData);
                    bundle.putString(ProjectConstants.RELATION_ID, relationId);
                    CommonUtil.startActivtiyForResult(mContext, EditTaskActivity.class, ProjectConstants.EDIT_TASK_REQUEST_CODE, bundle);
                    break;
                case 1:
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    bundle.putLong(ProjectConstants.TASK_FROM_TYPE, fromType);
                    CommonUtil.startActivtiy(mContext, PersonalTaskRemindActivity.class, bundle);
                    break;
                case 2:
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    CommonUtil.startActivtiy(mContext, PersonalTaskRepeatActivity.class, bundle);
                    break;
                case 3:
                    DialogUtils.getInstance().sureOrCancel(mContext, "删除任务后，所有子任务也将同时被删除。", null, viewDelegate.getRootView(), () -> {
                        model.delPersonalTask(mContext, taskId + "", new ProgressSubscriber<BaseBean>(mContext) {
                            @Override
                            public void onNext(BaseBean baseBean) {
                                super.onNext(baseBean);
                                ToastUtils.showSuccess(mContext, "删除成功");
                                finish();
                            }
                        });
                    });
                    break;
                default:
                    break;
            }
            return false;
        });
    }

}
