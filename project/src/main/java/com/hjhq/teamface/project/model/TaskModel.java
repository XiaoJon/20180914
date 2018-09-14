package com.hjhq.teamface.project.model;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CommonNewResultBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.MainRetrofit;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.factory.FastJsonConverterFactory;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.project.ProjectApiService;
import com.hjhq.teamface.project.bean.AddTaskRequestBean;
import com.hjhq.teamface.project.bean.AllNodeResultBean;
import com.hjhq.teamface.project.bean.CustomModuleResultBean;
import com.hjhq.teamface.project.bean.EditTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.PersonalSubListResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskDetailResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskListResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskMembersResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskRemindRequestBean;
import com.hjhq.teamface.project.bean.PersonalTaskRemindResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskRoleResultBan;
import com.hjhq.teamface.project.bean.PersonalTaskStatusResultBean;
import com.hjhq.teamface.project.bean.ProjectLabelsListBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.QueryHierarchyResultBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.bean.QueryTaskAuthResultBean;
import com.hjhq.teamface.project.bean.QueryTaskCompleteAuthResultBean;
import com.hjhq.teamface.project.bean.QueryTaskDetailResultBean;
import com.hjhq.teamface.project.bean.QuoteRelationRequestBean;
import com.hjhq.teamface.project.bean.QuoteTaskListResultBean;
import com.hjhq.teamface.project.bean.QuoteTaskResultBean;
import com.hjhq.teamface.project.bean.ReferTaskListResultBean;
import com.hjhq.teamface.project.bean.RelationListResultBean;
import com.hjhq.teamface.project.bean.SavePersonalRelationRequestBean;
import com.hjhq.teamface.project.bean.SavePersonalTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.SaveRelationRequestBean;
import com.hjhq.teamface.project.bean.SaveTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.SaveTaskResultBean;
import com.hjhq.teamface.project.bean.SubListResultBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.bean.TaskListResultBean;
import com.hjhq.teamface.project.bean.TaskMemberListResultBean;
import com.hjhq.teamface.project.bean.TaskRemindRequestBean;
import com.hjhq.teamface.project.bean.TaskRemindResultBean;
import com.hjhq.teamface.project.bean.TaskRepeatRequestBean;
import com.hjhq.teamface.project.bean.TaskRepeatResultBean;
import com.hjhq.teamface.project.bean.TaskStatusResultBean;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by Administrator on 2018/6/25.
 */

public class TaskModel implements IModel<ProjectApiService> {
    @Override
    public ProjectApiService getApi() {
        return new ApiManager<ProjectApiService>().getAPI(ProjectApiService.class);
    }


