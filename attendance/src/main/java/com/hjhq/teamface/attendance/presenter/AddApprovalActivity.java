package com.hjhq.teamface.attendance.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.attendance.bean.SaveAttendanceApprovalBean;
import com.hjhq.teamface.attendance.model.AttendanceModel;
import com.hjhq.teamface.attendance.views.AddApprovalDelegate;
import com.hjhq.teamface.basis.bean.AppModuleBean;
import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.bean.CustomLayoutResultBean;
import com.hjhq.teamface.basis.bean.ModuleBean;
import com.hjhq.teamface.basis.bean.ModuleItemBean;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.popupwindow.OnMenuSelectedListener;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/5.
 * Describe：
 */
@RouteNode(path = "/add_approval", desc = "添加审批单")
public class AddApprovalActivity extends ActivityPresenter<AddApprovalDelegate, AttendanceModel> implements View.OnClickListener {
    String[] stateMenu;
    String[] unitMenu;
    int stateIndex = -1;
    int unitIndex = -1;
    String[] timeMenu;
    String[] timeVale;
    private int startTimeIndex;
    private int endTimeIndex;


    ModuleItemBean mModuleItemBean;
    AppModuleBean appModeluBean;
    // String datetimeType = "yyyy年MM月dd日 HH:mm";
    String datetimeType = "yyyy-MM-dd HH:mm";
    Calendar mStartCalendar;
    Calendar mStopCalendar;
    boolean isStartTimeChoosed = false;
    boolean isStopTimeChoosed = false;

