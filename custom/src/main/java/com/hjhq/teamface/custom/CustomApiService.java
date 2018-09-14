package com.hjhq.teamface.custom;


import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.DataListRequestBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.EmailListBean;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.bean.ModuleBean;
import com.hjhq.teamface.basis.bean.ModuleFunctionBean;
import com.hjhq.teamface.basis.bean.ReferDataTempResultBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.common.bean.AddCommentRequestBean;
import com.hjhq.teamface.common.bean.CommentDetailResultBean;
import com.hjhq.teamface.common.bean.DynamicListResultBean;
import com.hjhq.teamface.custom.bean.AddOrEditShareRequestBean;
import com.hjhq.teamface.custom.bean.AutoModuleResultBean;
import com.hjhq.teamface.custom.bean.DataRelationResultBean;
import com.hjhq.teamface.custom.bean.FindAutoByBean;
import com.hjhq.teamface.custom.bean.RelationDataRequestBean;
import com.hjhq.teamface.custom.bean.RepeatCheckResponseBean;
import com.hjhq.teamface.custom.bean.SaveCustomDataRequestBean;
import com.hjhq.teamface.custom.bean.SeasPoolResponseBean;
import com.hjhq.teamface.custom.bean.ShareResultBean;
import com.hjhq.teamface.custom.bean.TempMenuResultBean;
import com.hjhq.teamface.custom.bean.TransformationRequestBean;
import com.hjhq.teamface.custom.bean.TransformationResultBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author xj
 */
public interface CustomApiService {

    /**
     * 获取自定义布局
     */
    @GET("layout/getEnableLayout")
    Observable<CustomLayoutResultBean> getEnableFields(@QueryMap Map<String, Object> map);


    /**
     * 保存新增业务信息
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/saveData")
    Observable<ModuleBean> saveCustomData(@Body SaveCustomDataRequestBean bean);


    /**
     * 修改业务信息
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/updateData")
    Observable<BaseBean> updateCustomData(@Body SaveCustomDataRequestBean bean);

    /**
     * 获取业务信息列表
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/findDataList")
    Observable<DataTempResultBean> getDataTemp(@Body DataListRequestBean bean);

    /**
     * 查重
     */
    @GET("moduleOperation/getRecheckingFields")
    Observable<RepeatCheckResponseBean> getRecheckingFields(@Query("bean") String bean, @Query("field") String field, @Query("value") String value);

    /**
     * 获取全部子菜单
     */
    @GET("submenu/getSubmenus")
    Observable<TempMenuResultBean> getMenuList(@Query("moduleId") String moduleId);

    /**
     * 获取邮件相关模块的子菜单
     */
    @GET("moduleEmail/getModuleSubmenus")
    Observable<TempMenuResultBean> getModuleSubmenus(@Query("moduleId") String moduleId);

    /**
     * 获取筛选字段
     */
    @GET("moduleOperation/findFilterFields")
    Observable<FilterFieldResultBean> getFilterFields(@Query("bean") String bean);


    /**
     * 获取业务信息详情
     */
    @GET("moduleOperation/findDataDetail")
    Observable<DetailResultBean> getDataDetail(@QueryMap Map<String, Object> map);


    /**
     * 获取业务数据关联关系
     */
    @GET("moduleOperation/findDataRelation")
    Observable<DataRelationResultBean> getDataRelation(@Query("id") String id, @Query("bean") String bean);

    /**
     * 获取所有自动化模块
     */
    @GET("automatch/findAllModule")
    Observable<AutoModuleResultBean> getAutoModule(@Query("bean") String bean);

    /**
     * 根据bean获取自动匹配列表
     */
    @GET("automatch/findAutoByBean")
    Observable<FindAutoByBean> findAutoByBean(@Query("sorceBean") String sorceBean, @Query("targetBean") String targetBean);

    /**
     * 根据bean获取自动匹配列表
     */
    @GET("automatch/findAutoList")
    Observable<DataTempResultBean> findAutoList(@Query("dataId") String dataId,@Query("ruleId") long ruleId,  @Query("sorceBean") String sorceBean,
                                              @Query("targetBean") String targetBean,
                                              @Query("pageNum") int pageNum,
                                              @Query("pageSize") int pageSize);

