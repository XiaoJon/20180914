package com.hjhq.teamface.project;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.AppListResultBean;
import com.hjhq.teamface.basis.bean.AppModuleResultBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CommonNewResultBean;
import com.hjhq.teamface.basis.bean.CommonNewResultBean2;
import com.hjhq.teamface.basis.bean.LocalModuleBean;
import com.hjhq.teamface.basis.bean.UpdateAppModuleRequestBean;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.project.bean.AddPersonalSubTaskRequestBean;
import com.hjhq.teamface.project.bean.AddSubTaskRequestBean;
import com.hjhq.teamface.project.bean.AddTaskRequestBean;
import com.hjhq.teamface.project.bean.AllNodeResultBean;
import com.hjhq.teamface.project.bean.CustomModuleResultBean;
import com.hjhq.teamface.project.bean.EditTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.FlowNodeResultBean;
import com.hjhq.teamface.project.bean.PersonalSubListResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskConditionResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskDetailResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskListResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskMembersResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskRemindRequestBean;
import com.hjhq.teamface.project.bean.PersonalTaskRemindResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskResultBean;
import com.hjhq.teamface.project.bean.PersonalTaskRoleResultBan;
import com.hjhq.teamface.project.bean.PersonalTaskStatusResultBean;
import com.hjhq.teamface.project.bean.ProjectLabelsListBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.ProjectTaskListResultBean;
import com.hjhq.teamface.project.bean.ProjectTempBean;
import com.hjhq.teamface.project.bean.QueryHierarchyResultBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.bean.QuerySettingResultBean;
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
import com.hjhq.teamface.project.bean.SaveProjectRequestBean;
import com.hjhq.teamface.project.bean.SaveRelationRequestBean;
import com.hjhq.teamface.project.bean.SaveSubNodeRequestBean;
import com.hjhq.teamface.project.bean.SaveTaskLayoutRequestBean;
import com.hjhq.teamface.project.bean.SaveTaskResultBean;
import com.hjhq.teamface.project.bean.SortNodeRequestBean;
import com.hjhq.teamface.project.bean.SubListResultBean;
import com.hjhq.teamface.project.bean.TaskConditionResultBean;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.bean.TaskListResultBean;
import com.hjhq.teamface.project.bean.TaskMemberListResultBean;
import com.hjhq.teamface.project.bean.TaskNoteResultBean;
import com.hjhq.teamface.project.bean.TaskRemindRequestBean;
import com.hjhq.teamface.project.bean.TaskRemindResultBean;
import com.hjhq.teamface.project.bean.TaskRepeatRequestBean;
import com.hjhq.teamface.project.bean.TaskRepeatResultBean;
import com.hjhq.teamface.project.bean.TaskStatusResultBean;
import com.hjhq.teamface.project.bean.TimeWorkbenchResultBean;
import com.hjhq.teamface.project.bean.WorkFlowResultBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * 项目
 *
 * @author xj
 */
public interface ProjectApiService {
    /**
     * 新增项目
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/save")
    Observable<CommonNewResultBean2> addProject(@Body SaveProjectRequestBean bean);

    /**
     * 获取项目列表
     */
    @GET("projectController/queryAllList")
    Observable<ProjectListBean> queryAllList(@Query("type") int type, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("keyword") String keyword);

    /**
     * 获取全部项目列表
     */
    @GET("projectController/queryAllWebList")
    Observable<ProjectListBean> queryAllList(@Query("type") int type);

    /**
     * 筛选获取项目任务列表
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/queryProjectTaskListByCondition")
    Observable<BaseBean> queryProjectTask(@Body JSONObject jsonObject);

    /**
     * 获取项目任务的筛选条件
     */
    @GET("projectTaskController/queryProjectTaskConditions")
    Observable<TaskConditionResultBean> queryProjectTaskConditions(@Query("projectId") long projectId);

    /**
     * 获取个人任务的筛选条件
     */
    @GET("personelTask/findPersonelTaskConditions")
    Observable<PersonalTaskConditionResultBean> queryPersonalTaskConditions();

