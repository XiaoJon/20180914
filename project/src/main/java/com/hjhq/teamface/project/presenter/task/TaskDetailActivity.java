package com.hjhq.teamface.project.presenter.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;

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
import com.hjhq.teamface.basis.util.device.ScreenUtils;
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
import com.hjhq.teamface.project.adapter.RelationTaskAdapter;
import com.hjhq.teamface.project.adapter.SubTaskAdapter;
import com.hjhq.teamface.project.adapter.TaskItemAdapter;
import com.hjhq.teamface.project.bean.AddTaskRequestBean;
import com.hjhq.teamface.project.bean.QueryHierarchyResultBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.bean.QuerySettingResultBean;
import com.hjhq.teamface.project.bean.QueryTaskAuthResultBean;
import com.hjhq.teamface.project.bean.QueryTaskDetailResultBean;
import com.hjhq.teamface.project.bean.QuoteRelationRequestBean;
import com.hjhq.teamface.project.bean.RelationListResultBean;
import com.hjhq.teamface.project.bean.SaveRelationRequestBean;
import com.hjhq.teamface.project.bean.SubListResultBean;
import com.hjhq.teamface.project.bean.SubTaskBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.bean.TaskLikeBean;
import com.hjhq.teamface.project.bean.TaskListResultBean;
import com.hjhq.teamface.project.bean.TaskMemberListResultBean;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.model.TaskModel;
import com.hjhq.teamface.project.presenter.ProjectDetailActivity;
import com.hjhq.teamface.project.presenter.SelectModuleActivity;
import com.hjhq.teamface.project.ui.task.TaskDetailDelegate;
import com.hjhq.teamface.project.util.ProjectDialog;
import com.hjhq.teamface.project.util.ProjectUtil;
import com.hjhq.teamface.project.util.TaskHelper;
import com.hjhq.teamface.project.util.TaskUtil;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;
import com.woxthebox.draglistview.DragItemAdapter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * 任务详情
 *
 * @author Administrator
 * @date 2018/6/25
 */
@RouteNode(path = "/project_task_detail", desc = "项目任务详情")
public class TaskDetailActivity extends ActivityPresenter<TaskDetailDelegate, TaskModel> implements View.OnClickListener {
    public long projectId;
    private long taskId;
    private long mainTaskId;
    /**
     * 任务自定义布局 的bean
     */
    private String moduleBean;
    long taskType = 1;
    private SubTaskAdapter subTaskAdapter;
    private RelationTaskAdapter taskItemAdapter;
    private RelationTaskAdapter beTaskItemAdapter;
    /**
     * 已完成状态
     */
    private boolean completeStatus;
    /**
     * 检验状态  0 未检验 1检验通过  2检验驳回
     */
    private String passedStatus;
    /**
     * 点赞人
     */
    private List<TaskLikeBean> shareArr;
    /**
     * 节点id
     */
    private long subNodeId;

    private String taskName;
    private Map<String, Object> detailMap = new HashMap<>();
    private String associatesStatus;
    private String checkStatus;
    private String checkMember;

