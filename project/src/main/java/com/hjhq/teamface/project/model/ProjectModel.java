package com.hjhq.teamface.project.model;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.AppListResultBean;
import com.hjhq.teamface.basis.bean.AppModuleResultBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CommonNewResultBean2;
import com.hjhq.teamface.basis.bean.LocalModuleBean;
import com.hjhq.teamface.basis.bean.UpdateAppModuleRequestBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.project.ProjectApiService;
import com.hjhq.teamface.project.bean.AddPersonalSubTaskRequestBean;
import com.hjhq.teamface.project.bean.AddSubTaskRequestBean;
import com.hjhq.teamface.project.bean.AddTaskRequestBean;
import com.hjhq.teamface.project.bean.AllNodeResultBean;
import com.hjhq.teamface.project.bean.FlowNodeResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskConditionResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskResultBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.ProjectTaskListResultBean;
import com.hjhq.teamface.project.bean.ProjectTempBean;
import com.hjhq.teamface.project.bean.QuerySettingResultBean;
import com.hjhq.teamface.project.bean.SaveProjectRequestBean;
import com.hjhq.teamface.project.bean.SaveSubNodeRequestBean;
import com.hjhq.teamface.project.bean.SortNodeRequestBean;
import com.hjhq.teamface.project.bean.TaskConditionResultBean;
import com.hjhq.teamface.project.bean.TaskNoteResultBean;
import com.hjhq.teamface.project.bean.TimeWorkbenchResultBean;
import com.hjhq.teamface.project.bean.WorkFlowResultBean;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscriber;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */

public class ProjectModel implements IModel<ProjectApiService> {
    @Override
    public ProjectApiService getApi() {
        return new ApiManager<ProjectApiService>().getAPI(ProjectApiService.class);
    }

