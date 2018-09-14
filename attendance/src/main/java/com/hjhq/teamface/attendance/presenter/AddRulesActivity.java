package com.hjhq.teamface.attendance.presenter;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.adapter.AddDateAdapter;
import com.hjhq.teamface.attendance.bean.AddDateBean;
import com.hjhq.teamface.attendance.bean.AddRulesBean;
import com.hjhq.teamface.attendance.bean.AttendanceTypeBean;
import com.hjhq.teamface.attendance.bean.AttendanceTypeListBean;
import com.hjhq.teamface.attendance.bean.BaseSelectBean;
import com.hjhq.teamface.attendance.bean.MemberOrDepartmentBean;
import com.hjhq.teamface.attendance.bean.WorkTimeListBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.utils.NotifyUtils;
import com.hjhq.teamface.attendance.views.AddRulesDelegate;
import com.hjhq.teamface.attendance.widget.SelectView;
import com.hjhq.teamface.basis.bean.BaseBean;
import com.hjhq.teamface.basis.constants.AttendanceConstants;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.device.SoftKeyboardUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.recycler.SimpleItemClickListener;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/add_rules", desc = "打卡规则设置")
public class AddRulesActivity extends ActivityPresenter<AddRulesDelegate, AttendanceModel> implements View.OnClickListener {

    RadioGroup mRadioGroup;
    //外勤打卡
    com.kyleduo.switchbutton.SwitchButton allowOutdoor;
    //人脸识别
    com.kyleduo.switchbutton.SwitchButton allowVeriface;
    //节假日自动排休
    com.kyleduo.switchbutton.SwitchButton autoArrange;


    LinearLayout llCanRoot;
    LinearLayout llCan11;
    LinearLayout llCan12;
    LinearLayout llCan21;
    LinearLayout llCan31;
    LinearLayout llCan32;
    //顶部班次说明
    TextView tvRulesHint;

    //固定班制
    SelectView selectView111;
    SelectView selectView112;
    SelectView selectView113;
    SelectView selectView114;
    SelectView selectView115;
    SelectView selectView116;
    SelectView selectView117;
    List<SelectView> selectViewList1 = new ArrayList<>();
    List<SelectView> selectViewList3 = new ArrayList<>();
    //排班制
    SelectView selectView121;
    SelectView selectView122;
    SelectView selectView211;
    //自由工时
    SelectView selectView311;
    SelectView selectView312;
    SelectView selectView313;
    SelectView selectView314;
    SelectView selectView315;
    SelectView selectView316;
    SelectView selectView317;
    List<BaseSelectBean> workTimeList = new ArrayList<>();
    List<BaseSelectBean> freeWorkTimeList = new ArrayList<>();
    List<BaseSelectBean> locationList = new ArrayList<>();
    List<BaseSelectBean> wifiList = new ArrayList<>();
    List<AddDateBean> mustAttendanceDays = new ArrayList<>();
    List<AddDateBean> notAttendanceDays = new ArrayList<>();
    private ArrayList<Member> mustMember = new ArrayList<>();
    private ArrayList<Member> notMember = new ArrayList<>();
    AddDateAdapter mAddDateAdapter1;
    AddDateAdapter mAddDateAdapter2;
    String[] weekdayArr;
    String[] optionArr;
    String[] hintArr;
    private int mode = 1;

