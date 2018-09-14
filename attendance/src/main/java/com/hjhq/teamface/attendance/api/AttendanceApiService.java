package com.hjhq.teamface.attendance.api;

import com.hjhq.teamface.attendance.bean.AddLeaveingLateRulesListBean;
import com.hjhq.teamface.attendance.bean.AddRemindBean;
import com.hjhq.teamface.attendance.bean.AddRulesBean;
import com.hjhq.teamface.attendance.bean.AddTypeBean;
import com.hjhq.teamface.attendance.bean.AddWorkTimeBean;
import com.hjhq.teamface.attendance.bean.AttendanceDetailListBean;
import com.hjhq.teamface.attendance.bean.AttendanceRulesListBean;
import com.hjhq.teamface.attendance.bean.AttendanceSettingBean;
import com.hjhq.teamface.attendance.bean.AttendanceSettingDetailBean;
import com.hjhq.teamface.attendance.bean.AttendanceTypeBean;
import com.hjhq.teamface.attendance.bean.ChangeRankRulesBean;
import com.hjhq.teamface.attendance.bean.SaveAttendanceApprovalBean;
import com.hjhq.teamface.attendance.bean.WorkTimeDetailBean;
import com.hjhq.teamface.attendance.bean.WorkTimeListBean;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.ModuleBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * 考勤api
 */
public interface AttendanceApiService {
    /**
     * 新增考勤方式
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceWay/save")
    Observable<BaseBean> addAttendanceType(@Body AddTypeBean bean);

    /**
     * 修改考勤方式
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceWay/update")
    Observable<BaseBean> updateAttendanceType(@Body AddTypeBean bean);

    /**
     * 删除考勤方式
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceWay/del")
    Observable<BaseBean> delAttendanceType(@Body Map<String, String> bean);

    /**
     * 根据id查询考勤方式详情
     *
     * @param id
     * @return
     */
    @GET("attendanceWay/findDetail")
    Observable<BaseBean> findDetail(@Query("id") String id);

    /**
     * 获取数据列表(不分页)
     *
     * @param type 0：地址考勤 1：wifi考勤
     * @return
     */
    @GET("attendanceWay/findWebList")
    Observable<AttendanceTypeBean> getAttendanceTypeList(@Query("type") int type);

    /**
     * 新增班次
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceClass/save")
    Observable<BaseBean> saveAttendanceTime(@Body AddWorkTimeBean bean);

    /**
     * 修改班次
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceClass/update")
    Observable<BaseBean> updateAttendanceTime(@Body AddWorkTimeBean bean);

    /**
     * 删除班次
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceClass/del")
    Observable<BaseBean> delAttendanceTime(@Body Map<String, String> bean);

    /**
     * 查询班次详情
     *
     * @param id
     * @return
     */
    @GET("attendanceClass/findDetail")
    Observable<WorkTimeDetailBean> findAttendanceTimeDetail(@Query("id") String id);

    /**
     * 获取班次列表
     *
     * @return
     */
    @GET("attendanceClass/findWebList")
    Observable<WorkTimeListBean> getAttendanceTimeList();

    /**
     * 新增打卡规则
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSchedule/save")
    Observable<BaseBean> addAttendanceRules(@Body AddRulesBean bean);

    /**
     * 修改打卡规则
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSchedule/update")
    Observable<BaseBean> updateAttendanceRules(@Body Map<String, String> bean);

    /**
     * 删除打卡规则
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSchedule/del")
    Observable<BaseBean> delAttendanceRules(@Body Map<String, String> bean);

    /**
     * 打卡规则详情
     *
     * @param id
     * @return
     */
    @GET("attendanceSchedule/findDetail")
    Observable<BaseBean> getAttendanceRulesDetail(@Query("id") String id);

    /**
     * 打卡规则列表
     *
     * @return
     */
    @GET("attendanceSchedule/findWebList")
    Observable<AttendanceRulesListBean> getAttendanceRulesList();

    /**
     * 员工信息同步(规则生效)
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSchedule/synchronousUpdate")
    Observable<BaseBean> syncEmployeeInfo(@Body Map<String, String> bean);

    /**
     * 新增排班周期
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceCycle/save")
    Observable<BaseBean> addAttendanceCycle(@Body Map<String, String> bean);

    /**
     * 修改排班周期
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceCycle/update")
    Observable<BaseBean> updateAttendanceCycle(@Body Map<String, String> bean);

    /**
     * 删除排班周期
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceCycle/del")
    Observable<BaseBean> delAttendanceCycle(@Body Map<String, String> bean);

    /**
     * 获取排班周期详情
     *
     * @param id
     * @return
     */
    @GET("attendanceCycle/findDetail")
    Observable<BaseBean> getAttendanceCycleDetail(@Query("id") String id);

    /**
     * 获取个人排班详情
     *
     * @param dateStamp 当月第一天时间戳 1533052800
     * @param id        员工id
     * @return
     */
    @GET("attendanceManagement/findAppDetail")
    Observable<AttendanceDetailListBean> getAttendanceDetail(@Query("month") Long dateStamp, @Query("id") Long id);

    /**
     * 项目文件下载(不在此处使用)
     *
     * @return
     */
    @GET("common/file/projectDownload")
    Observable<BaseBean> downloadProjectFile();

    /**
     * 其他设置-新增管理员
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/saveAdmin")
    Observable<BaseBean> saveAdmin(@Body AttendanceSettingBean bean);

    /**
     * 其他设置-修改管理员
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/update")
    Observable<BaseBean> updateAdmin(@Body Map<String, String> bean);

    /**
     * 其他设置-新增打卡提醒
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/saveRemind")
    Observable<BaseBean> saveRemind(@Body AddRemindBean bean);

    /**
     * 其他设置-新增/编辑榜单设置
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/saveCount")
    Observable<BaseBean> saveCount(@Body ChangeRankRulesBean bean);

    /**
     * 其他设置-新增/编辑晚走晚到设置
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/saveLate")
    Observable<BaseBean> saveLate(@Body AddLeaveingLateRulesListBean bean);

    /**
     * 其他设置-新增/编辑人性化班次
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/saveHommization")
    Observable<BaseBean> saveHommization(@Body Map<String, Object> bean);

    /**
     * 其他设置-新增/编辑旷工规则
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceSetting/saveAbsenteeism")
    Observable<BaseBean> saveAbsenteeism(@Body Map<String, Object> bean);

    /**
     * 其他设置-详情
     *
     * @param id
     * @return
     */
    @GET("attendanceSetting/findDetail")
    Observable<AttendanceSettingDetailBean> getSettingDetail(@Query("id") Long id);

    /**
     * 添加关联审批单
     *
     * @param bean
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @POST("attendanceRelevanceApprove/save")
    Observable<ModuleBean> saveAttendanceApproval(@Body SaveAttendanceApprovalBean bean);

    /**
     * 获取自定义布局
     */
    @GET("layout/getEnableLayout")
    Observable<CustomLayoutResultBean> getEnableFields(@QueryMap Map<String, Object> map);
}