    /**
     * 新增项目
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addProject(RxAppCompatActivity mActivity, SaveProjectRequestBean bean, Subscriber<CommonNewResultBean2> s) {
        getApi().addProject(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目列表
     *
     * @param mActivity
     * @param pageNum   页码
     * @param pageSize  页的大小
     * @param keyword   关键字（模糊匹配）
     * @param s
     */
    public void queryAllList(RxAppCompatActivity mActivity, int type, int pageNum, int pageSize, String keyword, Subscriber<ProjectListBean> s) {
        getApi().queryAllList(type, pageNum, pageSize, keyword).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取全部项目列表
     *
     * @param mActivity
     * @param s
     */
    public void queryAllList(RxAppCompatActivity mActivity, int type, Subscriber<ProjectListBean> s) {
        getApi().queryAllList(type).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目任务的筛选条件
     *
     * @param mActivity
     * @param s
     */
    public void queryProjectTaskConditions(RxAppCompatActivity mActivity, long projectId, Subscriber<TaskConditionResultBean> s) {
        getApi().queryProjectTaskConditions(projectId).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取个人任务的筛选条件
     *
     * @param mActivity
     * @param s
     */
    public void queryPersonalTaskConditions(RxAppCompatActivity mActivity, Subscriber<PersonalTaskConditionResultBean> s) {
        getApi().queryPersonalTaskConditions().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 筛选获取项目任务列表
     *
     * @param mActivity
     * @param s
     */
    public void queryProjectTask(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().queryProjectTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取个人任务列表
     *
     * @param mActivity
     * @param s
     */
    public void queryPersonalTask(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<PersonalTaskResultBean> s) {
        getApi().queryPersonalTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据项目id获取项目任务列表
     *
     * @param mActivity
     * @param queryType 0全部 1我负责 2我参与 3我创建 5已完成
     * @param s
     */
    public void queryTaskListByProjectId(RxAppCompatActivity mActivity, String projectId, int queryType, Subscriber<ProjectTaskListResultBean> s) {
        getApi().queryTaskListByProjectId(projectId, queryType).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取主节点
     *
     * @param mActivity
     * @param id        项目ID
     * @param s
     */
    public void getMainNode(RxAppCompatActivity mActivity, long id, Subscriber<AllNodeResultBean> s) {
        getApi().getMainNode(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 获取项目子节点
     *
     * @param mActivity
     * @param id        主节点ID
     * @param s
     */
    public void getSubNode(RxAppCompatActivity mActivity, long id, Subscriber<AllNodeResultBean> s) {
        getApi().getSubNode(id).map(new HttpResultFunc<>())
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
     * 保存项目主节点
     *
     * @param mActivity
     * @param s
     */
    public void addMainNode(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().addMainNode(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 主节点重命名
     *
     * @param mActivity
     * @param projectId 项目ID
     * @param nodeId    主节点id
     * @param name      节点名称
     * @param s
     */
    public void editMainNode(RxAppCompatActivity mActivity, long projectId, long nodeId, String name, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectId", projectId);
        jsonObject.put("nodeId", nodeId);
        jsonObject.put("name", name);
        getApi().editMainNode(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 删除主节点
     *
     * @param mActivity
     * @param projectId 项目ID
     * @param nodeId    主节点id
     * @param s
     */
    public void deleteMainNode(RxAppCompatActivity mActivity, long projectId, long nodeId, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectId", projectId);
        jsonObject.put("nodeId", nodeId);
        getApi().deleteMainNode(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 主节点拖动排序
     *
     * @param mActivity
     * @param bean      保存实体
     * @param s
     */
    public void sortMainNode(RxAppCompatActivity mActivity, SortNodeRequestBean bean, Subscriber<BaseBean> s) {
        getApi().sortMainNode(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 保存子节点
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addSubNode(RxAppCompatActivity mActivity, SaveSubNodeRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addSubNode(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 子节点重命名
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void editSubNode(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().editSubNode(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 删除项目子节点
     *
     * @param mActivity
     * @param projectId 项目ID
     * @param nodeId    主节点ID
     * @param subnodeId 子节点ID
     * @param s
     */
    public void deleteSubNode(RxAppCompatActivity mActivity, long projectId, long nodeId, long subnodeId, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("projectId", projectId);
        jsonObject.put("nodeId", nodeId);
        jsonObject.put("subnodeId", subnodeId);
        getApi().deleteSubNode(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 子节点拖动排序
     *
     * @param mActivity
     * @param bean      子节点实体
     * @param s
     */
    public void sortSubNode(RxAppCompatActivity mActivity, SortNodeRequestBean bean, Subscriber<BaseBean> s) {
        getApi().sortSubNode(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 任务拖动排序
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void sortTask(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().sortTask(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 得到应用下的模块
     *
     * @param mActivity
     * @param s
     */
    public void getModule(ActivityPresenter mActivity, String id, Subscriber<AppModuleResultBean> s) {
        getApi().getModule(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 得到全部审批模块集合
     *
     * @param mActivity
     * @param s
     */
    public void findApprovalModuleList(ActivityPresenter mActivity, Subscriber<AppModuleResultBean> s) {
        getApi().findApprovalModuleList().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 得到全部应用
     *
     * @param mActivity
     * @param s
     */
    public void getAppList(ActivityPresenter mActivity, Subscriber<AppListResultBean> s) {
        getApi().getAppList().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 固定模块列表
     *
     * @param mActivity
     * @param s
     */
    public void findModuleList(ActivityPresenter mActivity, Subscriber<LocalModuleBean> s) {
        getApi().findModuleList().map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 得到全部应用
     *
     * @param mActivity
     * @param s
     */
    public void saveAppModule(ActivityPresenter mActivity, UpdateAppModuleRequestBean list, Subscriber<BaseBean> s) {
        getApi().saveAppModule(list).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取子节点下任务列表
     *
     * @param mActivity
     * @param s
     */
    public void queryTaskList(ActivityPresenter mActivity, long id, int pageNum, int pageSize, Subscriber<TaskListBean> s) {
        getApi().queryTaskList(id, pageNum, pageSize).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取子节点下任务列表web
     *
     * @param mActivity
     * @param s
     */
    public void queryWebList(RxAppCompatActivity mActivity, long id, Subscriber<TaskListBean> s) {
        getApi().queryWebList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取子节点下任务列表
     *
     * @param mActivity
     * @param id        项目id
     * @param starLevel 星标 0为否，1为是
     * @param s
     */
    public void updateStar(ActivityPresenter mActivity, long id, int starLevel, Subscriber<BaseBean> s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("star_level", starLevel);
        getApi().updateStar(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 任务新增
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addTask(ActivityPresenter mActivity, AddTaskRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addTask(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 子任务新增
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addSubTask(ActivityPresenter mActivity, AddSubTaskRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addSubTask(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人任务 - 子任务新增
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addPersonalSubTask(ActivityPresenter mActivity, AddPersonalSubTaskRequestBean bean, Subscriber<BaseBean> s) {
        getApi().addPersonalSubTask(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 任务引用
     *
     * @param mActivity
     * @param json
     * @param s
     */
    public void quoteTask(ActivityPresenter mActivity, JSONObject json, Subscriber<BaseBean> s) {
        getApi().quoteTask(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
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
     * 获取所有的模板
     *
     * @param mActivity
     * @param s
     */
    public void queryProjectTemplateList(ActivityPresenter mActivity, int templateRole, Subscriber<ProjectTempBean> s) {
        getApi().queryProjectTemplateList(templateRole).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取模板的任务分组
     *
     * @param mActivity
     * @param s
     */
    public void queryTaskNoteByTempId(ActivityPresenter mActivity, long tempId, Subscriber<TaskNoteResultBean> s) {
        getApi().queryTaskNoteByTempId(tempId).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 查询项目企业工作流列表
     *
     * @param mActivity
     * @param s
     */
    public void queryWorkFlow(ActivityPresenter mActivity, Subscriber<WorkFlowResultBean> s) {
        getApi().queryWorkFlow().map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取工作流及其节点信息
     *
     * @param mActivity
     * @param s
     */
    public void queryFlowNodesById(ActivityPresenter mActivity, long flowId, Subscriber<FlowNodeResultBean> s) {
        getApi().queryFlowNodesById(flowId).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 移除项目成员
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void deleteProjectMember(ActivityPresenter mActivity, long id, Subscriber<BaseBean> s) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        getApi().deleteProjectMember(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增项目成员
     *
     * @param mActivity
     * @param projectId
     * @param s
     */
    public void addProjectMember(ActivityPresenter mActivity, long projectId, String employeeIds, Subscriber<BaseBean> s) {
        JSONObject json = new JSONObject();
        json.put("projectId", projectId);
        json.put("employeeIds", employeeIds);
        getApi().addProjectMember(json).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取时间工作台
     *
     * @param mActivity
     * @param workbenchType 类型 1超期未完成，2今日要做，3明天要做，4以后要做
     * @param s
     */
    public void queryTimeWorkbench(RxAppCompatActivity mActivity, int workbenchType, Subscriber<TimeWorkbenchResultBean> s) {
        getApi().queryTimeWorkbench(workbenchType, 1).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 时间工作台任务拖动
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void moveTimeWorkbench(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().moveTimeWorkbench(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 时间工作台任务排序
     *
     * @param mActivity
     * @param jsonObject
     * @param s
     */
    public void sortTimeWorkbench(RxAppCompatActivity mActivity, JSONObject jsonObject, Subscriber<BaseBean> s) {
        getApi().sortTimeWorkbench(jsonObject).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 根据ID查询基本设置详情
     *
     * @param mActivity
     * @param id        项目ID
     * @param s
     */
    public void querySettingById(ActivityPresenter mActivity, long id, Subscriber<QuerySettingResultBean> s) {
        getApi().querySettingById(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindToLifecycle())
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


}
