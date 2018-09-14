package com.hjhq.teamface.email;

import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.DataListRequestBean;
import com.hjhq.teamface.basis.bean.DataTempResultBean;
import com.hjhq.teamface.basis.bean.EmailListBean;
import com.hjhq.teamface.basis.bean.ModuleResultBean;
import com.hjhq.teamface.email.bean.CheckNextApprovalResultBean;
import com.hjhq.teamface.email.bean.EmailAccountListBean;
import com.hjhq.teamface.email.bean.EmailContactsListBean;
import com.hjhq.teamface.email.bean.EmailDetailBean;
import com.hjhq.teamface.email.bean.EmailFromModuleDataBean;
import com.hjhq.teamface.email.bean.EmailRecentContactsListBean;
import com.hjhq.teamface.email.bean.EmailUnreadNumBean;
import com.hjhq.teamface.email.bean.MolduleListBean;
import com.hjhq.teamface.email.bean.NewEmailBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/9/28.
 * Describe：
 */
public interface EmailApiService {
    /**
     * 发送邮件
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/send")
    Observable<BaseBean> sendEmail(@Body NewEmailBean bean);

    /**
     * 回复
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/mailReply")
    Observable<BaseBean> mailReply(@Body NewEmailBean bean);

    /**
     * 转发
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/mailForward")
    Observable<BaseBean> mailForward(@Body NewEmailBean bean);

    /**
     * 保存到草稿
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/saveToDraft")
    Observable<BaseBean> saveToDraft(@Body NewEmailBean bean);

    /**
     * 编辑后再次保存
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/editDraft")
    Observable<BaseBean> editDraft(@Body NewEmailBean bean);

    /**
     * 手动发送
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/manualSend")
    Observable<BaseBean> manualSend(@Body Map<String, String> bean);

    /**
     * 标记已读未读
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/markMailReadOrUnread")
    Observable<BaseBean> markMailReadOrUnread(@Body Map<String, String> bean);

    /**
     * 收件箱全部标记已读
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/markAllRead")
    Observable<BaseBean> markAllRead(@Body Map<String, String> bean);

    /**
     * 移动邮件
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/moveToBox")
    Observable<BaseBean> moveToBox(@Body Map<String, String> bean);

    /**
     * 删除草稿???????
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/deleteDraft")
    Observable<BaseBean> deleteDraft(@Body Map<String, String> bean);

    /**
     * 标记不是垃圾邮件???
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/markNotTrash")
    Observable<BaseBean> markNotTrash(@Body Map<String, String> bean);


    /**
     * 删除箱、草稿箱彻底删除???
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/clearMail")
    Observable<BaseBean> clearMail(@Body Map<String, String> bean);

    /**
     * 收件箱拒收
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("mailOperation/refuseReceive")
    Observable<BaseBean> refuseReceive(@Body Map<String, String> bean);

    /**
     * 获取个人有效邮箱账号
     *
     * @return
     */
    @GET("mailAccount/queryPersonnelAccount")
    Observable<EmailAccountListBean> queryPersonnelAccount();


    /**
     * 获取邮箱最近联系人
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GET("mailContact/queryList")
    Observable<EmailRecentContactsListBean> getRecentEmailContacts(@Query("pageSize") int pageSize, @Query("pageNum") int pageNum);

    /**
     * 收信
     *
     * @return
     */
    @GET("mailOperation/receive")
    Observable<EmailListBean> receive();

    /**
     * 获取不同邮箱的未读数
     *
     * @return
     */
    @GET("mailOperation/queryUnreadNumsByBox")
    Observable<EmailUnreadNumBean> queryUnreadNumsByBox();

    /**
     * 获取邮件列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GET("mailOperation/queryList")
    Observable<EmailListBean> getEmailList(
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize,
            @Query("accountId") String accountId,
            @Query("boxId") String boxId
    );


    /**
     * 邮件收信
     *
     * @return
     */
    @GET("mailOperation/receive")
    Observable<EmailListBean> receiveEmail();

    /**
     * 邮件详情
     *
     * @param id   邮件ID
     * @param type 1 邮箱邮件详情，2标签邮件详情
     * @return
     */
    @GET("mailOperation/queryById")
    Observable<EmailDetailBean> getEmailDetail(@Query("id") String id, @Query("type") String type);

    /**
     * 邮箱通讯类
     *
     * @param pageSize 分页大小
     * @param pageNum  页码
     * @return
     */
    @GET("mailCatalog/queryList")
    Observable<EmailContactsListBean> getEmailContacts(@Query("pageSize") int pageSize, @Query("pageNum") int pageNum);

    /**
     * 获取应用和模块
     *
     * @param approvalFlag
     * @return
     */
    @GET("module/findAllModuleList")
    Observable<ModuleResultBean> getAllModule(@Query("approvalFlag") String approvalFlag);

    /**
     * 获取有邮件组件的模块列表
     *
     * @return
     */
    @GET("moduleEmail/getModuleEmails")
    Observable<MolduleListBean> getModuleEmails();


    /**
     * 获取业务信息列表
     */
    @Headers({"Content-Type: application/json"})
    @POST("moduleOperation/findDataList")
    Observable<DataTempResultBean> getDataTemp(@Body DataListRequestBean bean);


    /**
     * 判断是否需要选择下一审批人
     * moduleBean
     */
    @GET("workflow/checkChooseNextApproval")
    Observable<CheckNextApprovalResultBean> checkChooseNextApproval(@Query("moduleBean") String moduleBean);


    /**
     * 获取邮件相关模块详情
     */
    @GET("moduleEmail/getEmailFromModuleDetail")
    Observable<EmailFromModuleDataBean> getEmailFromModuleDetail(@Query("bean") String bean, @Query("ids") String ids);

}
