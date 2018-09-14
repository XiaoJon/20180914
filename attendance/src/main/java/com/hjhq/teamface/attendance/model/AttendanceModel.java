package com.hjhq.teamface.attendance.model;

import android.text.TextUtils;

import com.hjhq.teamface.attendance.api.AttendanceApiService;
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
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.network.ApiManager;
import com.hjhq.teamface.basis.network.exception.HttpResultFunc;
import com.hjhq.teamface.basis.network.utils.TransformerHelper;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.basis.zygote.IModel;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */

public class AttendanceModel implements IModel<AttendanceApiService> {
    @Override
    public AttendanceApiService getApi() {
        if (Constants.IS_DEBUG) {
            SPHelper.setToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyOCIsInN1YiI6IjEiLCJhdWQiOiIxOCIsImlzcyI6IjEwMDQ1IiwiaWF0IjoxNTMwMDgxMDQ4fQ.YCiNyuVYwk-GZfYVwkaEELyLURQMiCMOfQou5uXD9Ck");
        }
        /*AppInterceptor interceptor = new AppInterceptor.Builder().addHeaderParam("TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyOCIsInN1YiI6IjEiLCJhdWQiOiIxOCIsImlzcyI6IjEwMDQ1IiwiaWF0IjoxNTI5NjI4NjYwfQ.02sFySWeGIemRR9JLI4l4gpe4a0zjuZdKicqLWRUW2Q").build();
        AttendanceApiService service = new MainRetrofit.Builder<AttendanceApiService>()
                .addNetworkInterceptor(interceptor)
                .baseUrl("http://192.168.1.60:8083/custom-gateway/")
                .build(AttendanceApiService.class);
        return service;*/
        return new ApiManager<AttendanceApiService>().getAPI(AttendanceApiService.class);
    }


    /**
     * 添加考勤地址/WiFi
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addAttendanceType(ActivityPresenter mActivity, AddTypeBean bean, Subscriber<BaseBean> s) {
        getApi().addAttendanceType(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 修改考勤方式
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void updateAttendanceType(ActivityPresenter mActivity, AddTypeBean bean, Subscriber<BaseBean> s) {
        getApi().updateAttendanceType(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除考勤方式
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void delAttendanceType(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().delAttendanceType(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取列表
     *
     * @param mActivity
     * @param type
     * @param s
     */
    public void getAttendanceTypeList(ActivityPresenter mActivity, int type, Subscriber<AttendanceTypeBean> s) {
        getApi().getAttendanceTypeList(type).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加班次
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveAttendanceTime(ActivityPresenter mActivity, AddWorkTimeBean bean, Subscriber<BaseBean> s) {
        getApi().saveAttendanceTime(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 修改班次
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void updateAttendanceTime(ActivityPresenter mActivity, AddWorkTimeBean bean, Subscriber<BaseBean> s) {
        getApi().updateAttendanceTime(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取班次列表
     *
     * @param mActivity
     * @param s
     */
    public void getAttendanceTimeList(ActivityPresenter mActivity, Subscriber<WorkTimeListBean> s) {
        getApi().getAttendanceTimeList().map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除班次
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void deleteAttendanceTime(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().delAttendanceTime(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 班次详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void findAttendanceTimeDetail(ActivityPresenter mActivity, String id, Subscriber<WorkTimeDetailBean> s) {

        getApi().findAttendanceTimeDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 打卡规则详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getAttendanceRulesDetail(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {

        getApi().getAttendanceRulesDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 删除打卡规则
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void delAttendanceRules(ActivityPresenter mActivity, String id, Subscriber<BaseBean> s) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getApi().delAttendanceRules(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 打卡规则列表
     *
     * @param mActivity
     * @param s
     */
    public void getAttendanceRulesList(ActivityPresenter mActivity, Subscriber<AttendanceRulesListBean> s) {
        getApi().getAttendanceRulesList().map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加打卡规则
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addAttendanceRules(ActivityPresenter mActivity, AddRulesBean bean, Subscriber<BaseBean> s) {
        getApi().addAttendanceRules(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加考勤管理员
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void addAdmin(ActivityPresenter mActivity, AttendanceSettingBean bean, Subscriber<BaseBean> s) {
        getApi().saveAdmin(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加/编辑提醒
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveRemind(ActivityPresenter mActivity, AddRemindBean bean, Subscriber<BaseBean> s) {
        getApi().saveRemind(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 添加考勤管理员
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void rankRules(ActivityPresenter mActivity, ChangeRankRulesBean bean, Subscriber<BaseBean> s) {
        getApi().saveCount(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 人性化班次
     *
     * @param mActivity
     * @param times
     * @param minutes
     * @param s
     */
    public void saveHommization(ActivityPresenter mActivity, int times, int minutes, Subscriber<BaseBean> s) {
        Map<String, Object> map = new HashMap<>();
        //人性化允许每月迟到次数
        map.put("humanizationAllowLateTimes", times);
        //人性化允许每月迟到次数
        map.put("humanizationAllowLateMinutes", minutes);
        getApi().saveHommization(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 人性化班次
     *
     * @param mActivity
     * @param times
     * @param minutes
     * @param s
     */
    public void eidtAbsenteeism(ActivityPresenter mActivity, String id, int times, int minutes, Subscriber<BaseBean> s) {
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        }
        //人性化允许每月迟到次数
        map.put("absenteeismRuleBeLateMinutes", times);
        //人性化允许每月迟到次数
        map.put("absenteeismRuleLeaveEarlyMinutes ", minutes);
        getApi().saveAbsenteeism(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 个人排班详情
     *
     * @param mActivity
     * @param dateStamp
     * @param s
     */
    public void getAttendanceDetail(ActivityPresenter mActivity, Long dateStamp, Long id, Subscriber<AttendanceDetailListBean> s) {

        getApi().getAttendanceDetail(dateStamp, id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 晚走晚到
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void leaveingLateRules(ActivityPresenter mActivity, AddLeaveingLateRulesListBean bean, Subscriber<BaseBean> s) {
        getApi().saveLate(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


    /**
     * 添加考勤管理员
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void updateRemind(ActivityPresenter mActivity, AttendanceSettingBean bean, Subscriber<BaseBean> s) {
        getApi().saveAdmin(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取其他设置详情
     *
     * @param mActivity
     * @param id
     * @param s
     */
    public void getSettingDetail(ActivityPresenter mActivity, Long id, Subscriber<AttendanceSettingDetailBean> s) {
        getApi().getSettingDetail(id).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 新增审批单
     *
     * @param mActivity
     * @param bean
     * @param s
     */
    public void saveAttendanceApproval(ActivityPresenter mActivity, final SaveAttendanceApprovalBean bean, Subscriber<ModuleBean> s) {
        getApi().saveAttendanceApproval(bean).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }

    /**
     * 获取布局
     *
     * @param mActivity
     * @param map
     * @param s
     */
    public void getCustomLayout(ActivityPresenter mActivity, Map<String, Object> map, Subscriber<CustomLayoutResultBean> s) {
        getApi().getEnableFields(map).map(new HttpResultFunc<>())
                .compose(mActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(TransformerHelper.applySchedulers())
                .subscribe(s);
    }


}
