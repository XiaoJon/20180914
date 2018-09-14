package com.hjhq.teamface.project.model;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.project.ProjectApiService;
import com.hjhq.teamface.project.ProjectApiService2;
import com.hjhq.teamface.project.bean.AddRelevantBean;
import com.hjhq.teamface.project.bean.EditProjectBean;
import com.hjhq.teamface.project.bean.NewProjectBean;
import com.hjhq.teamface.project.bean.ProjectAddShareBean;
import com.hjhq.teamface.project.bean.ProjectFileListBean;
import com.hjhq.teamface.project.bean.ProjectFolderListBean;
import com.hjhq.teamface.project.bean.ProjectInfoBean;
import com.hjhq.teamface.project.bean.ProjectLabelsListBean;
import com.hjhq.teamface.project.bean.ProjectMemberResultBean;
import com.hjhq.teamface.project.bean.ProjectSettingBean;
import com.hjhq.teamface.project.bean.ProjectShareDetailBean;
import com.hjhq.teamface.project.bean.ProjectShareListBean;
import com.hjhq.teamface.project.bean.QueryManagerRoleResultBean;
import com.hjhq.teamface.project.bean.QueryTaskAuthResultBean;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */

public class ProjectModel2 implements IModel<ProjectApiService2> {
    @Override
    public ProjectApiService2 getApi() {
        return new ApiManager<ProjectApiService2>().getAPI(ProjectApiService2.class);
    }

