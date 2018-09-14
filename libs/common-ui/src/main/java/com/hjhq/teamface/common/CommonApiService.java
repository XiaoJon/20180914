package com.hjhq.teamface.common;

import com.alibaba.fastjson.JSONObject;
import com.hjhq.teamface.basis.bean.AddGroupChatReqBean;
import com.hjhq.teamface.basis.bean.AddGroupChatResponseBean;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.bean.ModuleAuthBean;
import com.hjhq.teamface.basis.bean.ModuleFunctionBean;
import com.hjhq.teamface.basis.bean.QueryBarcodeDataBean;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthBean;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.common.bean.AddCommentRequestBean;
import com.hjhq.teamface.common.bean.CommentDetailResultBean;
import com.hjhq.teamface.common.bean.DynamicListResultBean;
import com.hjhq.teamface.common.bean.LinkageFieldsResultBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 通用
 * Created by Administrator on 2018/3/23.
 */

public interface CommonApiService {

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
     * 评论上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/upload")
    Observable<UpLoadFileResponseBean> uploadFile(@PartMap Map<String, RequestBody> fileList);

    /**
     * 获取员工
     */
    @GET("employee/selectEmployeeList")
    Observable<EmployeeResultBean> getEmployee(@Query("userName") String userName);

    /**
     * 获取选择范围内的员工
     */
    @Headers({"Content-Type: application/json"})
    @POST("employee/queryRangeEmployeeList")
    Observable<EmployeeResultBean> queryRangeEmployeeList(@Body Map<String, List<Member>> map);

    /**
     * 人员模糊搜索
     *
     * @param departmentId 不传查询全公司
     * @param employeeName
     */
    @GET("user/findByUserName")
    Observable<EmployeeResultBean> findByUserName(@Query("department_id") String departmentId, @Query("employee_name") String employeeName);

    /**
     * 获取公司人员架构
     *
     * @param companyId
     * @return
     */
    @GET("user/findUsersByCompany")
    Observable<GetDepartmentStructureBean> findUsersByCompany(@Query("companyId") String companyId);

    /**
     * 获取角色
     *
     * @return
     */
    @GET("moduleDataAuth/getRoleGroupList")
    Observable<RoleGroupResponseBean> getRoleGroupList();

    /**
     * 获取部门
     *
     * @return
     */
    @POST("employee/queryRangeDepartmentList")
    Observable<EmployeeResultBean> queryRangeDepartmentList(@Body Map chooseRange);


    /**
     * 获取模块功能权限
     *
     * @return
     */
    @GET("moduleDataAuth/getFuncAuthByModule")
    Observable<ModuleFunctionBean> getModuleFunctionAuth(@Query("bean") String moduleBean);

    /**
     * 验证是否有功能权限
     *
     * @param bean
     * @return
     */
    @GET("moduleDataAuth/getAuthByBean")
    Observable<ViewDataAuthBean> getAuthByBean(@Query("bean") String bean, @Query("dataId") String dataId);

    /**
     * 验证是否有模块的权限
     *
     * @param bean
     * @return
     */
    @GET("moduleDataAuth/getAuthByModule")
    Observable<ModuleAuthBean> getAuthByModule(@Query("bean") String bean);

    /**
     * 创建个人聊天
     *
     * @param receiverId
     * @return
     */
    @GET("imChat/addSingleChat")
    Observable<AddSingleChatResponseBean> addSingleChat(@Query("receiverId") String receiverId);

    /**
     * 创建群
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/addGroupChat")
    Observable<AddGroupChatResponseBean> addGroupChat(@Body AddGroupChatReqBean bean);

    /**
     * 扫码登录
     *
     * @param map
     */
    @Headers({"Content-Type: application/json"})
    @POST("user/valiLogin")
    Observable<BaseBean> valiLogin(@Body Map<String, String> map);

    /**
     * 根据条形码获取数据详情
     *
     * @return
     */
    @GET("barcode/findDetailByBarcode")
    Observable<QueryBarcodeDataBean> findDetailByBarcode(@Query("barcodeValue") String barcodeValue);


    /**
     * 获取联动字段
     */
    @GET("layout/getLinkageFieldsForCustom")
    Observable<LinkageFieldsResultBean> getLinkageFields(@Query("bean") String bean);
    /**
     * 联动字段变更请求相应数据
     *
     * @param jsonObject
     */
    @Headers({"Content-Type: application/json"})
    @POST("aggregationLinkage/findAggregationDataLinkageList")
    Observable<DetailResultBean> getLinkageData(@Body JSONObject jsonObject);
}