    /**
     * 获取个人任务列表
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTask/queryTaskListByCondition")
    Observable<PersonalTaskResultBean> queryPersonalTask(@Body JSONObject jsonObject);

    /**
     * 根据项目id获取项目任务列表
     */
    @GET("personelTask/findTaskListByProjectId")
    Observable<ProjectTaskListResultBean> queryTaskListByProjectId(@Query("projectId") String projectId, @Query("queryType") int queryType);

    /**
     * 获取主节点
     */
    @GET("projectController/queryMainNode")
    Observable<AllNodeResultBean> getMainNode(@Query("id") long id);

    /**
     * 获取项目子节点
     */
    @GET("projectController/querySubNode")
    Observable<AllNodeResultBean> getSubNode(@Query("id") long id);

    /**
     * 获取项目所有节点
     */
    @GET("projectController/queryAllNode")
    Observable<AllNodeResultBean> getAllNode(@Query("id") long id);


    /**
     * 保存项目主节点
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/saveMainNode")
    Observable<BaseBean> addMainNode(@Body JSONObject json);

    /**
     * 主节点重命名
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/editMainNode")
    Observable<BaseBean> editMainNode(@Body JSONObject json);

    /**
     * 删除主节点
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/deleteMainNode")
    Observable<BaseBean> deleteMainNode(@Body JSONObject json);

    /**
     * 主节点拖动排序
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/sortMainNode")
    Observable<BaseBean> sortMainNode(@Body SortNodeRequestBean bean);

    /**
     * 保存子节点
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/saveSubNode")
    Observable<BaseBean> addSubNode(@Body SaveSubNodeRequestBean bean);

    /**
     * 子节点重命名
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/editSubNode")
    Observable<BaseBean> editSubNode(@Body JSONObject json);

    /**
     * 删除项目子节点
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/deleteSubNode")
    Observable<BaseBean> deleteSubNode(@Body JSONObject json);

    /**
     * 子节点拖动排序
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/sortSubNode")
    Observable<BaseBean> sortSubNode(@Body SortNodeRequestBean bean);


    /**
     * 任务拖动排序
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/sort")
    Observable<BaseBean> sortTask(@Body JSONObject json);


    /**
     * 得到应用下的模块
     */
    @GET("module/findModuleListByAuth")
    Observable<AppModuleResultBean> getModule(@Query("application_id") String id);

    /**
     * 查询审批模块集合
     */
    @GET("module/findApprovalModuleList")
    Observable<AppModuleResultBean> findApprovalModuleList();

    /**
     * 获取应用列表
     */
    @GET("module/findPcAllModuleList")
    Observable<AppListResultBean> getAppList();

    /**
     * 固定模块列表
     *
     * @return
     */
    @GET("moduleManagement/findModuleList")
    Observable<LocalModuleBean> findModuleList();

    /**
     * 编辑应用常用模块
     */
    @Headers({"Content-Type: application/json"})
    @POST("applicationModuleUsed/save")
    Observable<BaseBean> saveAppModule(@Body UpdateAppModuleRequestBean bean);