    /**
     * 获取项目设置详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getProjectSettingDetail(ActivityPresenter mActivity, long id, Subscriber<ProjectInfoBean> s) {
        getApi().getProjectSettingDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目任务权限
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getProjectTaskAuth(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        getApi().getProjectTaskAuth(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 保存项目信息
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveProjectInfo(ActivityPresenter mActivity, EditProjectBean bean, Subscriber<BaseBean> s) {
        getApi().saveProjectInfo(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目标签列表
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getProjectLabel(ActivityPresenter mActivity, long id, int type, String keyword, Subscriber<ProjectLabelsListBean> s) {
        getApi().getProjectLabel(id, type, keyword).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目标签列表
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryAllLabel(ActivityPresenter mActivity, long id, int type, Subscriber<ProjectLabelsListBean> s) {
        getApi().queryAllLabel(id, type, "").map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 为项目添加标签
     *
     * @param mActivity
     * @param id
     * @param labelIds  标签id字符串
     * @param s
     */
    public void addProjectLabel(ActivityPresenter mActivity, long id, String labelIds, Subscriber<ProjectLabelsListBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("ids", labelIds);
        getApi().addprojectLabel(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 移除标签
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void removeProjectLabel(ActivityPresenter mActivity, long id, String labelId, Subscriber<ProjectLabelsListBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("ids", labelId);
        getApi().removeProjectLabel(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增项目
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void createProject(ActivityPresenter mActivity, NewProjectBean bean, Subscriber<BaseBean> s) {
        getApi().createProject(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 保存项目设置
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveProjectSetting(ActivityPresenter mActivity, ProjectSettingBean bean, Subscriber<BaseBean> s) {
        getApi().saveProjectSetting(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目状态变更
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void editProjectStatus(ActivityPresenter mActivity, Map<String, String> map, Subscriber<BaseBean> s) {
        getApi().editProjectStatus(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目标签变更
     *
     * @param mActivity
     * @param projectId 项目id
     * @param labelsId  当前项目标签id以","分割的字符串
     * @param s
     */
    public void editProjectLabel(ActivityPresenter mActivity, String projectId, String labelsId, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", projectId);
        map.put("ids", labelsId);
        getApi().editProjectLabel(map).map(new HttpResultFunc<>())
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
     * 修改分享
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void editProjectShare(ActivityPresenter mActivity, ProjectAddShareBean map, Subscriber<BaseBean> s) {
        getApi().editProjectShare(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增分享
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void addProjectShare(ActivityPresenter mActivity, ProjectAddShareBean map, Subscriber<BaseBean> s) {
        getApi().addProjectShare(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取分享详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getProjectShareDetail(ActivityPresenter mActivity, String id, Subscriber<ProjectShareDetailBean> s) {
        getApi().getProjectShareDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取分享关联列表
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryRelationList(ActivityPresenter mActivity, String id, Subscriber<TaskListBean> s) {
        getApi().queryRelationList(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 项目分享列表(搜索)
     *
     * @param mActivity
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param type
     * @param s
     */
    public void getProjectShareList(ActivityPresenter mActivity, int pageNum,
                                    int pageSize, String keyword, int type, Long projectId,
                                    Subscriber<ProjectShareListBean> s) {
        getApi().getProjectShareList(pageNum, pageSize, keyword, type, projectId).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 搜索分享
     *
     * @param mActivity
     * @param keyword
     * @param type
     * @param s
     */
    public void searchProjectShareList(ActivityPresenter mActivity, Long projectId, String keyword, int type,
                                       Subscriber<ProjectShareListBean> s) {
        getApi().searchProjectShareList(projectId, keyword, type, 100, 1).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 置顶(不置顶)
     *
     * @param mActivity
     * @param id
     * @param status
     * @param s
     */
    public void editShareStickStatus(ActivityPresenter mActivity, String id, String status, Subscriber<BaseBean> s) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", TextUtil.parseLong(id));
        map.put("status", TextUtil.parseLong(status));
        getApi().editShareStickStatus(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 关联变更
     *
     * @param mActivity
     * @param id
     * @param status
     * @param s
     */
    public void editRelevance(ActivityPresenter mActivity, String id, String status, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        getApi().editRelevance(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 分享添加关联
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveRelation(ActivityPresenter mActivity, AddRelevantBean bean, Subscriber<BaseBean> s) {
        getApi().saveRelation(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 点赞/取消点赞
     *
     * @param mActivity
     * @param id
     * @param status
     * @param s
     */
    public void sharePraise(ActivityPresenter mActivity, String id, String status, Subscriber<BaseBean> s) {
        //0不点赞  1点赞
        Map<String, Long> map = new HashMap<>();
        map.put("id", TextUtil.parseLong(id));
        map.put("status", TextUtil.parseLong(status));
        getApi().sharePraise(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除分享
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void deleteProjectShare(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().deleteProjectShare(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 取消分享中的关联
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void cancleRelation(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", TextUtil.parseLong(id));
        getApi().cancleRelation(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取项目文件夹列表
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getProjectFolderList(ActivityPresenter mActivity, long id, int pageSize, int pageNum, Subscriber<ProjectFolderListBean> s) {

        getApi().getProjectFolderList(id, pageSize, pageNum).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 二级文件夹列表
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void queryFileLibraryList(ActivityPresenter mActivity, String id, String type, int pageSize, int pageNum, Subscriber<ProjectFolderListBean> s) {

        getApi().queryFileLibraryList(id, type, pageSize, pageNum).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取任务的文件夹
     *
     * @param mActivity
     * @param id
     * @param type
     * @param s
     */
    public void getProjectFolderTaskList(ActivityPresenter mActivity, String id, String type, int pageSize, int pageNum,
                                         Subscriber<ProjectFileListBean> s) {

        getApi().getProjectFolderTaskList(id, type, pageSize, pageNum).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 搜索文件
     *
     * @param mActivity
     * @param id
     * @param type
     * @param keyword
     * @param s
     */
    public void searchProjectFile(ActivityPresenter mActivity, String id, String type, String keyword,
                                  Subscriber<ProjectFileListBean> s) {

        int pageSize = 1000;
        int pageNum = 1;
        getApi().getProjectFolderTaskList(id, type, keyword, pageSize, pageNum).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加项目文件夹
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void addProjectFolder(ActivityPresenter mActivity, Map<String, String> id, Subscriber<BaseBean> s) {

        getApi().addProjectFolder(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 编辑项目文件夹
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void editProjectFolder(ActivityPresenter mActivity, Map<String, String> id, Subscriber<BaseBean> s) {

        getApi().editProjectFolder(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除文件夹
     *
     * @param mActivity
     * @param folderId
     * @param s
     */
    public void deleteProjectFolder(ActivityPresenter mActivity, String folderId, String projectId,
                                    String folderName, Subscriber<BaseBean> s) {
        Map<String, String> id = new HashMap<>();
        id.put("id", folderId);
        id.put("project_id", projectId);
        id.put("name", folderName);
        getApi().deleteProjectFolder(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 分享文件
     *
     * @param mActivity
     * @param id
     * @param employeeIds
     * @param s
     */
    public void shareProjectFile(ActivityPresenter mActivity, String id, String employeeIds,
                                 Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("employee_id", employeeIds);
        getApi().shareLibrary(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

}
