package com.hjhq.teamface.im;

import com.hjhq.teamface.basis.bean.AddGroupChatReqBean;
import com.hjhq.teamface.basis.bean.AddGroupChatResponseBean;
import com.hjhq.teamface.basis.bean.AddSingleChatResponseBean;
import com.hjhq.teamface.basis.bean.AppListResultBean;
import com.hjhq.teamface.basis.bean.AppModuleResultBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.EmployeeCardDetailBean;
import com.hjhq.teamface.basis.bean.EmployeeDetailBean;
import com.hjhq.teamface.basis.bean.EmployeeResultBean;
import com.hjhq.teamface.basis.bean.GetDepartmentStructureBean;
import com.hjhq.teamface.basis.bean.QueryApprovalDataResultBean;
import com.hjhq.teamface.basis.bean.RoleGroupResponseBean;
import com.hjhq.teamface.basis.bean.UpLoadFileResponseBean;
import com.hjhq.teamface.basis.bean.UserInfoBean;
import com.hjhq.teamface.basis.bean.ViewDataAuthResBean;
import com.hjhq.teamface.common.bean.FriendsListBean;
import com.hjhq.teamface.im.bean.AddFriendsCommentResponseBean;
import com.hjhq.teamface.im.bean.AddFriendsRequestBean;
import com.hjhq.teamface.im.bean.AppAssistantInfoBean;
import com.hjhq.teamface.im.bean.AppAssistantListBean;
import com.hjhq.teamface.im.bean.ConversationListBean;
import com.hjhq.teamface.im.bean.GetGroupListBean;
import com.hjhq.teamface.im.bean.GroupChatInfoBean;
import com.hjhq.teamface.im.bean.ModuleBean;
import com.hjhq.teamface.im.bean.SingleChatInfoBean;

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
 * Created by Ked on 2017/5/8.
 * tel:13163739593
 * <p>
 * 此接口用于定义同事圈相关的数据请求
 * <p>
 * 请求头说明
 * 名称	            是否必须	    类型	    默认值	备注
 * TOKEN	             true	    long		    访问令牌
 * COMPANYID  	    true	    long		    公司Id
 * USERID	            true	    long		    用户Id
 * EMPLOYEEID	         true	    long		    员工Id
 * CLIENT_FLAG	    true	    int		        0：其他1：Android客户端2：IOS客户端3:Windows客户端4：Mac客户端
 * CLIENT_VERSION	    true	    string		    APP版本号
 * CLIENT_ID	        true	    string		    appKey(设备号)
 */

public interface ImApiService {

    /**
     * 获取企业圈列表
     *
     * @return
     */
    @GET("imCircle/list")
    Observable<FriendsListBean> getFriends(@Query("isPerson") String isPerson, @Query("pageNo") Integer pageNo, @Query("pageSize") Integer pageSize);

    /**
     * 企业圈点赞
     *
     * @return
     */
    @GET("imCircle/up")
    Observable<BaseBean> likeFriends(@Query("circleMainId") String circleMainId);

    /**
     * 删除企业圈
     *
     * @return
     */
    @GET("imCircle/delete")
    Observable<BaseBean> delFriends(@Query("circleMainId") String circleMainId);

    /**
     * 设置企业圈背景
     *
     * @return
     */
    @GET("imCircle/backGround/mod")
    Observable<BaseBean> modBackGround(@Query("url") String url);


    /**
     * 新增企业圈
     */

    @Headers({"Content-Type: application/json"})
    @POST("imCircle/add")
    Observable<BaseBean> addFriends(@Body AddFriendsRequestBean bean);

    /**
     * 评论企业圈
     */
    @Headers({"Content-Type: application/json"})
    @POST("imCircle/comment")
    Observable<AddFriendsCommentResponseBean> commentFriends(@Body Map<String, String> map);


    /**
     * 删除评论
     */
    @GET("imCircle/comment/delete")
    Observable<BaseBean> delComment(@Query("commentId") String commentId);


    /**
     * 获取员工
     */
    @GET("employee/selectEmployeeList")
    Observable<EmployeeResultBean> getEmployee(@Query("userName") String userName);

   /* *//**
     * 获取员工
     *//*
    @GET("user/queryCompanyBanner")
    Observable<QueryBannerBean> queryCompanyBanner();
*/