    /**
     * 任务权限
     */
    private List<QueryTaskAuthResultBean.DataBean> taskAuthList;
    /**
     * 项目角色权限
     */
    private String priviledgeIds;
    /**
     * 任务权限角色
     */
    private List<String> taskRoles = new ArrayList<>();
    private boolean[] taskAuths;
    private String projectCompleteStatus;
    /**
     * 协作人是否可以修改
     */
    private boolean editAssociates;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            projectId = intent.getLongExtra(ProjectConstants.PROJECT_ID, 0);
            taskId = intent.getLongExtra(ProjectConstants.TASK_ID, 0);
            mainTaskId = intent.getLongExtra(ProjectConstants.MAIN_TASK_ID, 0);
            taskName = intent.getStringExtra(ProjectConstants.TASK_NAME);
            moduleBean = intent.getStringExtra(Constants.MODULE_BEAN);
            taskType = intent.getLongExtra(ProjectConstants.TASK_FROM_TYPE, 1);
        }
    }

    @Override
    public void init() {
        viewDelegate.setTaskTitle(taskName);
        viewDelegate.showMenu(0);

        subTaskAdapter = new SubTaskAdapter(null);
        setSubTaskHead();
        viewDelegate.setSubTaskAdapter(subTaskAdapter);
        taskItemAdapter = new RelationTaskAdapter();
        viewDelegate.setRelevanceAdapter(taskItemAdapter);
        beTaskItemAdapter = new RelationTaskAdapter();
        viewDelegate.setBeRelevanceAdapter(beTaskItemAdapter);
        initData();
    }

    private void initData() {
        //获取项目角色信息
        queryProjectRoleInfo();
        //根据项目ID查询权限
        queryTaskAuth();
        //获取任务所有角色的信息
        queryTaskRoles();
        //获取项目基本信息
        getProjectInfo();
        //详情
        queryTaskDetail();

        //任务层级关系接口
        queryByHierarchy();

        //获取任务 协助人角色 的信息（子任务或者任务）
        queryTaskMemberList();

        if (taskType == 1) {
            //子任务
            querySubList();
        } else {
            viewDelegate.hideSubTask();
        }

        if (taskType == 1) {
            //被关联任务
            queryByRelationList();
        } else {
            viewDelegate.hideBeRelevance();
        }

        //关联任务
        queryRelationList();
        //点赞列表
        queryShareList();
    }

    /**
     * 获取任务权限
     */
    private void queryTaskAuth() {
        model.queryTaskAuthList(this, projectId, new ProgressSubscriber<QueryTaskAuthResultBean>(this) {
            @Override
            public void onNext(QueryTaskAuthResultBean taskAuthResultBean) {
                super.onNext(taskAuthResultBean);
                taskAuthList = taskAuthResultBean.getData();
                permissionHandle();
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取任务权限失败");
            }
        });
    }


    /**
     * 获取项目角色信息
     */
    public void queryProjectRoleInfo() {
        //查询管理员权限
        new TaskModel().queryManagementRoleInfo(this, TextUtil.parseLong(SPHelper.getEmployeeId()), projectId, new ProgressSubscriber<QueryManagerRoleResultBean>(this) {
            @Override
            public void onNext(QueryManagerRoleResultBean queryManagerRoleResultBean) {
                super.onNext(queryManagerRoleResultBean);
                QueryManagerRoleResultBean.DataBean.PriviledgeBean priviledge = queryManagerRoleResultBean.getData().getPriviledge();
                priviledgeIds = priviledge.getPriviledge_ids();
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目角色权限失败");
            }
        });
    }

    /**
     * 获取项目信息
     */
    private void getProjectInfo() {
        new ProjectModel().querySettingById(mContext, projectId, new ProgressSubscriber<QuerySettingResultBean>(mContext) {
            @Override
            public void onNext(QuerySettingResultBean querySettingResultBean) {
                super.onNext(querySettingResultBean);
                QuerySettingResultBean.DataBean data = querySettingResultBean.getData();
                projectCompleteStatus = data.getProject_complete_status();
            }

            @Override
            public void onError(Throwable e) {
                dismissWindowView();
                e.printStackTrace();
                ToastUtils.showError(mContext, "获取项目信息失败");
            }
        });
    }

    /**
     * 任务层级关系接口
     */
    private void queryByHierarchy() {
        model.queryByHierarchy(this, taskId, new ProgressSubscriber<QueryHierarchyResultBean>(this) {
            @Override
            public void onNext(QueryHierarchyResultBean taskLayoutResultBean) {
                super.onNext(taskLayoutResultBean);
                QueryHierarchyResultBean.DataBean hierarchyData = taskLayoutResultBean.getData();
                subNodeId = hierarchyData.getSubnodeid2() == 0 ? hierarchyData.getSubnodeid() : hierarchyData.getSubnodeid2();
                viewDelegate.setNavigatorLayout(hierarchyData);
            }
        });
    }

    /**
     * 获取协作人列表信息
     */
    private void queryTaskMemberList() {
        model.queryTaskMemberList(this, projectId, taskId, taskType, 1, new ProgressSubscriber<TaskMemberListResultBean>(this) {
            @Override
            public void onNext(TaskMemberListResultBean taskMemberListResultBean) {
                super.onNext(taskMemberListResultBean);
                TaskMemberListResultBean.DataBean data = taskMemberListResultBean.getData();
                List<Member> members = new ArrayList<>();
                if (!CollectionUtils.isEmpty(data.getDataList())) {
                    for (TaskMemberListResultBean.DataBean.DataListBean dataBean : data.getDataList()) {
                        Member member = new Member(dataBean.getEmployee_id(), dataBean.getEmployee_name(), C.EMPLOYEE);
                        member.setSign_id(dataBean.getId());
                        members.add(member);
                    }
                }
                viewDelegate.setAssociates(members);
            }
        });
    }

    /**
     * 获取全部角色
     */
    private void queryTaskRoles() {
        model.queryTaskMemberList(this, projectId, taskId, taskType, 0, new ProgressSubscriber<TaskMemberListResultBean>(this) {
            @Override
            public void onNext(TaskMemberListResultBean taskMemberListResultBean) {
                super.onNext(taskMemberListResultBean);
                TaskMemberListResultBean.DataBean data = taskMemberListResultBean.getData();
                taskRoles.clear();
                if (!CollectionUtils.isEmpty(data.getDataList())) {
                    Observable.from(data.getDataList()).subscribe(dataBean -> taskRoles.add(dataBean.getProject_task_role()));
                }
                permissionHandle();
            }
        });
    }

    /**
     * 获取子任务
     */
    private void querySubList() {
        model.querySubList(this, taskId, new ProgressSubscriber<SubListResultBean>(this) {
            @Override
            public void onNext(SubListResultBean baseBean) {
                super.onNext(baseBean);
                //子任务
                List<SubTaskBean> subTaskArr = baseBean.getData().getDataList();
                CollectionUtils.notifyDataSetChanged(subTaskAdapter, subTaskAdapter.getData(), subTaskArr);
                setSubTaskHead();
            }
        });
    }

    /**
     * 获取被关联列表
     */
    private void queryByRelationList() {
        model.queryByRelationList(this, taskId, new ProgressSubscriber<RelationListResultBean>(mContext) {
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
     * 获取关联任务
     */
    private void queryRelationList() {
        model.queryRelationList(this, taskId, taskType, new ProgressSubscriber<RelationListResultBean>(mContext) {
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
     * 点赞列表
     */
    private void queryShareList() {
        model.praiseQueryList(this, taskId, taskType, new ProgressSubscriber<TaskListResultBean>(mContext, false) {
            @Override
            public void onNext(TaskListResultBean baseBean) {
                super.onNext(baseBean);
                shareArr = baseBean.getData().getDataList();
                viewDelegate.setLikeNum(CollectionUtils.size(shareArr));

                if (!CollectionUtils.isEmpty(shareArr)) {
                    String employeeId = SPHelper.getEmployeeId();
                    Observable.from(shareArr)
                            .filter(share -> employeeId.equals(share.getId()))
                            .subscribe(share -> viewDelegate.setLikeStatus(true));
                }
            }
        });
    }

    /**
     * 查询任务详情
     */
    private void queryTaskDetail() {
        if (taskType == 1) {
            model.queryTaskDetail(this, taskId, moduleBean, (queryTaskDetailResultBean, taskLayoutResultBean) -> {
                handleTaskDetail(queryTaskDetailResultBean, taskLayoutResultBean);
                return taskLayoutResultBean;
            }, new ProgressSubscriber<TaskLayoutResultBean>(this) {
                @Override
                public void onNext(TaskLayoutResultBean taskLayoutResultBean) {
                    super.onNext(taskLayoutResultBean);
                    viewDelegate.drawLayout(taskLayoutResultBean.getData().getEnableLayout(), moduleBean, false);
                }
            });
        } else if (taskType == 2) {
            model.querySubTaskDetail(this, taskId, moduleBean, (queryTaskDetailResultBean, taskLayoutResultBean) -> {
                handleTaskDetail(queryTaskDetailResultBean, taskLayoutResultBean);
                return taskLayoutResultBean;
            }, new ProgressSubscriber<TaskLayoutResultBean>(this) {
                @Override
                public void onNext(TaskLayoutResultBean taskLayoutResultBean) {
                    super.onNext(taskLayoutResultBean);
                    viewDelegate.drawLayout(taskLayoutResultBean.getData().getEnableLayout(), moduleBean, false);
                }
            });
        }
    }

    /**
     * 处理任务详情
     *
     * @param queryTaskDetailResultBean
     * @param taskLayoutResultBean
     */
    private void handleTaskDetail(QueryTaskDetailResultBean queryTaskDetailResultBean, TaskLayoutResultBean taskLayoutResultBean) {
        TaskLayoutResultBean.DataBean.EnableLayoutBean enableLayout = taskLayoutResultBean.getData().getEnableLayout();
        List<CustomBean> rows = enableLayout.getRows();
        QueryTaskDetailResultBean.DataBean taskData = queryTaskDetailResultBean.getData();

        Observable.just(1)
                .filter(o -> taskData != null)
                .compose(TransformerHelper.applySchedulers())
                .subscribe(i ->
                        setDetail(taskData));


        detailMap = taskData.getCustomArr();
        if (!CollectionUtils.isEmpty(rows)) {
            for (CustomBean customBean : rows) {
                Object value = detailMap.get(customBean.getName());
                if (ProjectConstants.PROJECT_TASK_LABEL.equals(customBean.getName())) {
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
        List<SubTaskBean> data = subTaskAdapter.getData();
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
    private void setDetail(QueryTaskDetailResultBean.DataBean taskData) {
        //协作人是否可见
        associatesStatus = taskData.getAssociates_status();
        viewDelegate.setCheckedImmediatelyNoEvent("1".equals(taskData.getAssociates_status()));
        completeStatus = "1".equals(taskData.getComplete_status());
        viewDelegate.setTaskStatus(completeStatus);

        checkStatus = taskData.getCheck_status();
        checkMember = taskData.getCheck_member();
        if ("1".equals(checkStatus)) {
            passedStatus = taskData.getPassed_status();
            if (completeStatus) {
                viewDelegate.setCheckStatus(passedStatus);
            }
            //已完成
            if (ProjectConstants.CHECK_STATUS_WAIT.equals(passedStatus) && completeStatus
                    && SPHelper.getEmployeeId().equals(taskData.getCheck_member())) {
                viewDelegate.setCheckBtnVis(View.VISIBLE);
            }
        }
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
            bundle.putSerializable(Constants.DATA_TAG1, (Serializable) shareArr);
            CommonUtil.startActivtiy(mContext, TaskThumbUpActivity.class, bundle);
        }, R.id.tv_like_num);
        viewDelegate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
            CommonUtil.startActivtiy(mContext, ProjectDetailActivity.class, bundle);
        }, R.id.tv_sub_node_name, R.id.tv_node_name, R.id.tv_project_name);

        viewDelegate.subTaskRecyclerView.addOnItemTouchListener(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                SubTaskBean item = (SubTaskBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putLong(ProjectConstants.TASK_ID, item.getId());
                bundle.putLong(ProjectConstants.TASK_FROM_TYPE, 2);
                bundle.putString(ProjectConstants.TASK_NAME, item.getName());
                bundle.putString(Constants.MODULE_BEAN, item.getBean_name());
                bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                bundle.putLong(ProjectConstants.SUBNODE_ID, subNodeId);
                bundle.putLong(ProjectConstants.MAIN_TASK_ID, taskId);
                CommonUtil.startActivtiy(TaskDetailActivity.this, TaskDetailActivity.class, bundle);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                SubTaskBean item = subTaskAdapter.getItem(position);
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
            bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
            bundle.putLong(ProjectConstants.TASK_ID, taskId);
            bundle.putLong(ProjectConstants.TASK_FROM_TYPE, taskType);
            if (taskType == 2) {
                bundle.putLong(ProjectConstants.MAIN_TASK_ID, mainTaskId);
            }

            CommonUtil.startActivtiyForResult(mContext, TaskMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        }, R.id.add_member_ll);

        //协作人可见
        viewDelegate.sbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.updateTaskAssociatesState(mContext, taskId, isChecked ? 1 : 0, taskType, new ProgressSubscriber<BaseBean>(mContext) {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    viewDelegate.setCheckedImmediatelyNoEvent(!isChecked);
                }
            });
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
                if (!checkTaskPermission(13)) {
                    ToastUtils.showError(mContext, "没有权限");
                    return;
                }
                DialogUtils.getInstance().sureOrCancel(mContext, "确定取消该关联任务吗？", null, view.getRootView(), () -> {
                    TaskInfoBean item = (TaskInfoBean) adapter.getItemList().get(position);
                    new TaskModel().cancleRelation(mContext, item.getTaskInfoId(), new ProgressSubscriber<BaseBean>(mContext) {
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
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_TASK_DYNAMIC_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, taskId + "");
            CommonUtil.startActivtiy(this, CommentActivity.class, bundle);
        } else if (id == R.id.tv_dynamic) {
            //动态
            bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_TASK_DYNAMIC_BEAN_NAME);
            bundle.putString(Constants.DATA_ID, taskId + "");
            CommonUtil.startActivtiy(this, DynamicActivity.class, bundle);
        } else if (id == R.id.tv_look_status) {
            //查看状态
            bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
            bundle.putLong(ProjectConstants.TASK_ID, taskId);
            bundle.putLong(ProjectConstants.TASK_FROM_TYPE, taskType);
            CommonUtil.startActivtiy(mContext, TaskStatusActivity.class, bundle);
        } else if (id == R.id.iv_like) {
            //点赞
            likeOrUnLike();
        } else if (id == R.id.tv_add_relevance) {
            //添加关联
            addRelevanceClick();
        } else if (id == R.id.iv_task_status) {
            //完成、激活任务
            updateTaskStatus();
        } else if (id == R.id.ll_check) {
            //校验
            TaskUtil.inputDialog(mContext, viewDelegate.getRootView(), new TaskUtil.OnInputClickListner() {
                @Override
                public boolean inputClickSure(PopupWindow popup, String content) {
                    updatePassedStatus(popup, 1, content);
                    return true;
                }

                @Override
                public boolean inputClickCancel(PopupWindow popup, String content) {
                    updatePassedStatus(popup, 2, content);
                    return true;
                }
            });
        }
    }


    /**
     * 添加子任务点击
     */
    private void addSubTaskClick() {
        if (!checkTaskPermission(8)) {
            ToastUtils.showError(mContext, "没有权限");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
        bundle.putLong(ProjectConstants.TASK_ID, taskId);
        bundle.putString(Constants.MODULE_BEAN, moduleBean);
        bundle.putString(ProjectConstants.CHECK_MEMBER, checkMember);
        bundle.putString(ProjectConstants.CHECK_STATUS, checkStatus);
        bundle.putString(ProjectConstants.ASSOCIATE_STATUS, associatesStatus);
        CommonUtil.startActivtiyForResult(mContext, AddSubTaskActivity.class, ProjectConstants.ADD_SUB_TASK_REQUEST_CODE, bundle);
    }

    /**
     * 添加关联点击
     */
    private void addRelevanceClick() {
        if (!checkTaskPermission(12)) {
            ToastUtils.showError(mContext, "没有权限");
            return;
        }
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
     * 校验
     *
     * @param popup
     * @param status
     * @param content
     */
    private void updatePassedStatus(PopupWindow popup, int status, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", taskId);
        jsonObject.put("status", status);
        jsonObject.put("content", content);
        model.updatePassedStatus(mContext, jsonObject, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                popup.dismiss();
                ScreenUtils.letScreenLight(mContext);
                queryTaskDetail();
            }
        });
    }

    /**
     * 完成、激活任务或子任务
     */
    private void updateTaskStatus() {
        if (!checkTaskPermission(2)) {
            ToastUtils.showError(mContext, "没有权限");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", taskId);
        jsonObject.put("completeStatus", completeStatus ? 0 : 1);

        ProjectDialog.updateTaskStatus(taskType == 2, mContext, viewDelegate.getRootView(), jsonObject, completeStatus, projectCompleteStatus, new ProgressSubscriber<BaseBean>(mContext, 1) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                queryTaskDetail();
                EventBusUtils.sendEvent(new MessageBean(ProjectConstants.PROJECT_TASK_REFRESH_CODE, taskId+"", null));
            }
        });
    }

    /**
     * 完成、激活子任务
     */
    private void updateSubTaskStatus(long taskId, boolean completeStatus) {
        if (!checkTaskPermission(2)) {
            ToastUtils.showError(mContext, "没有权限");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", taskId);
        jsonObject.put("completeStatus", completeStatus ? 0 : 1);

        ProjectDialog.updateTaskStatus(true, mContext, viewDelegate.getRootView(), jsonObject, completeStatus, projectCompleteStatus, new ProgressSubscriber<BaseBean>(mContext, 1) {
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
        if (!ProjectUtil.INSTANCE.checkProjectPermission(mContext, priviledgeIds, 27)) {
            return;
        }
        viewDelegate.setLikeStatus();
        viewDelegate.setLikeNum(CollectionUtils.size(shareArr) + (viewDelegate.isLike() ? 1 : -1));
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", taskId);
        jsonObject.put("status", viewDelegate.isLike() ? 1 : 0);
        jsonObject.put("typeStatus", taskType);
        model.sharePraise(this, jsonObject, new ProgressSubscriber<BaseBean>(mContext, false) {
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
            //更改协作人
            queryTaskMemberList();
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
            editTask(data);
        } else if (requestCode == ProjectConstants.MOVE_TASK_REQUEST_CODE) {
            //移动任务
            queryByHierarchy();
        } else if (requestCode == ProjectConstants.ADD_SUB_TASK_REQUEST_CODE) {
            //新增子任务
            querySubList();
            queryTaskMemberList();
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
                bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_TASK_MOBULE_BEAN + projectId);
                bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                CommonUtil.startActivtiyForResult(mContext, AddTaskActivity.class, ProjectConstants.ADD_TASK_TASK_REQUEST_CODE, bundle);
                break;
            case MemoConstant.BEAN_NAME:
                UIRouter.getInstance().openUri(mContext, "DDComp://memo/add", bundle, ProjectConstants.ADD_TASK_MEMO_REQUEST_CODE);
                break;
            default:
                if (ApproveConstants.APPROVAL_MODULE_BEAN.equals(appModeluBean.getApplication_id())) {
                    bundle.putString(Constants.MODULE_BEAN, moduleBean);
                    UIRouter.getInstance().openUri(mContext, "DDComp://custom/add", bundle, ProjectConstants.ADD_TASK_APPROVE_REQUEST_CODE);
                } else {
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
        SaveRelationRequestBean bean = new SaveRelationRequestBean();
        bean.setProjectId(projectId);
        bean.setBean(ProjectConstants.PROJECT_TASK_MOBULE_BEAN + projectId);
        bean.setDataId(id);
        bean.setSubnodeId(subNodeId);
        bean.setCheckMember(data.getStringExtra(Constants.DATA_TAG3));
        bean.setCheckStatus(data.getStringExtra(Constants.DATA_TAG2));
        bean.setAssociatesStatus(data.getStringExtra(Constants.DATA_TAG4));
        bean.setEndTime(data.getStringExtra(Constants.DATA_TAG5));
        bean.setExecutorId(data.getStringExtra(Constants.DATA_TAG6));
        bean.setStartTime(data.getStringExtra(Constants.DATA_TAG7));
        bean.setTaskName(data.getStringExtra(Constants.DATA_TAG8));
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);
        bean.setBean_type("2");

        addRelevanceByNew(bean);
    }

    /**
     * 通过新增自定义添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewCustom(Intent data) {
        ModuleBean.DataBean moduleBean = (ModuleBean.DataBean) data.getSerializableExtra(Constants.DATA_TAG1);
        SaveRelationRequestBean bean = new SaveRelationRequestBean();
        bean.setProjectId(projectId);
        bean.setBean(moduleBean.getBean());
        bean.setDataId(moduleBean.getDataId());
        bean.setSubnodeId(subNodeId);
        bean.setModuleId(moduleBean.getModuleId());
        bean.setModuleName(moduleBean.getModuleName());
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);
        bean.setTaskName(taskName);
        bean.setBean_type("3");

        addRelevanceByNew(bean);
    }

    /**
     * 通过新增备忘录添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewMemo(Intent data) {
        long dataId = data.getLongExtra(Constants.DATA_TAG1, 0);
        SaveRelationRequestBean bean = new SaveRelationRequestBean();
        bean.setProjectId(projectId);
        bean.setBean(MemoConstant.BEAN_NAME);
        bean.setDataId(dataId);
        bean.setSubnodeId(subNodeId);
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);
        bean.setTaskName(taskName);
        bean.setBean_type("1");

        addRelevanceByNew(bean);
    }

    /**
     * 通过新增审批添加关联
     *
     * @param data
     */
    private void AddRelevaceByNewApprove(Intent data) {
        ModuleBean.DataBean moduleBean = (ModuleBean.DataBean) data.getSerializableExtra(Constants.DATA_TAG1);
        SaveRelationRequestBean bean = new SaveRelationRequestBean();
        bean.setProjectId(projectId);
        bean.setBean(moduleBean.getBean());
        bean.setDataId(moduleBean.getDataId());
        bean.setSubnodeId(subNodeId);
        bean.setModuleId(moduleBean.getModuleId());
        bean.setModuleName(moduleBean.getModuleName());
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);
        bean.setTaskName(taskName);
        bean.setBean_type("4");

        addRelevanceByNew(bean);
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
                bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                CommonUtil.startActivtiyForResult(mContext, QuoteTaskActivity.class, ProjectConstants.QUOTE_TASK_TASK_REQUEST_CODE, bundle);
                break;
            case MemoConstant.BEAN_NAME:
                UIRouter.getInstance().openUri(mContext, "DDComp://memo/choose_memo", bundle, ProjectConstants.QUOTE_TASK_MEMO_REQUEST_CODE);
                break;
            default:
                if (ApproveConstants.APPROVAL_MODULE_BEAN.equals(appModeluBean.getApplication_id())) {
                    bundle.putString(Constants.DATA_TAG1, moduleBean);
                    bundle.putString(Constants.DATA_TAG2, appModeluBean.getChinese_name());
                    bundle.putString(Constants.DATA_TAG3, appModeluBean.getId());
                    UIRouter.getInstance().openUri(mContext, "DDComp://app/approve/select", bundle, ProjectConstants.QUOTE_TASK_APPROVE_REQUEST_CODE);
                } else {
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

        QuoteRelationRequestBean bean = new QuoteRelationRequestBean();
        bean.setBean(MemoConstant.BEAN_NAME);
        bean.setProjectId(projectId);
        bean.setBean_type(1);
        bean.setQuoteTaskId(sb.toString());
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);

        addRelevanceByQuote(bean);
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

        QuoteRelationRequestBean bean = new QuoteRelationRequestBean();
        bean.setBean(moduleBean);
        bean.setModuleId(moduleId);
        bean.setModuleName(moduleName);
        bean.setProjectId(projectId);
        bean.setBean_type(4);
        bean.setQuoteTaskId(sb.toString());
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);

        addRelevanceByQuote(bean);
    }

    /**
     * 引用任务
     *
     * @param data
     */
    private void quoteTask(Intent data) {
        String ids = data.getStringExtra(Constants.DATA_TAG1);
        String beanName = data.getStringExtra(Constants.DATA_TAG2);

        QuoteRelationRequestBean bean = new QuoteRelationRequestBean();
        bean.setBean(beanName);
        bean.setProjectId(projectId);
        bean.setBean_type(2);
        bean.setQuoteTaskId(ids);
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);

        addRelevanceByQuote(bean);
    }

    /**
     * 引用自定义
     *
     * @param data
     */
    private void quoteCustom(Intent data) {
        String moduleBean = data.getStringExtra(Constants.MODULE_BEAN);
        ArrayList<DataTempResultBean.DataBean.DataListBean> datas = (ArrayList<DataTempResultBean.DataBean.DataListBean>) data.getSerializableExtra(Constants.DATA_TAG1);
        String moduleId = data.getStringExtra(Constants.MODULE_ID);
        String moduleName = data.getStringExtra(Constants.NAME);
        if (CollectionUtils.isEmpty(datas)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (DataTempResultBean.DataBean.DataListBean dataListBean : datas) {
            sb.append("," + dataListBean.getId().getValue());
        }
        sb.deleteCharAt(0);

        QuoteRelationRequestBean bean = new QuoteRelationRequestBean();
        bean.setBean(moduleBean);
        bean.setModuleId(moduleId);
        bean.setModuleName(moduleName);
        bean.setProjectId(projectId);
        bean.setBean_type(3);
        bean.setQuoteTaskId(sb.toString());
        bean.setTaskId(taskId);
        bean.setTaskType(taskType);

        addRelevanceByQuote(bean);
    }

    /**
     * 编辑任务
     *
     * @param data
     */
    private void editTask(Intent data) {
        AddTaskRequestBean bean = new AddTaskRequestBean();
        bean.setProjectId(projectId);
        bean.setBean(ProjectConstants.PROJECT_TASK_MOBULE_BEAN + projectId);
        bean.setId(taskId + "");
        bean.setSubnodeId(subNodeId);
        bean.setCheckMember(data.getStringExtra(Constants.DATA_TAG3));
        bean.setCheckStatus(data.getStringExtra(Constants.DATA_TAG2));
        bean.setAssociatesStatus(data.getStringExtra(Constants.DATA_TAG4));
        bean.setEndTime(data.getStringExtra(Constants.DATA_TAG5));
        bean.setExecutorId(data.getStringExtra(Constants.DATA_TAG6));
        bean.setStartTime(data.getStringExtra(Constants.DATA_TAG7));
        bean.setTaskName(data.getStringExtra(Constants.DATA_TAG8));
        bean.setRemark(data.getStringExtra(Constants.DATA_TAG9));

        model.editTask(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "编辑成功");
                queryTaskDetail();
            }
        });
    }

    /**
     * 通过新增 添加关联
     *
     * @param bean
     */
    private void addRelevanceByNew(SaveRelationRequestBean bean) {
        model.addRelevanceByNew(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "添加成功!");
                queryRelationList();
            }
        });
    }

    /**
     * 通过引用 添加关联
     *
     * @param bean
     */
    private void addRelevanceByQuote(QuoteRelationRequestBean bean) {
        model.addRelevanceByQuote(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
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
        List<String> menuList = new ArrayList<>();
        if (!completeStatus && checkTaskPermission(1)) {
            menuList.add("编辑任务");
        }
        if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 26)) {
            menuList.add("设置任务提醒");
        }
        if (checkTaskPermission(5)) {
            menuList.add("设置重复任务");
        }
        if (checkTaskPermission(4)) {
            menuList.add("移动任务");
        }
        if (ProjectUtil.INSTANCE.checkPermission(priviledgeIds, 25)) {
            menuList.add("复制任务");
        }
        if (!completeStatus && checkTaskPermission(3)) {
            menuList.add("删除任务");
        }
        if (CollectionUtils.isEmpty(menuList)) {
            ToastUtils.showError(mContext, "没有权限");
            return super.onOptionsItemSelected(item);
        }

        if (CollectionUtils.isEmpty(menuList)) {
            return false;
        }
        String[] menus = menuList.toArray(new String[menuList.size()]);
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), null, menus, p -> {
            Bundle bundle = new Bundle();
            switch (menus[p]) {
                case "编辑任务":
                    bundle.putString(Constants.MODULE_BEAN, ProjectConstants.PROJECT_TASK_MOBULE_BEAN + projectId);
                    bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    bundle.putSerializable(Constants.DATA_TAG1, (Serializable) detailMap);
                    bundle.putString(ProjectConstants.CHECK_MEMBER, checkMember);
                    bundle.putString(ProjectConstants.CHECK_STATUS, checkStatus);
                    bundle.putString(ProjectConstants.ASSOCIATE_STATUS, associatesStatus);
                    bundle.putLong(ProjectConstants.LAYOUT_ID, TextUtil.parseLong(detailMap.get("id") + ""));
                    bundle.putLong(ProjectConstants.TASK_FROM_TYPE, taskType);
                    CommonUtil.startActivtiyForResult(mContext, EditTaskActivity.class, ProjectConstants.EDIT_TASK_REQUEST_CODE, bundle);
                    break;
                case "设置任务提醒":
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                    bundle.putLong(ProjectConstants.TASK_FROM_TYPE, taskType);
                    CommonUtil.startActivtiy(mContext, TaskRemindActivity.class, bundle);
                    break;
                case "设置重复任务":
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    CommonUtil.startActivtiy(mContext, RepeatTaskActivity.class, bundle);
                    break;
                case "移动任务":
                    bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    CommonUtil.startActivtiyForResult(mContext, MoveTaskActivity.class, ProjectConstants.MOVE_TASK_REQUEST_CODE, bundle);
                    break;
                case "复制任务":
                    bundle.putLong(ProjectConstants.TASK_ID, taskId);
                    bundle.putLong(ProjectConstants.PROJECT_ID, projectId);
                    CommonUtil.startActivtiy(mContext, CopyTaskActivity.class, bundle);
                    break;
                case "删除任务":
                    DialogUtils.getInstance().sureOrCancel(mContext, "删除任务后，所有子任务也将同时被删除。", null, viewDelegate.getRootView(), () -> {
                        model.delTask(mContext, taskId, new ProgressSubscriber<BaseBean>(mContext) {
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
        return super.onOptionsItemSelected(item);
    }


    /**
     * 检测任务权限
     *
     * @param permission
     */
    private boolean checkTaskPermission(int permission) {
        if (taskAuths != null) {
            return taskAuths[permission - 1];
        }
        return false;
    }

    /**
     * 权限处理
     */
    private synchronized void permissionHandle() {
        if (CollectionUtils.isEmpty(taskRoles) || CollectionUtils.isEmpty(taskAuthList)) {
            return;
        }
        taskAuths = new boolean[13];
        for (QueryTaskAuthResultBean.DataBean auth : taskAuthList) {
            for (String taskRole : taskRoles) {
                if (taskRole.equals(auth.getRole_type())) {
                    Class<QueryTaskAuthResultBean.DataBean> clz = QueryTaskAuthResultBean.DataBean.class;
                    try {
                        for (int i = 0; i < 13; i++) {
                            Method mt = clz.getMethod("getAuth_" + (i + 1));
                            if ("1".equals(mt.invoke(auth))) {
                                taskAuths[i] = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        if (checkTaskPermission(11)) {
            viewDelegate.sbtn.setEnabled(true);
        }
        if (checkTaskPermission(6) && checkTaskPermission(7)) {
            editAssociates = true;
        }
    }

}
