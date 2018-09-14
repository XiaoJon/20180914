package com.hjhq.teamface.project;

import com.hjhq.teamface.basis.bean.BaseBean;
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
import com.hjhq.teamface.common.bean.TaskListBean;

import java.util.Map;

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
public interface ProjectApiService2 {

    /**
     * 获取项目设置详情
     */
    @GET("projectSettingController/queryById")
    Observable<ProjectInfoBean> getProjectSettingDetail(@Query("id") long id);

    /**
     * 获取任务权限
     *
     * @param id
     * @return
     */
    @GET("projectSettingController/queryTaskAuthList")
    Observable<BaseBean> getProjectTaskAuth(@Query("id") String id);

    /**
     * 项目标签列表
     *
     * @param id
     * @return
     */
    @GET("projectSettingController/queryLabelsList")
    Observable<ProjectLabelsListBean> getProjectLabel(@Query("id") long id, @Query("type") int type, @Query("keyword") String keyword);

    /**
     * 获取全部标签列表
     *
     * @param id
     * @return
     */
    @GET("projectManagementTagController/queryAllTagList")
    Observable<ProjectLabelsListBean> queryAllLabel(@Query("id") long id, @Query("type") int type, @Query("keyword") String keyword);

    /**
     * 为项目添加标签
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectSettingController/addProjectLabel")
    Observable<ProjectLabelsListBean> addprojectLabel(@Body Map<String, String> map);

    /**
     * 移除项目标签
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectSettingController/removeProjectLabel")
    Observable<ProjectLabelsListBean> removeProjectLabel(@Body Map<String, String> map);

    /**
     * 新增项目
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectController/save")
    Observable<BaseBean> createProject(@Body NewProjectBean bean);

    /**
     * 保存项目信息
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectSettingController/saveInformation")
    Observable<BaseBean> saveProjectInfo(@Body EditProjectBean bean);


    /**
     * 保存项目设置
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectSettingController/saveSetting")
    Observable<BaseBean> saveProjectSetting(@Body ProjectSettingBean bean);

    /**
     * 项目状态变更
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectSettingController/editStatus")
    Observable<BaseBean> editProjectStatus(@Body Map<String, String> map);

    /**
     * 获取项目成员
     *
     * @param id
     * @return
     */
    @GET("projectMemberController/queryList")
    Observable<ProjectMemberResultBean> queryProjectMember(@Query("id") long id);

    /**
     * 项目标签变更
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectSettingController/editLabels")
    Observable<BaseBean> editProjectLabel(@Body Map<String, String> map);

    /**
     * 新增分享
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/save")
    Observable<BaseBean> addProjectShare(@Body ProjectAddShareBean bean);

    /**
     * 修改分享
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/edit")
    Observable<BaseBean> editProjectShare(@Body ProjectAddShareBean bean);

    /**
     * 删除分享
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/delete")
    Observable<BaseBean> deleteProjectShare(@Body Map<String, String> map);

    /**
     * 取消关联
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/cancleRelation")
    Observable<BaseBean> cancleRelation(@Body Map<String, Long> map);

    /**
     * 关联变更
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/editRelevance")
    Observable<BaseBean> editRelevance(@Body Map<String, String> map);

    /**
     * 添加关联
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/saveRelation")
    Observable<BaseBean> saveRelation(@Body AddRelevantBean bean);

    /**
     * 分享置顶/取消分享置顶
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/shareStick")
    Observable<BaseBean> editShareStickStatus(@Body Map<String, Long> map);

    /**
     * 分享点赞
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectShareController/sharePraise")
    Observable<BaseBean> sharePraise(@Body Map<String, Long> map);

    /**
     * 获取分享详情
     *
     * @param id
     * @return
     */
    @GET("projectShareController/queryById")
    Observable<ProjectShareDetailBean> getProjectShareDetail(@Query("id") String id);

    /**
     * 获取关联数据
     *
     * @param id
     * @return
     */
    @GET("projectShareController/queryRelationList")
    Observable<TaskListBean> queryRelationList(@Query("id") String id);

    /**
     * @param
     * @return
     */
    @GET("projectShareController/queryList")
    Observable<ProjectShareListBean> getProjectShareList(@Query("pageNum") int pageNum,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("keyword") String keyword,
                                                         @Query("type") int type,
                                                         @Query("projectId") Long projectId);

    /**
     * 搜索分享
     *
     * @param projectId
     * @param keyword
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GET("projectShareController/queryWebList")
    Observable<ProjectShareListBean> searchProjectShareList(
            @Query("projectId") Long projectId,
            @Query("keyword") String keyword,
            @Query("type") int type,
            @Query("pageSize") int pageSize,
            @Query("pageNum") int pageNum);


    /**
     * 文库分组列表
     *
     * @param id 项目id
     * @return
     */
    @GET("projectLibrary/queryLibraryList")
    Observable<ProjectFolderListBean> getProjectFolderList(@Query("id") long id,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("pageNum") int pageNum);

    /**
     * 列表(二级)
     *
     * @param id
     * @return
     */
    @GET("projectLibrary/queryFileLibraryList")
    Observable<ProjectFolderListBean> queryFileLibraryList(@Query("id") String id,
                                                           @Query("library_type") String library_type,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("pageNum") int pageNum
    );

    /**
     * 文库任务列表
     *
     * @param id           项目id
     * @param library_type 文件夹类型
     * @return
     */
    @GET("projectLibrary/queryTaskLibraryList")
    Observable<ProjectFileListBean> getProjectFolderTaskList(@Query("id") String id,
                                                             @Query("library_type") String library_type,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("pageNum") int pageNum);

    /**
     * 搜索文件
     *
     * @param id
     * @param library_type
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GET("projectLibrary/queryTaskLibraryList")
    Observable<ProjectFileListBean> getProjectFolderTaskList(@Query("id") String id,
                                                             @Query("library_type") String library_type,
                                                             @Query("keyWord") String keyword,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("pageNum") int pageNum);

    /**
     * 添加项目文件夹
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectLibrary/savaFileLibrary")
    Observable<BaseBean> addProjectFolder(@Body Map<String, String> map);

    /**
     * 修改项目文件夹
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectLibrary/editLibrary")
    Observable<BaseBean> editProjectFolder(@Body Map<String, String> map);

    /**
     * 删除项目文件夹
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectLibrary/delLibrary")
    Observable<BaseBean> deleteProjectFolder(@Body Map<String, String> map);

    /**
     * 项目文件上传(不在此处使用)
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("common/file/projectUpload")
    Observable<BaseBean> uploadProjectFile();

    /**
     * 分享文件
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("projectLibrary/sharLibrary")
    Observable<BaseBean> shareLibrary(@Body Map<String, String> map);


    /**
     * 项目文件下载(不在此处使用)
     *
     * @return
     */
    @GET("common/file/projectDownload")
    Observable<BaseBean> downloadProjectFile();
}