    /**
     * 获取子节点下任务列表
     */
    @GET("projectTaskController/queryList")
    Observable<TaskListBean> queryTaskList(@Query("id") long id, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 获取子节点下任务列表WEB
     */
    @GET("projectTaskController/queryWebList")
    Observable<TaskListBean> queryWebList(@Query("id") long id);

    /**
     * 更新项目星标
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/updateProjectAsterisk")
    Observable<BaseBean> updateStar(@Body JSONObject json);

    /**
     * 任务--新增自定义
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/save")
    Observable<BaseBean> addTask(@Body AddTaskRequestBean bean);

    /**
     * 任务--新增子任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/saveSub")
    Observable<BaseBean> addSubTask(@Body AddSubTaskRequestBean bean);

    /**
     * 个人任务--新增子任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelSubTask/saveData")
    Observable<BaseBean> addPersonalSubTask(@Body AddPersonalSubTaskRequestBean bean);

    /**
     * 任务--编辑自定义
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/updateTask")
    Observable<BaseBean> editTask(@Body AddTaskRequestBean bean);

    /**
     * 保存任务自定义业务数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectLayoutOperation/save")
    Observable<SaveTaskResultBean> saveTaskLayout(@Body SaveTaskLayoutRequestBean bean);

    /**
     * 编辑任务自定义业务数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectLayoutOperation/edit")
    Observable<BaseBean> editTaskLayout(@Body EditTaskLayoutRequestBean bean);

    /**
     * 编辑个人任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTask/update")
    Observable<BaseBean> editPersonalTask(@Body SavePersonalTaskLayoutRequestBean bean);


    /**
     * 新增个人任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTask/save")
    Observable<CommonNewResultBean> addPersonalTask(@Body SaveTaskLayoutRequestBean bean);

    /**
     * 任务引用
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/quote")
    Observable<BaseBean> quoteTask(@Body JSONObject json);

    /**
     * 获取项目任务引用列表
     */
    @GET("projectTaskController/queryQuoteList")
    Observable<ReferTaskListResultBean> queryQuoteList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("keyword") String keyword, @Query("projectId") long projectId);

    /**
     * 获取个人任务和项目列表
     */
    @GET("personelTask/queryCommonTaskList")
    Observable<QuoteTaskListResultBean> queryTaskList();

    @GET("projectTaskController/queryQuoteTask")
    Observable<QuoteTaskResultBean> queryQuoteTask(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize,
                                                   @Query("keyword") String keyword, @Query("query_type") int queryType,
                                                   @Query("from") int from, @Query("project_id") String projectId);

    /**
     * 根据ID查询基本设置详情
     */
    @GET("projectSettingController/queryById")
    Observable<QuerySettingResultBean> querySettingById(@Query("id") long id);

    /**
     * 根据任务ID查询详情
     */
    @GET("projectTaskController/queryById")
    Observable<QueryTaskDetailResultBean> queryTaskDetailById(@Query("id") long id);

    /**
     * 根据子任务ID查询详情
     */
    @GET("projectTaskController/querySubById")
    Observable<QueryTaskDetailResultBean> querySubTaskDetailById(@Query("id") long id);

    /**
     * 根据任务ID查询个人任务详情
     */
    @GET("personelTask/getDataDetail")
    Observable<PersonalTaskDetailResultBean> queryPersonalTaskDetail(@Query("taskId") long taskId);

    /**
     * 根据任务ID查询个人子任务详情
     */
    @GET("personelSubTask/getDataDetail")
    Observable<PersonalTaskDetailResultBean> queryPersonalSubTaskDetail(@Query("taskId") long taskId);

    /**
     * 获取任务下子任务列表
     */
    @GET("projectTaskController/querySubList")
    Observable<SubListResultBean> querySubList(@Query("id") long id);

    /**
     * 获取个人任务下 子任务列表
     */
    @GET("personelTask/querySubTaskByTaskId")
    Observable<PersonalSubListResultBean> queryPersonalSubList(@Query("taskId") long taskId);

    /**
     * 获取任务下关联列表（任务和子任务）
     */
    @GET("projectTaskController/queryRelationList")
    Observable<RelationListResultBean> queryRelationList(@Query("id") long id, @Query("taskType") long taskType);

    /**
     * 获取个人任务下的 关联列表
     */
    @GET("personelTaskAssociates/queryTaskAssociatesByTaskIdAndType")
    Observable<RelationListResultBean> queryPersonalRelations(@Query("taskId") long taskId, @Query("fromType") long fromType);

    /**
     * 获取任务下被关联列表（任务有，子任务没有）
     */
    @GET("projectTaskController/queryByRelationList")
    Observable<RelationListResultBean> queryByRelationList(@Query("id") long id);