    /**
     * 获取公司人员架构
     *
     * @param companyId
     * @return
     */
    @GET("user/findUsersByCompany")
    Observable<GetDepartmentStructureBean> findUsersByCompany(@Query("companyId") String companyId);


/*

    */
/**
 * 查找员工
 *
 * @param departmentId
 * @param employeeName
 * @return
 *//*

    @GET("employee/findEmployeeVague")
    Observable<QueryEmployeeBean> findEmployeeVague(@Query("departmentId") String departmentId, @Query("employeeName") String employeeName);
*/

    /**
     * 获取角色
     *
     * @return
     */
    @GET("moduleDataAuth/getRoleGroupList")
    Observable<RoleGroupResponseBean> getRoleGroupList();

    /**
     * 获取员工详细信息
     *
     * @return
     */
    @GET("employee/queryEmployeeInfo")
    Observable<EmployeeDetailBean> queryEmployeeInfo(@Query("employee_id") String employee_id);

    /**
     * 保存卡片信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("employee/savaCardInfo")
    Observable<EmployeeCardDetailBean> savaCardInfo(@Body EmployeeCardDetailBean.DataBean data);

    /**
     * 获取卡片信息
     *
     * @return
     */
    @GET("employee/queryEmployeeCard")
    Observable<EmployeeCardDetailBean> queryEmployeeCard(@Query("employeeId") String employeeId);

    /**
     * @param sign_id
     * @return
     */
    @GET("employee/queryEmployeeInfo")
    Observable<EmployeeDetailBean> queryEmployeeInfoBySingId(@Query("sign_id") String sign_id);

    /**
     * 人员模糊搜索
     *
     * @param departmentId 不传查询全公司
     * @param employeeName
     */
    @GET("user/findByUserName")
    Observable<EmployeeResultBean> findByUserName(@Query("department_id") String departmentId, @Query("employee_name") String employeeName);


    /**
     * 员工详情点赞
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("employee/whetherFabulous")
    Observable<BaseBean> whetherFabulous(@Body Map<String, String> map);

    /**
     * 登录后获取个人信息
     *
     * @return
     */
    @GET("user/queryInfo")
    Observable<UserInfoBean> queryInfo();

    /**
     * 修改个人中心资料
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("employee/editEmployeeDetail")
    Observable<BaseBean> editEmployeeDetail(@Body Map<String, Map> map);

    /**
     * 上传附件
     *
     * @param fileList 文件集合
     * @return url
     */
    @Multipart
    @POST("common/file/upload")
    Observable<UpLoadFileResponseBean> uploadAvatarFile(@PartMap Map<String, RequestBody> fileList);


    /**
     * 初始化时使用
     *
     * @param fileList
     * @return
     */
    @Multipart
    @POST("common/file/imageUpload")
    Observable<UpLoadFileResponseBean> imageUpload(@PartMap Map<String, RequestBody> fileList);

    /**
     * 应用模块上传文件(文件)
     *
     * @param fileList
     * @return
     */
    @Multipart
    @POST("common/file/applyFileUpload")
    Observable<UpLoadFileResponseBean> applyFileUpload(@PartMap Map<String, RequestBody> fileList);

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
     * 创建个人聊天
     *
     * @param receiverId
     * @return
     */
    @GET("imChat/addSingleChat")
    Observable<AddSingleChatResponseBean> addSingleChat(@Query("receiverId") String receiverId);

    /**
     * 获取群信息
     *
     * @param groupId
     * @return
     */
    @GET("imChat/getGroupInfo")
    Observable<GroupChatInfoBean> getGroupInfo(@Query("groupId") long groupId);

    /**
     * 获取个人聊天设置相关信息
     *
     * @param chatId
     * @return
     */
    @GET("imChat/getSingleInfo")
    Observable<SingleChatInfoBean> getSingleInfo(@Query("chatId") String chatId);

    /**
     * 获取会话列表
     *
     * @return
     */
    @GET("imChat/getListInfo")
    Observable<ConversationListBean> getListInfo();


    /**
     * 修改群设置
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/modifyGroupInfo")
    Observable<BaseBean> modifyGroupInfo(@Body Map<String, String> map);


    /**
     * 获取企信当前用户所有组的信息
     *
     * @return
     */
    @GET("imChat/getAllGroupsInfo")
    Observable<GetGroupListBean> getAllGroupsInfo();

