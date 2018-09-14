package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.LeavingLateAdapter;
import com.hjhq.teamface.attendance.bean.AddLeaveingLateRulesBean;
import com.hjhq.teamface.attendance.bean.AddLeaveingLateRulesListBean;
import com.hjhq.teamface.attendance.bean.AddRemindBean;
import com.hjhq.teamface.attendance.bean.AttendanceSettingBean;
import com.hjhq.teamface.attendance.bean.AttendanceSettingDetailBean;
import com.hjhq.teamface.attendance.bean.AttendanceSettingDetailBeanData;
import com.hjhq.teamface.attendance.bean.ChangeRankRulesBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.utils.NotifyUtils;
import com.hjhq.teamface.attendance.views.AdditionalSettingsDelegate;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.member.AddMemberView;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.luojilab.router.facade.annotation.RouteNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/additional_settings", desc = "其他设置")
public class AdditionalSettingsActivity extends ActivityPresenter<AdditionalSettingsDelegate, AttendanceModel> implements View.OnClickListener {
    private String[] remindBeforeMenu;
    private String[] remindAfterMenu;
    private String[] remindMenu;
    private String[] statisticRulesMenu;
    private String[] earlyMenu;
    private String[] diligentMenu;
    private String[] lateMenu;
    private String[] rankMenu;
    private int[] remindValue = new int[]{5, 10, 15, 20, 25, 30};
    private int[] rankValue = new int[]{10, 15, 20};
    private int remindBefore;
    private int remindAfter;
    private int rankType;
    private int earlyRankNum;
    private int diligentRankNum;
    private int lateRankNum;
    private int lateRankRule;
    private int maxLateTimes;
    private int maxLateMinutes;
    private int lateWouldBeAbsenteeism;
    private int earlyWouldBeAbsenteeism;
    private int[] valueArr = new int[11];
    private AddMemberView mAddMemberView;
    private Member mAdmin;
    private List<AddLeaveingLateRulesBean> dataList = new ArrayList<>();
    private LeavingLateAdapter mAdapter;
    private int[] rankRulesArray = new int[]{-1, -1, -1, -1, -1};
    private String settingId = "";


    @Override
    public void init() {
        initView();
    }

    private void initView() {
        initData();
        viewDelegate.setTitle(R.string.attendance_additional_setting_title);
        mAdapter = new LeavingLateAdapter(dataList);
        viewDelegate.setAdapter(mAdapter);
        mAddMemberView = viewDelegate.get(R.id.member_view);
        mAddMemberView.setMaxItemNum(4);
    }

    private void initData() {
        remindBeforeMenu = getResources().getStringArray(R.array.attendance_remind_before_menu);
        remindAfterMenu = getResources().getStringArray(R.array.attendance_remind_after_menu);
        statisticRulesMenu = getResources().getStringArray(R.array.attendance_statistic_menu);
//        earlyMenu = getResources().getStringArray(R.array.attendance_ear);
//        diligentMenu = getResources().getStringArray(R.array.attendance_remind_after_menu);
        lateMenu = getResources().getStringArray(R.array.attendance_late_range_menu);
        rankMenu = getResources().getStringArray(R.array.attendance_rank_menu);
        getDetailData();
    }