    /**
     * 获取个人任务下被关联列表（任务有，子任务没有）
     */
    @GET("personelTaskAssociates/queryBeTaskAssociatesByTaskIdAndType")
    Observable<RelationListResultBean> queryPersonalByRelations(@Query("taskId") long taskId);

    /**
     * 任务上的点赞列表（子任务与任务）
     * 点赞类型，0 分享，1任务，2子任务
     */
    @GET("projectShareController/praiseQueryList")
    Observable<TaskListResultBean> praiseQueryList(@Query("id") long id, @Query("typeStatus") long typeStatus);

    /**
     * 任务上的点赞列表（个人任务）
     * 点赞类型，0任务，1子任务
     */
    @GET("personelTaskPraiseRecord/getPraisePerson")
    Observable<PersonalTaskListResultBean> praisePersonQueryList(@Query("taskId") long taskId, @Query("fromType") int fromType);

    /**
     * 查询获取成员列表成员的权限
     */
    @GET("projectMemberController/queryManagementRoleInfo")
    Observable<QueryManagerRoleResultBean> queryManagementRoleInfo(@Query("eid") long id, @Query("projectId") long projectId);

    /**
     * 获取任务自定义
     */
    @GET("projectLayout/getAllLayout")
    Observable<TaskLayoutResultBean> getTaskLayout(@Query("bean") String bean);

    /**
     * 任务层级关系接口
     */
    @GET("projectTaskController/queryByHierarchy")
    Observable<QueryHierarchyResultBean> queryByHierarchy(@Query("id") long id);

    /**
     * 获取协助人列表（子任务或者任务）
     */
    @GET("projectMemberController/queryTaskList")
    Observable<TaskMemberListResultBean> queryTaskMemberList(@Query("id") long id, @Query("taskId") long taskId, @Query("typeStatus") long typeStatus, @Query("all") int all);

    /**
     * 获取协助人列表（个人任务）
     */
    @GET("personelTaskMember/queryMembersTaskId")
    Observable<PersonalTaskMembersResultBean> queryPersonalTaskMembers(@Query("taskId") long taskId, @Query("fromType") int fromType);


    /**
     * 根据项目ID 获取任务权限
     */
    @GET("projectSettingController/queryTaskAuthList")
    Observable<QueryTaskAuthResultBean> queryTaskAuthList(@Query("id") long id);

    /**
     * 获取任务完成权限
     */
    @GET("projectTaskController/queryCompleteTaskAuth")
    Observable<QueryTaskCompleteAuthResultBean> queryTaskCompleteAuth(@Query("project_id") long project_id, @Query("task_type") long task_type, @Query("task_id") long task_id);

    /**
     * 任务上的点赞与取消点赞（子任务与任务）
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/sharePraise")
    Observable<BaseBean> sharePraise(@Body JSONObject jsonObject);

    /**
     * 任务上的点赞与取消点赞（个人任务）
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskPraiseRecord/saveData")
    Observable<BaseBean> sharePersonalPraise(@Body JSONObject jsonObject);

    /**
     * 子任务勾选完成状态复选框
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/updateSubStatus")
    Observable<BaseBean> updateSubStatus(@Body JSONObject jsonObject);

    /**
     * 任务勾选完成状态复选框
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/updateStatus")
    Observable<BaseBean> updateStatus(@Body JSONObject jsonObject);

    /**
     * 个人任务勾选完成状态复选框
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTask/updateForFinish")
    Observable<BaseBean> updatePersonelTaskStatus(@Body JSONObject jsonObject);

    /**
     * 个人子任务勾选完成状态复选框
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelSubTask/updateForFinish")
    Observable<BaseBean> updatePersonelSubTaskStatus(@Body JSONObject jsonObject);


    /**
     * 校验
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/updatePassedStatus")
    Observable<BaseBean> updatePassedStatus(@Body JSONObject jsonObject);

    /**
     * 项目标签列表
     *
     * @param id
     * @return
     */
    @GET("projectSettingController/queryLabelsList")
    Observable<ProjectLabelsListBean> getProjectLabel(@Query("id") long id, @Query("type") int type);