    boolean isHaveRestTime = true;
    private int type;
    private String id;
    private String name;

    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(Constants.DATA_TAG1, AttendanceConstants.TYPE_ADD);
            id = bundle.getString(Constants.DATA_TAG2);
            if (!TextUtils.isEmpty(id)) {
                getDetailData();
            }
            mustMember = (ArrayList<Member>) bundle.getSerializable(Constants.DATA_TAG3);
            notMember = (ArrayList<Member>) bundle.getSerializable(Constants.DATA_TAG4);
            name = bundle.getString(Constants.DATA_TAG5);

        }
        weekdayArr = getResources().getStringArray(R.array.attendance_week_day_list);
        optionArr = getResources().getStringArray(R.array.attendance_rules_other_option_list);
        hintArr = getResources().getStringArray(R.array.attendance_add_rules_hint_list);
        AddDateBean bean1 = new AddDateBean();
        bean1.setName("上班");
        bean1.setId("");
        bean1.setType("1");
        bean1.setAttendance_time("");
        bean1.setLabel("");
        AddDateBean bean2 = new AddDateBean();
        bean2.setName("休息");
        bean2.setId("");
        bean2.setType("2");
        bean2.setAttendance_time("");
        bean2.setLabel("");

        freeWorkTimeList.add(bean1);
        freeWorkTimeList.add(bean2);
        initView();
        getNetData();
        initAdapter();
        //自由工时
        selectView311.setData(freeWorkTimeList);
        selectView311.setSelectedData(0);
        selectView312.setData(freeWorkTimeList);
        selectView312.setSelectedData(0);
        selectView313.setData(freeWorkTimeList);
        selectView313.setSelectedData(0);
        selectView314.setData(freeWorkTimeList);
        selectView314.setSelectedData(0);
        selectView315.setData(freeWorkTimeList);
        selectView315.setSelectedData(0);
        selectView316.setData(freeWorkTimeList);
        selectView316.setSelectedData(1);
        selectView317.setData(freeWorkTimeList);
        selectView317.setSelectedData(1);
    }

    private void initAdapter() {
        mAddDateAdapter1 = new AddDateAdapter(1, mustAttendanceDays);
        mAddDateAdapter2 = new AddDateAdapter(2, notAttendanceDays);
        viewDelegate.setAdapter1(mAddDateAdapter1);
        viewDelegate.setAdapter2(mAddDateAdapter2);
        viewDelegate.setItemClick1(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    mustAttendanceDays.remove(position);
                    mAddDateAdapter1.notifyDataSetChanged();
                }
            }
        });
        viewDelegate.setItemClick2(new SimpleItemClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    notAttendanceDays.remove(position);
                    mAddDateAdapter2.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取详情
     */
    private void getDetailData() {
        model.getAttendanceRulesDetail(mContext, id, new ProgressSubscriber<BaseBean>(mContext) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
            }
        });
    }

    /**
     * 获取班次/地址/wifi
     */
    private void getNetData() {
        //获取班次列表
        model.getAttendanceTimeList(mContext, new ProgressSubscriber<WorkTimeListBean>(mContext, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(WorkTimeListBean workTimeListBean) {
                super.onNext(workTimeListBean);
                workTimeList.clear();
                List<AddDateBean> dataList = workTimeListBean.getData().getDataList();
                if (dataList != null) {
                    workTimeList.addAll(dataList);
                    //固定班制
                    selectView111.setData(workTimeList);
                    selectView111.addRestDataItem();
                    selectView111.setSelectedData(1);
                    selectView112.setData(workTimeList);
                    selectView112.addRestDataItem();
                    selectView112.setSelectedData(1);
                    selectView113.setData(workTimeList);
                    selectView113.addRestDataItem();
                    selectView113.setSelectedData(1);
                    selectView114.setData(workTimeList);
                    selectView114.addRestDataItem();
                    selectView114.setSelectedData(1);
                    selectView115.setData(workTimeList);
                    selectView115.addRestDataItem();
                    selectView115.setSelectedData(1);
                    selectView116.setData(workTimeList);
                    selectView116.addRestDataItem();
                    selectView116.setSelectedData(0);
                    selectView117.setData(workTimeList);
                    selectView117.addRestDataItem();
                    selectView117.setSelectedData(0);
                    //排班制
                    selectView211.setData(workTimeList);
                    //selectView211.addRestDataItem();
                    selectView211.setSelectedData(0);
                    //自由工时
                   /* selectView311.setData(workTimeList);
                    selectView312.setData(workTimeList);
                    selectView313.setData(workTimeList);
                    selectView314.setData(workTimeList);
                    selectView315.setData(workTimeList);
                    selectView316.setData(workTimeList);
                    selectView317.setData(workTimeList);*/

                }
            }
        });
        //获取考勤地址列表
        model.getAttendanceTypeList(mContext, 0, new ProgressSubscriber<AttendanceTypeBean>(mContext, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(AttendanceTypeBean attendanceTypeBean) {
                super.onNext(attendanceTypeBean);
                locationList.clear();
                List<AttendanceTypeListBean> dataList = attendanceTypeBean.getData().getDataList();
                if (dataList != null) {
                    locationList.addAll(dataList);
                    selectView121.setData(locationList);
                }
            }
        });
        //获取考勤WiFi列表
        model.getAttendanceTypeList(mContext, 1, new ProgressSubscriber<AttendanceTypeBean>(mContext, false) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(AttendanceTypeBean attendanceTypeBean) {
                super.onNext(attendanceTypeBean);
                wifiList.clear();
                List<AttendanceTypeListBean> dataList = attendanceTypeBean.getData().getDataList();
                if (dataList != null) {
                    wifiList.addAll(dataList);
                    selectView122.setData(wifiList);

                }
            }
        });

    }

    private void initView() {
        mRadioGroup = viewDelegate.get(R.id.radio_group);
        tvRulesHint = viewDelegate.get(R.id.tv_rules_hint);
        TextUtil.setText(tvRulesHint, hintArr[mode - 1]);
        llCanRoot = viewDelegate.get(R.id.ll_can);
        llCan11 = viewDelegate.get(R.id.ll_can11);
        llCan12 = viewDelegate.get(R.id.ll_can12);
        llCan21 = viewDelegate.get(R.id.ll_can21);
        llCan31 = viewDelegate.get(R.id.ll_can31);
        llCan32 = viewDelegate.get(R.id.ll_can32);
        allowOutdoor = viewDelegate.get(R.id.sb11);
        allowVeriface = viewDelegate.get(R.id.sb12);
        autoArrange = viewDelegate.get(R.id.vb11);

        selectView111 = new SelectView(mContext, weekdayArr[0], SelectView.RADIO);
        selectView112 = new SelectView(mContext, weekdayArr[1], SelectView.RADIO);
        selectView113 = new SelectView(mContext, weekdayArr[2], SelectView.RADIO);
        selectView114 = new SelectView(mContext, weekdayArr[3], SelectView.RADIO);
        selectView115 = new SelectView(mContext, weekdayArr[4], SelectView.RADIO);
        selectView116 = new SelectView(mContext, weekdayArr[5], SelectView.RADIO);
        selectView117 = new SelectView(mContext, weekdayArr[6], SelectView.RADIO);
        llCan11.addView(selectView111);
        llCan11.addView(selectView112);
        llCan11.addView(selectView113);
        llCan11.addView(selectView114);
        llCan11.addView(selectView115);
        llCan11.addView(selectView116);
        llCan11.addView(selectView117);
        selectViewList1.add(selectView111);
        selectViewList1.add(selectView112);
        selectViewList1.add(selectView113);
        selectViewList1.add(selectView114);
        selectViewList1.add(selectView115);
        selectViewList1.add(selectView116);
        selectViewList1.add(selectView117);
        selectView121 = new SelectView(mContext, optionArr[1], SelectView.MULTI);
        selectView122 = new SelectView(mContext, optionArr[2], SelectView.MULTI);
        llCanRoot.addView(selectView121);
        llCanRoot.addView(selectView122);
        //选择班次
        selectView211 = new SelectView(mContext, optionArr[0], SelectView.RADIO);
        llCan21.addView(selectView211);
        //自由班次
        selectView311 = new SelectView(mContext, weekdayArr[0], SelectView.RADIO);
        selectView312 = new SelectView(mContext, weekdayArr[1], SelectView.RADIO);
        selectView313 = new SelectView(mContext, weekdayArr[2], SelectView.RADIO);
        selectView314 = new SelectView(mContext, weekdayArr[3], SelectView.RADIO);
        selectView315 = new SelectView(mContext, weekdayArr[4], SelectView.RADIO);
        selectView316 = new SelectView(mContext, weekdayArr[5], SelectView.RADIO);
        selectView317 = new SelectView(mContext, weekdayArr[6], SelectView.RADIO);
        llCan31.addView(selectView311);
        llCan31.addView(selectView312);
        llCan31.addView(selectView313);
        llCan31.addView(selectView314);
        llCan31.addView(selectView315);
        llCan31.addView(selectView316);
        llCan31.addView(selectView317);
        selectViewList3.add(selectView311);
        selectViewList3.add(selectView312);
        selectViewList3.add(selectView313);
        selectViewList3.add(selectView314);
        selectViewList3.add(selectView315);
        selectViewList3.add(selectView316);
        selectViewList3.add(selectView317);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_name));
        switch (type) {
            case AttendanceConstants.TYPE_ADD:
                AddRulesBean bean = new AddRulesBean();
                bean.setName(name);
                List<MemberOrDepartmentBean> list = new ArrayList<>();
                if (mustMember != null) {
                    for (int i = 0; i < mustMember.size(); i++) {
                        Member member = mustMember.get(i);
                        MemberOrDepartmentBean b = new MemberOrDepartmentBean();
                        b.setName(member.getName());
                        b.setId(member.getId() + "");
                        b.setType(member.getType() + "");
                        b.setPicture(member.getPicture());
                        b.setSign_id(member.getSign_id());
                        b.setValue(member.getType() + "-" + member.getId());
                        list.add(b);

                    }
                }
                bean.setMustAttendance(list);
                StringBuilder sb = new StringBuilder();
                if (notMember != null) {
                    for (int i = 0; i < notMember.size(); i++) {
                        if (TextUtils.isEmpty(sb)) {
                            sb.append(notMember.get(i).getId());
                        } else {
                            sb.append("," + notMember.get(i).getId());
                        }

                    }
                }
                bean.setNoAttendance(sb.toString());
                bean.setAttendanceType((mode - 1) + "");
                bean.setAutoStatus(autoArrange.isChecked() ? "1" : "0");
                bean.setMustPunchcardDate(mustAttendanceDays);
                StringBuilder sb2 = new StringBuilder();
                for (int i = 0; i < notAttendanceDays.size(); i++) {
                    if (TextUtils.isEmpty(sb2)) {
                        sb2.append(notAttendanceDays.get(i).getTime());
                    } else {
                        sb2.append("," + notAttendanceDays.get(i).getTime());
                    }

                }
                bean.setNoPunchcardDate(sb2.toString());
                bean.setFaceStatus("0");
                bean.setOutwokerStatus(allowOutdoor.isChecked() ? "1" : "0");
                List<BaseSelectBean> locationList = selectView121.getSelectedData();
                List<BaseSelectBean> wifiList = selectView122.getSelectedData();
                List<AttendanceTypeListBean> location = new ArrayList<>();
                List<AttendanceTypeListBean> wifi = new ArrayList<>();
                // TODO: 2018/6/27 为空判断
                for (int i = 0; i < locationList.size(); i++) {
                    if (locationList.get(i).getItemType() == AttendanceConstants.TYPE_SELECT_LOCATION) {
                        AttendanceTypeListBean lb = (AttendanceTypeListBean) locationList.get(i);
                        location.add(lb);
                    }
                }
                for (int i = 0; i < wifiList.size(); i++) {
                    if (wifiList.get(i).getItemType() == AttendanceConstants.TYPE_SELECT_WIFI) {
                        AttendanceTypeListBean lb = (AttendanceTypeListBean) wifiList.get(i);
                        wifi.add(lb);
                    }
                }
                bean.setAttendanceAddress(location);
                bean.setAttendanceWifi(wifi);
                List<AddDateBean> resultData = new ArrayList<>();
                //type   1上班,2休息,3必须打卡,4不用打卡
                switch (mode) {
                    case 1:
                        bean.setAttendanceStartTime(viewDelegate.getStartTime());
                        getResultData(mode, resultData);
                        bean.setReslutData(resultData);
                        break;
                    case 2:
                        bean.setReslutData(resultData);
                        break;
                    case 3:
                        getResultData(mode, resultData);
                        bean.setReslutData(resultData);
                        break;
                    default:
                        break;
                }
                model.addAttendanceRules(mContext, bean, new ProgressSubscriber<BaseBean>(mContext) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        ToastUtils.showToast(mContext, "成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });

                break;
            case AttendanceConstants.TYPE_EDIT:

                break;
            default:

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void getResultData(int mode, List<AddDateBean> resultData) {
        switch (mode) {
            case 1:
                for (int i = 0; i < selectViewList1.size(); i++) {
                    List<BaseSelectBean> list1 = selectViewList1.get(i).getData();
                    if (list1 != null && list1.size() > 0) {
                        AddDateBean bean11 = (AddDateBean) list1.get(0);
                        AddDateBean bean12 = new AddDateBean();
                        bean12.setType(bean11.getType());
                        bean12.setName(bean11.getName());
                        bean12.setLabel(bean11.getAttendance_time());
                        bean12.setId(bean11.getId());
                        bean12.setTime("");
                        resultData.add(bean12);
                    }
                }
                break;
            case 2:

                break;
            case 3:
                for (int i = 0; i < selectViewList3.size(); i++) {
                    List<BaseSelectBean> list1 = selectViewList3.get(i).getData();
                    if (list1 != null && list1.size() > 0) {
                        AddDateBean bean11 = (AddDateBean) list1.get(0);
                        AddDateBean bean12 = new AddDateBean();
                        bean12.setType(bean11.getType());
                        bean12.setName(bean11.getName());
                        bean12.setLabel(bean11.getAttendance_time());
                        bean12.setId(bean11.getId());
                        bean12.setTime("");
                        resultData.add(bean12);
                    }
                }
                break;
        }


    }

    @Override
    protected void bindEvenListener() {
        //自由工时选择开始时间
        viewDelegate.get(R.id.rl31).setOnClickListener(v -> {
            selectTime(R.id.rl31, viewDelegate.getStartTime());
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_name));
                if (checkedId == R.id.rb1) {
                    mode = 1;
                } else if (checkedId == R.id.rb2) {
                    mode = 2;
                } else if (checkedId == R.id.rb3) {
                    mode = 3;
                }
                TextUtil.setText(tvRulesHint, hintArr[mode - 1]);
                viewDelegate.switchType(mode);
            }
        });
        viewDelegate.get(R.id.tv122).setOnClickListener(v -> {
            NotifyUtils.showDateSelectMenu(mContext, viewDelegate.getRootView(), "", new NotifyUtils.OnDateSelectedListener() {
                @Override
                public void selected(long time) {
                    String[] menu = new String[workTimeList.size()];
                    for (int i = 0; i < workTimeList.size(); i++) {
                        menu[i] = ((AddDateBean) workTimeList.get(i)).getName() + "" + ((AddDateBean) workTimeList.get(i)).getAttendance_time();

                    }
                    PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "班次选择", menu, new OnMenuSelectedListener() {
                        @Override
                        public boolean onMenuSelected(int p) {
                            AddDateBean bean = new AddDateBean();
                            bean.setName(((AddDateBean) workTimeList.get(p)).getName());
                            bean.setId(((AddDateBean) workTimeList.get(p)).getId());
                            bean.setLabel(((AddDateBean) workTimeList.get(p)).getAttendance_time());
                            bean.setTime(time + "");
                            bean.setType("3");
                            mustAttendanceDays.add(bean);
                            mAddDateAdapter1.notifyDataSetChanged();
                            return false;
                        }
                    });
                }
            });
        });
        viewDelegate.get(R.id.tv142).setOnClickListener(v -> {
            NotifyUtils.showDateSelectMenu(mContext, viewDelegate.getRootView(), "", new NotifyUtils.OnDateSelectedListener() {
                @Override
                public void selected(long time) {
                    AddDateBean bean = new AddDateBean();
                    bean.setName("");
                    bean.setId("");
                    bean.setLabel("");
                    bean.setTime(time + "");
                    bean.setType("4");
                    notAttendanceDays.add(bean);
                    mAddDateAdapter2.notifyDataSetChanged();
                }
            });
        });
        super.bindEvenListener();
    }

    private void showRestTime(boolean isHaveRestTime) {
        /*if (isHaveRestTime) {
            viewDelegate.get(R.id.rl13).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.rl14).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.line4).setVisibility(View.VISIBLE);
            viewDelegate.get(R.id.line3).setVisibility(View.VISIBLE);
        } else {
            viewDelegate.get(R.id.rl13).setVisibility(View.GONE);
            viewDelegate.get(R.id.rl14).setVisibility(View.GONE);
            viewDelegate.get(R.id.line4).setVisibility(View.GONE);
            viewDelegate.get(R.id.line3).setVisibility(View.GONE);
        }*/

    }

    public void selectTime(int i, String time) {

        NotifyUtils.showTimeSelectMenu(mContext, viewDelegate.getRootView(), time, new NotifyUtils.OnTimeSelectedListener() {
            @Override
            public void selected(String time) {
                viewDelegate.setStartTime(i, time);

            }
        });
    }

    @Override
    public void onClick(View v) {
        SoftKeyboardUtils.hide(viewDelegate.get(R.id.et_name));
        /*switch (v.getId()) {
            case R.id.rl11:
                NotifyUtils.getInstance().showOperationNotify(mContext, 1, "上班时间: 09:00", "打卡时间: 09:00", viewDelegate.getRootView());
                break;
            case R.id.rl12:
                NotifyUtils.getInstance().showOperationNotify(mContext, 2, "下班时间: 09:00", "打卡时间: 09:00", viewDelegate.getRootView());
                break;
            case R.id.rl13:
                NotifyUtils.getInstance().showOperationNotify(mContext, 3, "迟到了明天早点来哦", "打卡时间: 09:00", viewDelegate.getRootView());
                break;
            case R.id.rl14:
                NotifyUtils.getInstance().showOperationNotify(mContext, 4, "早退了要跟领导说明下哦", "打卡时间: 09:00", viewDelegate.getRootView());
                break;
            case R.id.rl21:

                break;
            case R.id.rl22:

                break;
            case R.id.rl23:

                break;
            case R.id.rl24:

                break;
            case R.id.rl31:

                break;
            case R.id.rl32:

                break;
            case R.id.rl33:

                break;
            case R.id.rl34:

                break;
            case R.id.rl35:

                break;
            case R.id.rl36:

                break;
        }*/

    }
}