    /**
     * 获取考勤设置详情
     */
    private void getDetailData() {
        model.getSettingDetail(mContext, 0L, new ProgressSubscriber<AttendanceSettingDetailBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(AttendanceSettingDetailBean attendanceSettingDetailBean) {
                super.onNext(attendanceSettingDetailBean);
                showData(attendanceSettingDetailBean.getData());
            }
        });
    }

    /**
     * 显示设置详情
     *
     * @param data
     */
    private void showData(AttendanceSettingDetailBeanData data) {
        //考勤管理员
        // TODO: 2018/7/26 管理员列表

        mAdmin = new Member();
        mAdmin.setId(TextUtil.parseLong(data.getId()));
        mAdmin.setName("名字");
        mAdmin.setPicture("头像");
        List<Member> list = new ArrayList<>();
        list.add(mAdmin);
        mAddMemberView.setMembers(list);
        //上班打卡提醒
        remindBefore = TextUtil.parseInt(data.getRemind_clock_before_work());
        if (remindBefore > 0) {
            viewDelegate.setRemindTimeBefore("提前" + remindBefore + "分钟");
        }
        //下班打卡提醒
        remindAfter = TextUtil.parseInt(data.getRemind_clock_after_work());
        if (remindBefore > 0) {
            viewDelegate.setRemindTimeBefore("延后" + remindAfter + "分钟");
        }
        //榜单统计方式
        rankType = TextUtil.parseInt(data.getList_set_type());
        if (rankType == 0) {
            viewDelegate.setRangeRules(rankMenu[1]);
        } else if (rankType == 1) {
            viewDelegate.setRangeRules(rankMenu[0]);
        }
        //早到榜
        earlyRankNum = TextUtil.parseInt(data.getList_set_early_arrival());
        viewDelegate.setEarlyArrival("前" + earlyRankNum);
        //勤勉榜
        diligentRankNum = TextUtil.parseInt(data.getList_set_diligent());
        viewDelegate.setDiligentRules("前" + diligentRankNum);

        //迟到榜
        diligentRankNum = TextUtil.parseInt(data.getList_set_be_late());
        viewDelegate.setLateArrival("前" + lateRankNum);

        //迟到排序规则
        lateRankRule = TextUtil.parseInt(data.getList_set_sort_type());
        if (lateRankRule == 0) {
            viewDelegate.setLateArrivalRules(lateMenu[1]);
        } else if (lateRankRule == 1) {
            viewDelegate.setLateArrivalRules(lateMenu[0]);
        }

        //晚走晚到
        data.getLate_nigth_walk_arr();

        //允许迟到次数
        maxLateTimes = TextUtil.parseInt(data.getHumanization_allow_late_times());
        if (maxLateTimes > 0) {
            viewDelegate.setText(1, maxLateTimes + "");
        }

        //允许迟到分钟数
        maxLateMinutes = TextUtil.parseInt(data.getHumanization_allow_late_minutes());
        if (maxLateMinutes > 0) {
            viewDelegate.setText(2, maxLateMinutes + "");
        }
        //迟到算旷工
        lateWouldBeAbsenteeism = TextUtil.parseInt(data.getAbsenteeism_rule_be_late_minutes());
        if (lateWouldBeAbsenteeism > 0) {
            viewDelegate.setText(3, lateWouldBeAbsenteeism + "");
        }
        //早退算旷工
        earlyWouldBeAbsenteeism = TextUtil.parseInt(data.getAbsenteeism_rule_leave_early_minutes());
        if (earlyWouldBeAbsenteeism > 0) {
            viewDelegate.setText(4, earlyWouldBeAbsenteeism + "");
        }
    }


    @Override
    protected void bindEvenListener() {
        mAddMemberView.setOnAddMemberClickedListener(() -> {
            List<Member> members = mAddMemberView.getMembers();
            for (int i = 0; i < members.size(); i++) {
                members.get(i).setCheck(true);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, (Serializable) members);
            bundle.putInt(C.CHECK_TYPE_TAG, C.MULTIL_SELECT);
            CommonUtil.startActivtiyForResult(mContext,
                    SelectMemberActivity.class, Constants.REQUEST_CODE4, bundle);

        });
        viewDelegate.setItemClick(new SimpleItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    dataList.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else if (view.getId() == R.id.set) {
                    showSetTimeDialog(2, position);

                }
                super.onItemChildClick(adapter, view, position);
            }
        });
        viewDelegate.get(R.id.set221).setOnClickListener(v -> {
            //设置上班打卡提醒
            showRemindMenu(1);

        });
        viewDelegate.get(R.id.set231).setOnClickListener(v -> {
            //设置下班打卡提醒
            showRemindMenu(2);
        });
        viewDelegate.get(R.id.set321).setOnClickListener(v -> {
            //榜单统计方式
            showRangeMenu(1);
        });
        viewDelegate.get(R.id.set331).setOnClickListener(v -> {
            //早到榜
            showRangeMenu(2);
        });
        viewDelegate.get(R.id.set341).setOnClickListener(v -> {
            //勤勉榜
            showRangeMenu(3);
        });
        viewDelegate.get(R.id.set351).setOnClickListener(v -> {
            //迟到榜
            showRangeMenu(4);
        });
        viewDelegate.get(R.id.set361).setOnClickListener(v -> {
            //迟到排序规则
            showRangeMenu(5);
        });

        viewDelegate.get(R.id.set421).setOnClickListener(v -> {
            //晚走晚到
            showSetTimeDialog(1, 0);
        });
        viewDelegate.get(R.id.set521).setOnClickListener(v -> {
            //旷工规则
            showMenu1(1, "每人允许迟到次数", "请设置大于等于零整数");
        });
        viewDelegate.get(R.id.set531).setOnClickListener(v -> {
            //旷工规则
            showMenu1(2, "单次允许迟到分钟数", "请设置大于等于零整数");
        });
        viewDelegate.get(R.id.set621).setOnClickListener(v -> {
            //旷工规则
            showMenu1(3, "单次迟到超过分钟数记为旷工", "请设置大于等于零整数");
        });
        viewDelegate.get(R.id.set631).setOnClickListener(v -> {
            //晚走晚到
            showMenu1(4, "单次早退超过分钟数记为旷工", "请设置大于等于零整数");
        });


        super.bindEvenListener();
    }

    /**
     * 榜单设置
     *
     * @param i
     */
    private void showRangeMenu(int i) {
        String title = "";
        switch (i) {
            case 1:
                title = "榜单统计方式";
                remindMenu = statisticRulesMenu;
                break;
            case 2:
            case 3:
            case 4:
                title = "排名数字选择";
                remindMenu = rankMenu;
                break;
            case 5:
                title = "迟到排序方式";
                remindMenu = lateMenu;
                break;
            default:
                break;
        }
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), title, remindMenu, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                rankRulesArray[i - 1] = p;
                switch (i) {
                    case 1:
                        viewDelegate.setRangeRules(remindMenu[p]);
                        rankType = p == 0 ? 1 : 0;
                        break;
                    case 2:
                        viewDelegate.setEarlyArrival(remindMenu[p]);
                        earlyRankNum = rankValue[p];
                        break;
                    case 3:
                        viewDelegate.setDiligentRules(remindMenu[p]);
                        diligentRankNum = rankValue[p];
                        break;
                    case 4:
                        viewDelegate.setLateArrival(remindMenu[p]);
                        lateRankNum = rankValue[p];
                        break;
                    case 5:
                        viewDelegate.setLateArrivalRules(remindMenu[p]);
                        lateRankRule = p == 0 ? 1 : 0;
                        break;
                    default:
                        break;
                }
                saveRank();
                return false;
            }
        });

    }

    /**
     * 人性化班次与旷工规则
     *
     * @param type
     * @param title
     * @param hint
     */
    private void showMenu1(int type, String title, String hint) {
        NotifyUtils.getInstance().showEditMenu1(mContext, title, hint, viewDelegate.getRootView(), new NotifyUtils.OnSingleValueListener() {
            @Override
            public void clickSure(String value1) {
                viewDelegate.setText(type, value1);
                switch (type) {
                    case 1:
                        maxLateTimes = TextUtil.parseInt(value1);
                        saveHommization();
                        break;
                    case 2:
                        maxLateMinutes = TextUtil.parseInt(value1);
                        saveHommization();
                        break;
                    case 3:
                        lateWouldBeAbsenteeism = TextUtil.parseInt(value1);
                        addAbsenteeism();
                        break;
                    case 4:
                        earlyWouldBeAbsenteeism = TextUtil.parseInt(value1);
                        addAbsenteeism();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 晚走晚到规则
     *
     * @param type
     * @param position
     */
    private void showSetTimeDialog(int type, int position) {
        NotifyUtils.getInstance().showEditMenu2(mContext, "第一天下班晚走", "第二天上班晚到", "两项都设置才能生效", "请输入", "请输入", viewDelegate.getRootView(), new NotifyUtils.OnClickSureListener() {
            @Override
            public void clickSure(String value1, String value2) {
                AddLeaveingLateRulesBean bean = new AddLeaveingLateRulesBean();
                bean.setLeaveingLateTime(TextUtil.parseInt(value1));
                bean.setWorkLateTime(TextUtil.parseInt(value2));
                if (type == 1) {

                    if (bean.getWorkLateTime() > 12 || bean.getLeaveingLateTime() > 12) {
                        ToastUtils.showToast(mContext, "这时间过分了☞");
                        return;
                    }
                    dataList.add(bean);
                    mAdapter.notifyDataSetChanged();
                    //添加晚走晚到
                    addLeaveingLateRules();
                } else if (type == 2) {
                    if (bean.getWorkLateTime() > 12 || bean.getLeaveingLateTime() > 12) {
                        ToastUtils.showToast(mContext, "这时间过分了☞");
                        return;
                    }
                    List<AddLeaveingLateRulesBean> list = new ArrayList<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (position == i) {
                            bean.setId(dataList.get(i).getId());
                            list.add(bean);
                        } else {
                            list.add(dataList.get(i));
                        }
                    }
                    dataList.clear();
                    dataList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    //编辑晚走晚到
                    addLeaveingLateRules();
                }
            }
        });
    }

    /**
     * 晚走晚到规则
     */
    private void addLeaveingLateRules() {
        AddLeaveingLateRulesListBean bean = new AddLeaveingLateRulesListBean();
        bean.setLateNigthWalkArr(mAdapter.getData());
        model.leaveingLateRules(mContext, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getDetailData();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
            }
        });

    }

    private void showRemindMenu(int i) {
        String title = "";
        if (i == 1) {
            title = "上班提前提醒分钟 ";
            remindMenu = remindBeforeMenu;
        } else if (i == 2) {
            title = "下班延后提醒分钟";
            remindMenu = remindAfterMenu;
        }

        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), title, remindMenu, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                if (i == 1) {
                    viewDelegate.setRemindTimeBefore(remindMenu[p]);
                    remindBefore = remindValue[p];
                } else if (i == 2) {
                    viewDelegate.setRemindTimeAfter(remindMenu[p]);
                    remindAfter = remindValue[p];
                }
                saveRemind();

                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE4:
                    if (data != null) {
                        List<Member> list = (List<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
                        if (list.size() > 0) {
                            mAddMemberView.setMembers(list);
                            addAdmin();
                        } else {
                            ToastUtils.showToast(mContext, "未选择管理员");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 添加管理员
     */
    private void saveAdmin() {
        AttendanceSettingBean bean = getAttendanceSettingBean();

        model.addAdmin(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                getDetailData();
            }
        });
    }

    /**
     * 修改管理员
     */
    private void addAdmin() {
        AttendanceSettingBean bean = getAttendanceSettingBean();

        model.addAdmin(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getDetailData();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                getDetailData();
            }
        });
    }

    /**
     * 人性化班次
     */
    private void saveHommization() {
        model.saveHommization(mContext, maxLateTimes, maxLateMinutes, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                getDetailData();
            }
        });
    }


    /**
     * 新增/编辑旷工规则
     */
    private void addAbsenteeism() {
        model.eidtAbsenteeism(mContext, settingId, maxLateTimes, maxLateMinutes, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                getDetailData();
            }
        });
    }

    /**
     * 修改/添加提醒
     */
    private void saveRemind() {
        AddRemindBean bean = new AddRemindBean();
        bean.setId(settingId);
        bean.setRemindCockBeforeWork(remindBefore);
        bean.setRemindClockAfterWork(remindAfter);

        model.saveRemind(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getDetailData();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);

            }
        });
    }

    /**
     * 修改/添加提醒
     */
    private void saveRank() {
        ChangeRankRulesBean bean = new ChangeRankRulesBean();
        bean.setListSetType(rankType);
        bean.setListSetEarlyArrival(earlyRankNum);
        bean.setListSetDiligent(diligentRankNum);
        bean.setListSetBeLate(lateRankNum);
        bean.setListSetSortType(lateRankRule);

        model.rankRules(mContext, bean, new ProgressSubscriber<BaseBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getDetailData();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);

            }
        });
    }

    @NonNull
    private AttendanceSettingBean getAttendanceSettingBean() {
        AttendanceSettingBean bean = new AttendanceSettingBean();
        //管理员id
        bean.setAdminArr(getAdminIds());
        //上班前打卡提醒
        bean.setRemindCockBeforeWork(remindBefore);
        //下班后打卡提醒
        bean.setRemindClockAfterWork(remindAfter);
        //榜单统计方式0分开1一起
        bean.setListSetType(rankType);
        //早到榜
        bean.setListSetEarlyArrival(earlyRankNum);
        //勤勉榜
        bean.setListSetDiligent(diligentRankNum);
        //迟到榜
        bean.setListSetBeLate(lateRankNum);
        //迟到排序规则0次数1时长
        bean.setListSetSortType(lateRankRule);
        //晚走晚到
        bean.setLateNigthWalkArr(new ArrayList<>());
        //允许迟到次数
        bean.setHumanizationAllowLateTimes(maxLateTimes);
        //允许迟到分钟数
        bean.setHumanizationAllowLateMinutes(maxLateMinutes);
        //超过算迟到
        bean.setAbsenteeismRuleBeLateMinutes(lateWouldBeAbsenteeism);
        //超过算早退
        bean.setAbsenteeismRuleLeaveEarlyMinutes(earlyWouldBeAbsenteeism);
        return bean;
    }

    /**
     * 获取管理员id
     *
     * @return
     */
    private String getAdminIds() {
        List<Member> list = mAddMemberView.getMembers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append("," + list.get(i).getId() + "");
        }
        String ids = sb.toString().substring(1, sb.length());
        return ids;
    }
}
