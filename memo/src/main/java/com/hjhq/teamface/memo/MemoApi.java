package com.hjhq.teamface.memo;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CommonNewResultBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.common.bean.ProjectListBean;
import com.hjhq.teamface.common.bean.TaskListBean;
import com.hjhq.teamface.memo.bean.AddRelevantBean;
import com.hjhq.teamface.memo.bean.MemoDetailBean;
import com.hjhq.teamface.memo.bean.MemoListBean;
import com.hjhq.teamface.memo.bean.NewMemoBean;
import com.hjhq.teamface.memo.bean.RelevantDataBean;
import com.hjhq.teamface.memo.bean.SearchModuleDataBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/3/26.
 * Describe：
 */

public interface MemoApi {
    /**
     * 添加备忘录
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("memo/save")
    Observable<CommonNewResultBean> saveMemo(@Body NewMemoBean bean);

    /**
     * 添加关联数据
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("memo/updateRelationById")
    Observable<BaseBean> updateRelationById(@Body AddRelevantBean bean);

    /**
     * 获取关联数据详情
     *
     * @param id
     * @return
     */
    @GET("memo/findRelationList")
    Observable<RelevantDataBean> findRelationList(@Query("id") String id);

    /**
     * 编辑备忘录
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("memo/update")
    Observable<BaseBean> updateMemo(@Body NewMemoBean bean);

    /**
     * 删除/退出共享/恢复备忘录
     */
    @Headers({"Content-Type: application/json"})
    @POST("memo/del")
    Observable<BaseBean> memoOperation(@Query("type") String type, @Query("ids") String ids);

    /**
     * 获取备忘录列表
     *
     * @param pageNum
     * @param pageSize
     * @param type
     * @param keyword
     * @return
     */
    @GET("memo/findMemoList")
    Observable<MemoListBean> findMemoList(@Query("pageNum") String pageNum,
                                          @Query("pageSize") String pageSize,
                                          @Query("type") String type,
                                          @Query("keyword") String keyword);

    @GET("memo/findMemoWebList")
    Observable<MemoListBean> findMemoWebList(@Query("type") String type,
                                             @Query("keyword") String keyword);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    @GET("memo/findMemoDetail")
    Observable<MemoDetailBean> findMemoDetail(@Query("id") String id);

    /**
     * 获取模块列表
     *
     * @return
     */
    @GET("module/findAllModuleList")
    Observable<ModuleResultBean> getAllModule(@Query("approvalFlag") String approvalFlag);

    /**
     * 搜索自定义数据
     *
     * @param fieldValue 关键字
     * @return
     */
    @GET("moduleOperation/getFirstFieldFromModule")
    Observable<SearchModuleDataBean> getFirstFieldFromModule(@Query("bean") String bean, @Query("fieldValue") String fieldValue);

    /**
     * 权限查询
     *
     * @param bean
     * @param style
     * @param dataId
     * @return
     */
    @GET("moduleDataAuth/getFuncAuthWithCommunal")
    Observable<ViewDataAuthResBean> queryAuth(@Query("bean") String bean,
                                              @Query("style") String style,
                                              @Query("dataId") String dataId);

    /**
     * 获取项目列表
     */
    @GET("projectController/queryAllList")
    Observable<ProjectListBean> queryAllList(@Query("type") int type, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("keyword") String keyword);

    /**
     * 获取项目任务列表
     */
    @GET("projectTaskController/queryAllTaskList")
    Observable<TaskListBean> queryAllTaskList(@Query("projectId") Long projectId);


}