    /**
     * 退出群
     *
     * @param id
     * @return
     */
    @GET("imChat/quitGroup")
    Observable<BaseBean> quitGroup(@Query("id") long id);

    /**
     * 解散群
     *
     * @param id
     * @return
     */
    @GET("imChat/releaseGroup")
    Observable<BaseBean> releaseGroup(@Query("id") long id);

    /**
     * 转让群组
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/transferGroup")
    Observable<BaseBean> transferGroup(@Body Map<String, Object> body);

    /**
     * 群聊添加人
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/pullPeople")
    Observable<BaseBean> pullPeople(@Body Map<String, String> map);

    /**
     * 群聊移除人
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/kickPeople")
    Observable<BaseBean> kickPeople(@Body Map<String, String> map);

    /**
     * 置顶
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/setTop")
    Observable<BaseBean> setTop(@Body Map<String, String> map);

    /**
     * 免打扰
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/noBother")
    Observable<BaseBean> noBother(@Body Map<String, String> map);

    /**
     * 删除会话
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/hideSession")
    Observable<BaseBean> hideSession(@Body Map<String, String> map);

    /**
     * 显示或隐藏会话
     *
     * @param map
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("imChat/hideSessionWithStatus")
    Observable<BaseBean> hideSessionWithStatus(@Body Map<String, String> map);

    /**
     * 小助手标记全部已读
     *
     * @param id
     * @return
     */
    @GET("imChat/markAllRead")
    Observable<BaseBean> markAllRead(@Query("id") String id);

    /**
     * 只看未读消息
     *
     * @param id
     * @return
     */
    @GET("imChat/markReadOption")
    Observable<BaseBean> markReadOption(@Query("id") long id);

    /**
     * 查看推送的消息修改状态
     *
     * @param id
     * @return
     */
    @GET("imChat/readMessage")
    Observable<BaseBean> readMessage(@Query("assistantId") String assistantId, @Query("id") String id);


    /**
     * 标记小助手查看信息
     *
     * @param id
     * @return
     */
    @GET("imChat/markReadOption")
    Observable<BaseBean> markReadOption(@Query("id") String id);

    /**
     * 获取小助手消息
     *
     * @param id
     * @param beanName
     * @return
     */
    @GET("imChat/getAssistantMessage")
    Observable<AppAssistantListBean> getAssistantMessage(@Query("id") String id,
                                                         @Query("beanName") String beanName,
                                                         @Query("pageNo") int pageNo,
                                                         @Query("pageSize") int pageSize);

    /**
     * 得到应用下的模块
     */
    @GET("module/findModuleListByAuth")
    Observable<AppModuleResultBean> getModule(@Query("application_id") String id);

    /**
     * 1. upTime（仅传此参数，表示获取>upTime的数据）
     * 2. downTime（仅传此参数，表示获取<upTime的数据）
     * 3. upTime+downTime（表示获取 upTime>X>downTime）
     * 4. dataSize（表示获取的数据量）
     * PS：后端保留加参数的权利
     *
     * @param id
     * @param beanName
     * @param dataSize
     * @param upTime
     * @param downTime
     * @return
     */
    @GET("imChat/getAssistantMessageLimit")
    Observable<AppAssistantListBean> getAssistantMessageLimit(@Query("id") String id,
                                                              @Query("beanName") String beanName,
                                                              @Query("dataSize") int dataSize,
                                                              @Query("upTime") Long upTime,
                                                              @Query("downTime") Long downTime);

    /**
     * 查询权限
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
     * 获取应用列表
     */
    @GET("module/findPcAllModuleList")
    Observable<AppListResultBean> getAppList();

    /**
     * 获取模块列表
     *
     * @return
     */
    @GET("module/findModuleList")
    Observable<ModuleBean> findModuleList();

    /**
     * 获取小助手设置
     *
     * @param assisstantId
     * @return
     */
    @GET("imChat/getAssisstantInfo")
    Observable<AppAssistantInfoBean> getAssisstantInfo(@Query("assisstantId") String assisstantId);

    /**
     * 获取审批小助手跳转详情参数
     */
    @Headers({"Content-Type: application/json"})
    @POST("approval/queryApprovalData")
    Observable<QueryApprovalDataResultBean> queryApprovalData(@Body Map<String, String> map);
}
