package com.hjhq.teamface.api;


import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.DetailResultBean;
import com.hjhq.teamface.basis.bean.FilterFieldResultBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.basis.bean.UpdateModuleBean;
import com.hjhq.teamface.basis.bean.QueryApprovalDataResultBean;
import com.hjhq.teamface.oa.approve.bean.ApproveCopyRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApprovePassRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveRejectRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveResponseBean;
import com.hjhq.teamface.oa.approve.bean.ApproveRevokeRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveTransferRequestBean;
import com.hjhq.teamface.oa.approve.bean.ApproveUnReadCountResponseBean;
import com.hjhq.teamface.oa.approve.bean.PassTypeResponseBean;
import com.hjhq.teamface.oa.approve.bean.ProcessFlowResponseBean;
import com.hjhq.teamface.oa.approve.bean.RejectTypeResponseBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author xj
 */
public interface ApproveApiService {

    /**
     * 得到全部应用模块
     */
    @GET("module/findAllModuleList")
    Observable<ModuleResultBean> getAllModule(@Query("approvalFlag") String approvalFlag);


    /**
     * 编辑应用常用模块
     */
    @Headers({"Content-Type: application/json"})
    @POST("applicationModuleUsed/update")
    Observable<BaseBean> updateModule(@Body UpdateModuleBean moduleIds);

    /**
     * 获取业务信息详情
     */
    @GET("moduleOperation/findDataDetail")
    Observable<DetailResultBean> getDataDetail(@QueryMap Map<String, Object> map);

    @GET("layout/getEnableLayout")
    Observable<CustomLayoutResultBean> getEnableFields(@QueryMap Map<String, Object> map);


    /**
     * 审批列表
     * type 0 我发起的  1待我审批 2 我已审批 3 抄送到我
     */
    @Headers({"Content-Type: application/json"})
    @POST("approval/queryApprovalList")
    Observable<ApproveResponseBean> queryApprovalList(@Body Map<String, Object> map);

    /**
     * 项目引用审批模块下的数据
     */
    @Headers({"Content-Type: application/json"})
    @POST("approval/queryProjectApprovaList")
    Observable<ApproveResponseBean> queryProjectApprovaList(@Body Map<String, Object> map);

    /**
     * 获取筛选条件
     * type 0 我发起的  1待我审批 2 我已审批 3 抄送到我
     */
    @GET("approval/querySearchMenu")
    Observable<FilterFieldResultBean> querySearchMenu(@Query("type") int type);

    /**
     * 获取数量
     */
    @GET("approval/queryApprovalCount")
    Observable<ApproveUnReadCountResponseBean> queryApprovalCount();


    /**
     * 获取完整审批流
     * processInstanceId 流程实例id
     */
    // TODO: 2018/1/12 processDefinitionId参数后面要改成 processInstanceId
    @GET("workflow/getProcessWholeFlow")
    Observable<ProcessFlowResponseBean> getProcessWholeFlow(@QueryMap Map<String, Object> map);

    /**
     * 审批通过
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/pass")
    Observable<BaseBean> approvePass(@Body ApprovePassRequestBean bean);

    /**
     * 审批驳回
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/reject")
    Observable<BaseBean> approveReject(@Body ApproveRejectRequestBean bean);

    /**
     * 审批转交
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/transfer")
    Observable<BaseBean> approveTransfer(@Body ApproveTransferRequestBean bean);

    /**
     * 审批撤销
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/revoke")
    Observable<BaseBean> approveRevoke(@Body ApproveRevokeRequestBean bean);

    /**
     * 抄送
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/ccTo")
    Observable<BaseBean> approveCopy(@Body ApproveCopyRequestBean bean);

    /**
     * 催办
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/urgeTo")
    Observable<BaseBean> approveUrge(@Body Map<String, String> map);

    /**
     * 删除审批申请数据
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("workflow/removeProcessApproval")
    Observable<BaseBean> approveDel(@Body Map<String, String> map);

    /**
     * 获取通过方式
     * map
     */
    @GET("workflow/getPassType")
    Observable<PassTypeResponseBean> getPassType(@QueryMap Map<String, String> map);

    /**
     * 获取驳回方式
     * map
     */
    @GET("workflow/getRejectType")
    Observable<RejectTypeResponseBean> getRejectType(@QueryMap Map<String, String> map);

    /**
     * 获取审批小助手跳转详情参数
     */
    @Headers({"Content-Type: application/json"})
    @POST("approval/queryApprovalData")
    Observable<QueryApprovalDataResultBean> queryApprovalData(@Body Map<String, String> map);

    /**
     * 标记已读
     * bean
     */
    @Headers({"Content-Type: application/json"})
    @POST("approval/approvalRead")
    Observable<BaseBean> approvalRead(@Body Map<String, String> map);
}