    /**
     * 获取引用任务列表
     *
     * @param mActivity
     * @param s
     */
    public void queryQuoteList(ActivityPresenter mActivity, long projectId, int pageSize, int pageNum, String keyword, Subscriber<ReferTaskListResultBean> s) {
        getApi().queryQuoteList(pageNum, pageSize, keyword, projectId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取个人任务、全部项目
     *
     * @param mActivity
     * @param s
     */
    public void queryTaskList(ActivityPresenter mActivity, Subscriber<QuoteTaskListResultBean> s) {
        getApi().queryTaskList().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查询 引用任务列表
     *
     * @param mActivity
     * @param pageNum   页数
     * @param pageSize  每页个数
     * @param keyword   关键字
     * @param from      0 项目任务 1个人任务
     * @param projectId
     * @param s
     */
    public void queryQuoteTask(ActivityPresenter mActivity, int pageNum, int pageSize,
                               String keyword, int from, String projectId, Subscriber<QuoteTaskResultBean> s) {
        getApi().queryQuoteTask(pageNum, pageSize, keyword, 0, from, projectId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 根据任务ID查询详情
     *
     * @param mActivity
     * @param id        任务记录ID
     * @param bean      自定义布局bean
     * @param s
     */
    public void queryTaskDetail(ActivityPresenter mActivity, long id, String bean,
                                Func2<QueryTaskDetailResultBean, TaskLayoutResultBean, TaskLayoutResultBean> func2, Subscriber<TaskLayoutResultBean> s) {
        ProjectApiService projectApiService = new MainRetrofit.Builder<ProjectApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(ProjectApiService.class);

        Observable.zip(projectApiService.queryTaskDetailById(id), getApi().getTaskLayout(bean), func2)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据任务ID查询详情
     *
     * @param mActivity
     * @param id        任务记录ID
     * @param bean      自定义布局bean
     * @param s
     */
    public void querySubTaskDetail(ActivityPresenter mActivity, long id, String bean, Func2<QueryTaskDetailResultBean, TaskLayoutResultBean, TaskLayoutResultBean> func2, Subscriber<TaskLayoutResultBean> s) {
        ProjectApiService projectApiService = new MainRetrofit.Builder<ProjectApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(ProjectApiService.class);

        Observable.zip(projectApiService.querySubTaskDetailById(id), getApi().getTaskLayout(bean), func2)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据任务ID查询个人任务详情
     *
     * @param mActivity
     * @param id        任务记录ID
     * @param bean      自定义布局bean
     * @param s
     */
    public void queryPersonalTaskDetail(ActivityPresenter mActivity, long id, String bean, Func2<PersonalTaskDetailResultBean, TaskLayoutResultBean, TaskLayoutResultBean> func2, Subscriber<TaskLayoutResultBean> s) {
        ProjectApiService projectApiService = new MainRetrofit.Builder<ProjectApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(ProjectApiService.class);

        Observable.zip(projectApiService.queryPersonalTaskDetail(id), getApi().getTaskLayout(bean), func2)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 根据任务ID查询个人任务详情
     *
     * @param mActivity
     * @param id        任务记录ID
     * @param bean      自定义布局bean
     * @param s
     */
    public void queryPersonalSubTaskDetail(ActivityPresenter mActivity, long id, String bean, Func2<PersonalTaskDetailResultBean, TaskLayoutResultBean, TaskLayoutResultBean> func2, Subscriber<TaskLayoutResultBean> s) {
        ProjectApiService projectApiService = new MainRetrofit.Builder<ProjectApiService>()
                .addConverterFactory(new FastJsonConverterFactory())
                .build(ProjectApiService.class);

        Observable.zip(projectApiService.queryPersonalSubTaskDetail(id), getApi().getTaskLayout(bean), func2)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务下子任务列表
     *
     * @param mActivity
     * @param s
     */
    public void querySubList(ActivityPresenter mActivity, long id, Subscriber<SubListResultBean> s) {
        getApi().querySubList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取个人任务下子任务列表
     *
     * @param mActivity
     * @param s
     */
    public void queryPersonalSubList(ActivityPresenter mActivity, long id, Subscriber<PersonalSubListResultBean> s) {
        getApi().queryPersonalSubList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务下关联列表（任务和子任务）
     *
     * @param mActivity
     * @param s
     */
    public void queryRelationList(ActivityPresenter mActivity, long id, long taskType, Subscriber<RelationListResultBean> s) {
        getApi().queryRelationList(id, taskType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取个人任务下的 关联列表
     *
     * @param mActivity
     * @param s
     */
    public void queryPersonalRelations(ActivityPresenter mActivity, long taskId, int fromType, Subscriber<RelationListResultBean> s) {
        getApi().queryPersonalRelations(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务下被关联列表（任务有，子任务没有）
     *
     * @param mActivity
     * @param s
     */
    public void queryByRelationList(ActivityPresenter mActivity, long id, Subscriber<RelationListResultBean> s) {
        getApi().queryByRelationList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取个人任务下被关联列表（任务有，子任务没有）
     *
     * @param mActivity
     * @param s
     */
    public void queryPersonalByRelations(ActivityPresenter mActivity, long id, Subscriber<RelationListResultBean> s) {
        getApi().queryPersonalByRelations(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 任务上的点赞列表（子任务与任务）
     * 点赞类型，0 分享，1任务，2子任务
     *
     * @param mActivity
     * @param s
     */
    public void praiseQueryList(ActivityPresenter mActivity, long id, long typeStatus, Subscriber<TaskListResultBean> s) {
        getApi().praiseQueryList(id, typeStatus).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 任务上的点赞列表（个人任务）
     * 点赞类型，0任务，1子任务
     *
     * @param mActivity
     * @param s
     */
    public void praisePersonQueryList(ActivityPresenter mActivity, long taskId, int fromType, Subscriber<PersonalTaskListResultBean> s) {
        getApi().praisePersonQueryList(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查询获取成员列表成员的权限
     *
     * @param mActivity
     * @param s
     */
    public void queryManagementRoleInfo(ActivityPresenter mActivity, long eid, long projectId, Subscriber<QueryManagerRoleResultBean> s) {
        getApi().queryManagementRoleInfo(eid, projectId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务权限
     *
     * @param mActivity
     * @param projectId
     * @param s
     */
    public void queryTaskRoles(ActivityPresenter mActivity, long projectId, long taskId, long taskType,
                               Func2<TaskMemberListResultBean, QueryTaskAuthResultBean, QueryTaskAuthResultBean> func2,
                               Subscriber<QueryTaskAuthResultBean> s) {
        Observable.zip(getApi().queryTaskMemberList(projectId, taskId, taskType, 0), getApi().queryTaskAuthList(projectId), func2)
                .map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取任务自定义
     *
     * @param mActivity
     * @param s
     */
    public void getTaskLayout(RxAppCompatActivity mActivity, String bean, Subscriber<TaskLayoutResultBean> s) {
        getApi().getTaskLayout(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 任务层级关系接口
     *
     * @param mActivity
     * @param s
     */
    public void queryByHierarchy(ActivityPresenter mActivity, long id, Subscriber<QueryHierarchyResultBean> s) {
        getApi().queryByHierarchy(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取协助人列表（子任务或者任务）
     *
     * @param mActivity
     * @param id         项目ID
     * @param taskId     任务ID
     * @param typeStatus 1任务人员，2子任务人员
     * @param all        1 是否查询所有人员0：是，1：否
     * @param s
     */
    public void queryTaskMemberList(ActivityPresenter mActivity, long id, long taskId, long typeStatus, int all, Subscriber<TaskMemberListResultBean> s) {
        getApi().queryTaskMemberList(id, taskId, typeStatus, all).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取协助人列表（个人任务）
     *
     * @param mActivity
     * @param taskId    任务ID
     * @param fromType  0 任务人员，1 子任务人员
     * @param s
     */
    public void queryPersonalTaskMembers(ActivityPresenter mActivity, long taskId, int fromType, Subscriber<PersonalTaskMembersResultBean> s) {
        getApi().queryPersonalTaskMembers(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据项目ID 获取任务权限
     *
     * @param mActivity
     * @param id        项目ID
     * @param s
     */
    public void queryTaskAuthList(ActivityPresenter mActivity, long id, Subscriber<QueryTaskAuthResultBean> s) {
        getApi().queryTaskAuthList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务完成权限
     *
     * @param mActivity
     * @param project_id 项目ID
     * @param task_type  任务分类，1主任务，2子任务
     * @param task_id    任务ID
     * @param s
     */
    public void queryTaskCompleteAuth(RxAppCompatActivity mActivity, long project_id, long task_type, long task_id, Subscriber<QueryTaskCompleteAuthResultBean> s) {
        getApi().queryTaskCompleteAuth(project_id, task_type, task_id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目任务点赞
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void sharePraise(ActivityPresenter mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().sharePraise(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 个人任务点赞
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void sharePersonalPraise(ActivityPresenter mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().sharePersonalPraise(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 任务勾选完成状态复选框
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void updateStatus(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().updateStatus(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 子任务勾选完成状态复选框
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void updateSubStatus(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().updateSubStatus(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务勾选完成状态复选框
     *
     * @param mActivity
     * @param s
     */
    public void updatePersonelTaskStatus(RxAppCompatActivity mActivity, long taskId, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId", taskId);
        getApi().updatePersonelTaskStatus(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人子任务勾选完成状态复选框
     *
     * @param mActivity
     * @param s
     */
    public void updatePersonelSubTaskStatus(RxAppCompatActivity mActivity, long taskId, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId", taskId);
        getApi().updatePersonelSubTaskStatus(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 校验
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void updatePassedStatus(ActivityPresenter mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().updatePassedStatus(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取项目标签列表
     *
     * @param mActivity
     * @param id
     * @param type      0项目拥有标签 1所有标签
     * @param s
     */
    public void getProjectLabel(ActivityPresenter mActivity, long id, int type, Subscriber<ProjectLabelsListBean> s) {
        getApi().getProjectLabel(id, type).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取项目标签列表
     *
     * @param mActivity
     * @param id        子类则是父标签ID 分类则不填 子类不传ID则获取所有子类
     * @param type      1 添加分类 2 添加子类
     * @param s
     */
    public void getAllLabel(ActivityPresenter mActivity, Long id, int type, Subscriber<ProjectLabelsListBean> s) {
        getApi().getAllLabel(id, type).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目成员
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryProjectMember(ActivityPresenter mActivity, long id, Subscriber<ProjectMemberResultBean> s) {
        getApi().queryProjectMember(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增个人任务
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addPersonalTask(ActivityPresenter mActivity, SaveTaskLayoutRequestBean bean, Subscriber<CommonNewResultBean> s) {
        getApi().addPersonalTask(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 保存任务自定义业务数据
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveTaskLayout(ActivityPresenter mActivity, SaveTaskLayoutRequestBean bean, Subscriber<SaveTaskResultBean> s) {
        getApi().saveTaskLayout(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑任务自定义业务数据
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void editTaskLayout(ActivityPresenter mActivity, EditTaskLayoutRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editTaskLayout(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 编辑个人任务
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void editPersonalTask(ActivityPresenter mActivity, SavePersonalTaskLayoutRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editPersonalTask(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 任务编辑
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void editTask(ActivityPresenter mActivity, AddTaskRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editTask(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 取消任务关联
     *
     * @param mActivity
     * @param id        记录ID
     * @param id        taskInfoId
     * @param s
     */
    public void cancleRelation(RxAppCompatActivity mActivity, long id, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        getApi().cancleRelation(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 取消个人任务关联
     *
     * @param mActivity
     * @param id        记录ID
     * @param id        taskInfoId
     * @param s
     */
    public void canclePersonalRelation(RxAppCompatActivity mActivity, long id, long taskId, long fromType, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ids", id);
        jsonObject.put("taskId", taskId);
        jsonObject.put("fromType", fromType);
        getApi().canclePersonalRelation(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 通过新增 添加关联
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addRelevanceByNew(ActivityPresenter mActivity, SaveRelationRequestBean bean, ProgressSubscriber<BaseBean> s) {
        getApi().addRelevanceByNew(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务添加关联
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addRelevanceByPersonal(ActivityPresenter mActivity, SavePersonalRelationRequestBean bean, ProgressSubscriber<BaseBean> s) {
        getApi().addRelevanceByPersonal(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 通过引用添加关联
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addRelevanceByQuote(ActivityPresenter mActivity, QuoteRelationRequestBean bean, ProgressSubscriber<BaseBean> s) {
        getApi().addRelevanceByQuote(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除任务
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void delTask(ActivityPresenter mActivity, long id, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        getApi().delTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除个人任务
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void delPersonalTask(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ids", id);
        getApi().delPersonalTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取全部节点
     *
     * @param mActivity
     * @param id        项目ID
     * @param s
     */
    public void getAllNode(RxAppCompatActivity mActivity, long id, Subscriber<AllNodeResultBean> s) {
        getApi().getAllNode(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 移动任务
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void moveTask(ActivityPresenter mActivity, long id, long subId, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("subId", subId);
        getApi().moveTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 复制任务
     *
     * @param mActivity
     * @param taskId
     * @param s
     */
    public void copyTask(ActivityPresenter mActivity, long taskId, long mainNodeId, long subNodeId, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId", taskId);
        jsonObject.put("subNodeId", subNodeId);
        jsonObject.put("mainNodeId", mainNodeId);
        getApi().copyTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务提醒列表
     *
     * @param mActivity
     * @param taskId
     * @param s
     */
    public void getTaskRemindList(ActivityPresenter mActivity, long taskId, long fromType, Subscriber<TaskRemindResultBean> s) {
        getApi().getTaskRemindList(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增任务设置提醒数据
     *
     * @param mActivity
     * @param s
     */
    public void addTaskRemind(ActivityPresenter mActivity, TaskRemindRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addTaskRemind(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑任务设置提醒数据
     *
     * @param mActivity
     * @param s
     */
    public void editTaskRemind(ActivityPresenter mActivity, TaskRemindRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editTaskRemind(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取项目任务重复任务设置
     *
     * @param mActivity
     * @param taskId
     * @param s
     */
    public void getTaskRepeatList(ActivityPresenter mActivity, long taskId, Subscriber<TaskRepeatResultBean> s) {
        getApi().getTaskRepeatList(taskId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目任务设置重复任务
     *
     * @param mActivity
     * @param s
     */
    public void addTaskRepeat(ActivityPresenter mActivity, TaskRepeatRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addTaskRepeat(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目任务编辑重复任务
     *
     * @param mActivity
     * @param s
     */
    public void editTaskRepeat(ActivityPresenter mActivity, TaskRepeatRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editTaskRepeat(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取个人任务提醒列表
     *
     * @param mActivity
     * @param taskId
     * @param s
     */
    public void getPersonalTaskRemindList(ActivityPresenter mActivity, long taskId, long fromType, Subscriber<PersonalTaskRemindResultBean> s) {
        getApi().getPersonalTaskRemindList(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增个人任务设置提醒数据
     *
     * @param mActivity
     * @param s
     */
    public void addPersonalTaskRemind(ActivityPresenter mActivity, PersonalTaskRemindRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addPersonalTaskRemind(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑个人任务设置提醒数据
     *
     * @param mActivity
     * @param s
     */
    public void editPersonalTaskRemind(ActivityPresenter mActivity, PersonalTaskRemindRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editPersonalTaskRemind(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取个人任务重复任务设置
     *
     * @param mActivity
     * @param taskId
     * @param s
     */
    public void getPersonalTaskRepeatList(ActivityPresenter mActivity, long taskId, Subscriber<TaskRepeatResultBean> s) {
        getApi().getPersonalTaskRepeatList(taskId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务设置重复任务
     *
     * @param mActivity
     * @param s
     */
    public void addPersonalTaskRepeat(ActivityPresenter mActivity, TaskRepeatRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addPersonalTaskRepeat(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务编辑重复任务
     *
     * @param mActivity
     * @param s
     */
    public void editPersonalTaskRepeat(ActivityPresenter mActivity, TaskRepeatRequestBean bean, Subscriber<BaseBean> s) {
        getApi().editPersonalTaskRepeat(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取个人任务权限
     *
     * @param mActivity
     * @param taskId
     * @param fromType
     * @param s
     */
    public void queryPersonalTaskRole(RxAppCompatActivity mActivity, long taskId, long fromType, Subscriber<PersonalTaskRoleResultBan> s) {
        getApi().queryPersonalTaskRole(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目任务--修改协助人可见
     *
     * @param mActivity
     * @param taskId           任务id
     * @param associatesStatus 0否 1是
     * @param fromType         任务类型，1 主任务 2 子任务
     * @param s
     */
    public void updateTaskAssociatesState(ActivityPresenter mActivity, long taskId, long associatesStatus, long fromType, Subscriber<BaseBean> s) {
        JSONObject json = new JSONObject();
        json.put("id", taskId);
        json.put("associatesStatus", associatesStatus);
        json.put("taskType", fromType);
        getApi().updateTaskAssociatesState(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务--修改协助人可见
     *
     * @param mActivity
     * @param taskId           任务id
     * @param associatesStatus 0否 1是
     * @param fromType         0任务关联信息 1，子任务关联信息
     * @param s
     */
    public void updatePersonalTaskAssociatesState(ActivityPresenter mActivity, long taskId, long associatesStatus, long fromType, Subscriber<BaseBean> s) {
        JSONObject json = new JSONObject();
        json.put("task_id", taskId);
        json.put("participants_only", associatesStatus);
        json.put("from_type", fromType);
        getApi().updatePersonalTaskAssociatesState(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目任务--修改协助人
     *
     * @param mActivity
     * @param json
     * @param s
     */
    public void saveTaskMembers(ActivityPresenter mActivity, JSONObject json, Subscriber<BaseBean> s) {
        getApi().saveTaskMembers(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目任务--删除协助人
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void delTaskMembers(ActivityPresenter mActivity, long id, Subscriber<BaseBean> s) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        getApi().delTaskMembers(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务--修改协助人
     *
     * @param mActivity
     * @param taskId      任务id
     * @param employeeIds 人员
     * @param fromType    0任务关联信息 1，子任务关联信息
     * @param s
     */
    public void updatePersonalTaskMembers(ActivityPresenter mActivity, long taskId, String employeeIds, long fromType, Subscriber<BaseBean> s) {
        JSONObject json = new JSONObject();
        json.put("taskId", taskId);
        json.put("employeeIds", employeeIds);
        json.put("fromType", fromType);
        getApi().updatePersonalTaskMembers(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查看项目任务状态
     *
     * @param mActivity
     * @param projectId
     * @param taskId
     * @param taskType
     * @param s
     */
    public void queryTaskStatus(RxAppCompatActivity mActivity, long projectId, long taskId, long taskType, Subscriber<TaskStatusResultBean> s) {
        getApi().queryTaskStatus(projectId, taskId, taskType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查看个人任务状态
     *
     * @param taskId
     * @param s
     */
    public void queryPersonalTaskStatus(RxAppCompatActivity mActivity, long taskId, long fromType, Subscriber<PersonalTaskStatusResultBean> s) {
        getApi().queryPersonalTaskStatus(taskId, fromType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据当前登陆人权限获取所有自定义模块（个人任务）
     *
     * @param s
     */
    public void queryAllModule(RxAppCompatActivity mActivity, Subscriber<CustomModuleResultBean> s) {
        getApi().queryAllModule().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }
}