    /**
     * 查询标签目录
     *
     * @return
     */
    @GET("projectManagementTagController/queryList")
    Observable<ProjectLabelsListBean> getAllLabel(@Query("id") Long id, @Query("type") int type);

    /**
     * 获取项目成员
     *
     * @param id
     * @return
     */
    @GET("projectMemberController/queryList")
    Observable<ProjectMemberResultBean> queryProjectMember(@Query("id") long id);

    /**
     * 获取所有的模板
     */
    @GET("projectTemplateController/queryProjectTemplateList")
    Observable<ProjectTempBean> queryProjectTemplateList(@Query("templateRole") int templateRole);

    /**
     * 获取模板的任务分组
     */
    @GET("projectTemplateController/queryAllNode")
    Observable<TaskNoteResultBean> queryTaskNoteByTempId(@Query("tempId") long tempId);

    /**
     * 查询项目企业工作流列表
     */
    @GET("projectWorkflow/queryDataList")
    Observable<WorkFlowResultBean> queryWorkFlow();

    /**
     * 获取工作流及其节点信息
     */
    @GET("projectWorkflow/queryNodesNameById")
    Observable<FlowNodeResultBean> queryFlowNodesById(@Query("id") long flowId);

    /**
     * 移除项目成员
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectMemberController/delete")
    Observable<BaseBean> deleteProjectMember(@Body JSONObject jsonObject);

    /**
     * 添加项目成员
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectMemberController/save")
    Observable<BaseBean> addProjectMember(@Body JSONObject jsonObject);

    /**
     * 取消任务关联
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/cancleRelation")
    Observable<BaseBean> cancleRelation(@Body JSONObject jsonObject);

    /**
     * 取消个人任务关联
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskAssociates/deleteData")
    Observable<BaseBean> canclePersonalRelation(@Body JSONObject jsonObject);

    /**
     * 通过新增添加关联
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/saveRelation")
    Observable<BaseBean> addRelevanceByNew(@Body SaveRelationRequestBean bean);

    /**
     * 个人任务添加关联
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskAssociates/saveData")
    Observable<BaseBean> addRelevanceByPersonal(@Body SavePersonalRelationRequestBean bean);

    /**
     * 通过引用添加关联
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/quoteRelation")
    Observable<BaseBean> addRelevanceByQuote(@Body QuoteRelationRequestBean bean);

    /**
     * 删除任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/delete")
    Observable<BaseBean> delTask(@Body JSONObject jsonObject);

    /**
     * 删除个人任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTask/deleteData")
    Observable<BaseBean> delPersonalTask(@Body JSONObject jsonObject);

    /**
     * 删除个人子任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelSubTask/deleteData")
    Observable<BaseBean> delPersonalSubTask(@Body JSONObject jsonObject);

    /**
     * 移动任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/updateTaskSubNode")
    Observable<BaseBean> moveTask(@Body JSONObject jsonObject);

    /**
     * 复制任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/copyTask")
    Observable<BaseBean> copyTask(@Body JSONObject jsonObject);


    /**
     * 获取时间工作台
     */
    @GET("projectTaskWorkbench/queryTaskWorkbenchList")
    Observable<TimeWorkbenchResultBean> queryTimeWorkbench(@Query("workbench_type") int workbenchType, @Query("workbench_id") int workbench_id);

    /**
     * 时间工作台任务拖动
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskWorkbench/moveTimeWorkbench")
    Observable<BaseBean> moveTimeWorkbench(@Body JSONObject json);

    /**
     * 时间工作台任务排序
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskWorkbench/sortTimeWorkbench")
    Observable<BaseBean> sortTimeWorkbench(@Body JSONObject json);

    /**
     * 获取任务提醒列表
     */
    @GET("projectTaskRemind/getTaskRemindList")
    Observable<TaskRemindResultBean> getTaskRemindList(@Query("taskId") long taskId, @Query("fromType") long fromType);