    /**
     * 添加评论
     */
    @Headers({"Content-Type: application/json"})
    @POST("common/savaCommonComment")
    Observable<BaseBean> addComment(@Body AddCommentRequestBean bean);

    /**
     * 获取评论详情
     */
    @GET("common/queryCommentDetail")
    Observable<CommentDetailResultBean> getCommentDetail(@Query("id") String id, @Query("bean") String bean);

    /**
     * 获取动态 (APP专属)
     */
    @GET("common/queryAppDynamicList")
    Observable<DynamicListResultBean> getDynamicList(@Query("id") String id, @Query("bean") String bean);

    /**
     * 关联组件 列表
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/findRelationDataList")
    Observable<ReferDataTempResultBean> findRelationDataList(@Body RelationDataRequestBean bean);


    /**
     * 自定义上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/applyFileUpload")
    Observable<UpLoadFileResponseBean> uploadApplyFile(@PartMap Map<String, RequestBody> fileList);


    /**
     * 评论上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/upload")
    Observable<UpLoadFileResponseBean> uploadFile(@PartMap Map<String, RequestBody> fileList);


    /**
     * 获取模块功能权限
     *
     * @return
     */
    @GET("moduleDataAuth/getFuncAuthByModule")
    Observable<ModuleFunctionBean> getModuleFunctionAuth(@Query("bean") String moduleBean);


    /**
     * 转移责任人
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/transfor")
    Observable<BaseBean> transfor(@Body Map<String, String> map);

    /**
     * 复制
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/copy")
    Observable<DetailResultBean> copy(@Body Map<String, String> map);

    /**
     * 删除
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/del")
    Observable<BaseBean> del(@Body Map<String, String> map);

    /**
     * 共享设置删除
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleSingleShare/del")
    Observable<BaseBean> delShare(@Body Map<String, String> map);

    /**
     * 保存共享设置
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleSingleShare/save")
    Observable<BaseBean> saveShare(@Body AddOrEditShareRequestBean bean);

    /**
     * 保存共享设置
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleSingleShare/update")
    Observable<BaseBean> editShare(@Body AddOrEditShareRequestBean bean);

    /**
     * 根据模块数据ID查它的共享设置
     *
     * @return
     */
    @GET("moduleSingleShare/getSingles")
    Observable<ShareResultBean> getSingleShare(@Query("dataId") String dataId);

    /**
     * 根据模块bean 查询 转换 列表
     *
     * @return
     */
    @GET("fieldTransform/getFieldTransformationsForMobile")
    Observable<TransformationResultBean> getFieldTransformations(@Query("bean") String bean);


    /**
     * 转换
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/transformation")
    Observable<BaseBean> transformations(@Body TransformationRequestBean bean);

    /**
     * 领取
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/take")
    Observable<BaseBean> take(@Body Map<String, String> map);

    /**
     * 退回公海池
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/returnBack")
    Observable<BaseBean> returnBack(@Body Map<String, String> map);

    /**
     * 公海池分配
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/allocate")
    Observable<BaseBean> allocate(@Body Map<String, String> map);

    /**
     * 移动公海池
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/moveData2OtherSeapool")
    Observable<BaseBean> moveSeapool(@Body Map<String, String> map);

    /**
     * 获取公海池列表
     *
     * @return
     */
    @GET("seapool/getSeapools")
    Observable<SeasPoolResponseBean> getSeapools(@Query("bean") String bean);


    /**
     * 自定义详情获取邮件列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GET("mailOperation/queryMailListByAccount")
    Observable<EmailListBean> getEmailListyAccount(
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize,
            @Query("accountName") String accountName
    );

    /**
     * 生成条形码
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("barcode/createBarcode")
    Observable<BaseBean> createBarcode(@Body Map<String, String> map);

    /**
     * 根据条形码获取数据详情
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("barcode/findDetailByBarcode")
    Observable<BaseBean> findDetailByBarcode(@Body Map<String, String> map);

    /**
     * 获取条形码图片信息
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("barcode/getBarcodeMsg")
    Observable<BaseBean> getBarcodeMsg(@Body Map<String, String> map);


}