    @Override
    public void init() {
        stateMenu = getResources().getStringArray(R.array.attendance_state_menu);
        unitMenu = getResources().getStringArray(R.array.attendance_unit_menu);
        mStartCalendar = Calendar.getInstance();
        mStopCalendar = Calendar.getInstance();
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5, R.id.rl6);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveApproval();
        return super.onOptionsItemSelected(item);
    }

    private void chooseApproveType() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA_TAG1, Constants.SELECT_TYPE);
        UIRouter.getInstance().openUri(mContext, "DDComp://project/appModule", bundle, Constants.REQUEST_CODE3);
    }


    private void saveApproval() {
        if (appModeluBean == null) {
            ToastUtils.showToast(mContext, "请选择要关联的审批");
            return;
        }
        if (stateIndex == -1) {
            ToastUtils.showToast(mContext, "请选择要修正的状态");
            return;
        }
        if (!isStartTimeChoosed) {
            ToastUtils.showToast(mContext, "请选择开始时间");
            return;
        }
        // TODO: 2018/7/30 重新设置数据
        SaveAttendanceApprovalBean bean = new SaveAttendanceApprovalBean();
        bean.setRelevanceDataId(mModuleItemBean.getDataId());
        bean.setRelevanceModuleBean(mModuleItemBean.getModule());
        bean.setRelevanceModuleId(mModuleItemBean.getId());
        bean.setRelevanceModuleName(mModuleItemBean.getTitle());
        bean.setStartTime(mStartCalendar.getTimeInMillis());
        if (isStopTimeChoosed) {
            bean.setEndTime(mStopCalendar.getTimeInMillis());
        }
        bean.setDuration(TextUtil.parseDouble(viewDelegate.getDurationText()));
        bean.setDurationUnit(unitIndex);
        model.saveAttendanceApproval(mContext, bean, new ProgressSubscriber<ModuleBean>(mContext, true) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(ModuleBean moduleBean) {
                super.onNext(moduleBean);
            }

        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl1) {
            //关联审批单
            chooseApproveType();

        } else if (v.getId() == R.id.rl2) {
            //修正状态
            selectStateMenu();
        } else if (v.getId() == R.id.rl3) {
            //开始时间
            selectDate(1);
        } else if (v.getId() == R.id.rl4) {
            //结束时间
            selectDate(2);

        } else if (v.getId() == R.id.rl5) {
            //时长

        } else if (v.getId() == R.id.rl6) {
            //时长单位
            selectUnitMenu();
        }

    }

    private void selectDate(int i) {
        if (appModeluBean == null) {
            ToastUtils.showToast(mContext, "请选择模块");
            return;
        }
        if (timeMenu == null || timeMenu.length <= 0) {
            ToastUtils.showToast(mContext, "所选模块无相关字段");
            return;
        }
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "选择字段", timeMenu, new OnMenuSelectedListener() {
            @Override
            public boolean onMenuSelected(int p) {
                switch (i) {
                    case 1:
                        viewDelegate.setStartTime(timeMenu[p]);
                        startTimeIndex = p;
                        break;
                    case 2:
                        viewDelegate.setStartTime(timeMenu[p]);
                        endTimeIndex = p;
                        break;
                    default:

                        break;
                }
                return false;
            }
        });

    }

    /**
     * 修正状态
     */
    private void selectStateMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(),
                getResources().getString(R.string.attendance_please_select_hint), stateMenu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        stateIndex = p;
                        viewDelegate.setStateText(stateMenu[stateIndex]);
                        return true;
                    }
                });

    }

    /**
     * 修正状态
     */
    private void selectUnitMenu() {
        PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(),
                getResources().getString(R.string.attendance_please_select_hint), unitMenu,
                new OnMenuSelectedListener() {
                    @Override
                    public boolean onMenuSelected(int p) {
                        unitIndex = p;
                        viewDelegate.setUnitText(unitMenu[unitIndex]);
                        return true;
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE1:
                //开始时间
                mStartCalendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                viewDelegate.setStartTime(DateTimeUtil.longToStr(mStartCalendar.getTimeInMillis(), datetimeType));
                isStartTimeChoosed = true;
                break;
            case Constants.REQUEST_CODE2:
                //结束时间
                mStopCalendar = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                viewDelegate.setStopTime(DateTimeUtil.longToStr(mStartCalendar.getTimeInMillis(), datetimeType));
                isStopTimeChoosed = true;
                break;
            case Constants.REQUEST_CODE3:
                //选择模块
                appModeluBean = (AppModuleBean) data.getSerializableExtra(Constants.DATA_TAG1);
                if (appModeluBean != null) {
                    viewDelegate.setApproveTitle(appModeluBean.getChinese_name());
                    getCustomLayout(appModeluBean);
                }
                break;
            default:

                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 得到自定义布局
     */

    private void getCustomLayout(AppModuleBean appModeluBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("bean", appModeluBean.getEnglish_name());
        map.put("operationType", CustomConstants.ADD_STATE);
        model.getCustomLayout(this, map, new ProgressSubscriber<CustomLayoutResultBean>(this) {
            @Override
            public void onNext(CustomLayoutResultBean customLayoutResultBean) {
                super.onNext(customLayoutResultBean);
                final List<CustomLayoutResultBean.DataBean.LayoutBean> layout = customLayoutResultBean.getData().getLayout();
                if (layout == null || layout.size() <= 0) {
                    ToastUtils.showToast(mContext, "无相关字段");
                    return;
                }
                for (CustomLayoutResultBean.DataBean.LayoutBean layoutBean : layout) {
                    boolean isTerminalApp = "1".equals(layoutBean.getTerminalApp());
                    boolean isHideInCreate = "1".equals(layoutBean.getIsHideInCreate());
                    boolean isSpread = "0".equals(layoutBean.getIsSpread());
                    boolean isHideColumnName = "1".equals(layoutBean.getIsHideColumnName());
                    if (!isTerminalApp || isHideInCreate) {
                        //不是app端或新建时隐藏
                        continue;
                    }

                    List<CustomBean> list = layoutBean.getRows();
                    List<CustomBean> timeList = layoutBean.getRows();

                    for (int i = 0; i < list.size(); i++) {
                        if (CustomConstants.DATETIME.equals(list.get(i).getType())) {
                            final CustomBean customBean = list.get(i);
                            timeList.add(customBean);
                        }
                    }
                    timeMenu = new String[list.size()];
                    timeVale = new String[list.size()];

                    for (int i = 0; i < timeList.size(); i++) {
                        timeMenu[i] = timeList.get(i).getLabel();
                        timeVale[i] = timeList.get(i).getName();
                    }
                }

            }
        });
    }
}