    /**
     * 新增任务设置提醒数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskRemind/saveData")
    Observable<BaseBean> addTaskRemind(@Body TaskRemindRequestBean bean);

    /**
     * 编辑任务设置提醒数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskRemind/updateData")
    Observable<BaseBean> editTaskRemind(@Body TaskRemindRequestBean bean);


    /**
     * 获取项目任务重复任务设置
     */
    @GET("projectTaskRepeat/getTaskRepeatList")
    Observable<TaskRepeatResultBean> getTaskRepeatList(@Query("taskId") long taskId);

    /**
     * 项目任务设置重复任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskRepeat/saveData")
    Observable<BaseBean> addTaskRepeat(@Body TaskRepeatRequestBean bean);

    /**
     * 项目任务编辑重复任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskRepeat/updateData")
    Observable<BaseBean> editTaskRepeat(@Body TaskRepeatRequestBean bean);


    /**
     * 获取个人任务提醒列表
     */
    @GET("personelTaskRemind/getTaskRemindList")
    Observable<PersonalTaskRemindResultBean> getPersonalTaskRemindList(@Query("taskId") long taskId, @Query("fromType") long fromType);

    /**
     * 新增个人任务设置提醒数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskRemind/saveData")
    Observable<BaseBean> addPersonalTaskRemind(@Body PersonalTaskRemindRequestBean bean);

    /**
     * 编辑个人任务设置提醒数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskRemind/updateData")
    Observable<BaseBean> editPersonalTaskRemind(@Body PersonalTaskRemindRequestBean bean);


    /**
     * 获取个人任务重复任务设置
     */
    @GET("personelTaskRepeat/getTaskRepeatList")
    Observable<TaskRepeatResultBean> getPersonalTaskRepeatList(@Query("taskId") long taskId);

    /**
     * 个人任务设置重复任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskRepeat/saveData")
    Observable<BaseBean> addPersonalTaskRepeat(@Body TaskRepeatRequestBean bean);

    /**
     * 个人任务编辑重复任务
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskRepeat/updateData")
    Observable<BaseBean> editPersonalTaskRepeat(@Body TaskRepeatRequestBean bean);


    /**
     * 获取个人在个人任务中的角色
     */
    @GET("personelTaskMember/getTaskRoleFromPersonelTask")
    Observable<PersonalTaskRoleResultBan> queryPersonalTaskRole(@Query("taskId") long taskId, @Query("fromType") long fromType);


    /**
     * 项目任务--修改协助人可见
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectTaskController/update")
    Observable<BaseBean> updateTaskAssociatesState(@Body JSONObject json);

    /**
     * 个人任务--修改协助人可见
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskMember/updateTask")
    Observable<BaseBean> updatePersonalTaskAssociatesState(@Body JSONObject json);

    /**
     * 项目任务--新增协助人
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectMemberController/saveTaskMember")
    Observable<BaseBean> saveTaskMembers(@Body JSONObject json);

    /**
     * 项目任务--删除协助人
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectMemberController/deleteTaskMember")
    Observable<BaseBean> delTaskMembers(@Body JSONObject json);

    /**
     * 个人任务--修改协助人
     */
    @Headers({"Content-Type: application/json"})
    @POST("personelTaskMember/saveData")
    Observable<BaseBean> updatePersonalTaskMembers(@Body JSONObject json);


    /**
     * 查看任务状态（项目任务）
     */
    @GET("projectTaskController/queryTaskViewStatus")
    Observable<TaskStatusResultBean> queryTaskStatus(@Query("projectId") long projectId, @Query("taskId") long taskId, @Query("taskType") long taskType);

    /**
     * 查看任务状态（个人任务）
     */
    @GET("personelTaskMember/queryTaskViewStatus")
    Observable<PersonalTaskStatusResultBean> queryPersonalTaskStatus(@Query("taskId") long taskId, @Query("fromType") long fromType);

    /**
     * 根据当前登陆人权限获取所有自定义模块（个人任务）
     */
    @GET("personelTask/findAllModuleListByAuth")
    Observable<CustomModuleResultBean> queryAllModule();
}
