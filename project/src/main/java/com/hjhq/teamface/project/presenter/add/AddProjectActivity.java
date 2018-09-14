package com.hjhq.teamface.project.presenter.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.hjhq.teamface.basis.bean.CommonNewResultBean2;
import com.hjhq.teamface.basis.constants.C;
import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.network.subscribers.ProgressSubscriber;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.ToastUtils;
import com.hjhq.teamface.basis.util.dialog.DialogUtils;
import com.hjhq.teamface.basis.util.file.SPHelper;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;
import com.hjhq.teamface.common.ui.member.SelectMemberActivity;
import com.hjhq.teamface.common.ui.time.DateTimeSelectPresenter;
import com.hjhq.teamface.common.utils.CommonUtil;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.SaveProjectRequestBean;
import com.hjhq.teamface.project.bean.TempDataList;
import com.hjhq.teamface.project.model.ProjectModel;
import com.hjhq.teamface.project.presenter.ProjectDetailActivity;
import com.hjhq.teamface.project.presenter.ProjectTemplateActivity;
import com.hjhq.teamface.project.ui.add.NewOrEditProjectDelegate;
import com.luojilab.router.facade.annotation.RouteNode;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 项目
 * Created by Administrator on 2018/4/10.
 */
@RouteNode(path = "/addproject", desc = "新建项目")
public class AddProjectActivity extends ActivityPresenter<NewOrEditProjectDelegate, ProjectModel> {
    private static final String[] SCOPE_MENU = {"不公开", "公开"};
    private int currentIndex = 0;
    private Member admin;
    private Calendar mCalendar;
    /**
     * 模板
     */
    private TempDataList tempData;

    @Override
    public void init() {
        viewDelegate.setTitle("新建项目");
        viewDelegate.setRightMenuTexts("确定");
        viewDelegate.setScopeText(SCOPE_MENU[currentIndex]);
        viewDelegate.get(R.id.rl_auth).setVisibility(View.GONE);
        viewDelegate.get(R.id.rl_start_time).setVisibility(View.GONE);
        viewDelegate.get(R.id.rl_progress).setVisibility(View.GONE);
        viewDelegate.get(R.id.rl_status).setVisibility(View.GONE);
        viewDelegate.get(R.id.rl_progress_type).setVisibility(View.GONE);

        admin = new Member();
        admin.setId(TextUtil.parseLong(SPHelper.getEmployeeId()));
        admin.setName(SPHelper.getUserName());
        admin.setType(C.EMPLOYEE);
        admin.setPicture(SPHelper.getUserAvatar());
        viewDelegate.setAdmin(admin);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //模版
        viewDelegate.setOnClickListener(v -> CommonUtil.startActivtiyForResult(mContext, ProjectTemplateActivity.class, Constants.REQUEST_CODE3)
                , R.id.rl_template);

        //是否公开
        viewDelegate.setOnClickListener(v ->
                PopUtils.showBottomMenu(mContext, viewDelegate.getRootView(), "", SCOPE_MENU, currentIndex, 0, p -> {
                    currentIndex = p;
                    viewDelegate.setScopeText(SCOPE_MENU[currentIndex]);
                    return false;
                }), R.id.rl_scope);
        //负责人
        viewDelegate.setOnClickListener(v -> {
            ArrayList<Member> list = new ArrayList<Member>();
            if (admin != null) {
                admin.setCheck(true);
                list.add(admin);
            }
            Bundle bundle = new Bundle();
            bundle.putInt(C.CHECK_TYPE_TAG, C.RADIO_SELECT);
            bundle.putSerializable(C.SPECIAL_MEMBERS_TAG, list);
            CommonUtil.startActivtiyForResult(mContext,
                    SelectMemberActivity.class, Constants.REQUEST_CODE1, bundle);
        }, R.id.rl_admin);

        //截止时间
        viewDelegate.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DateTimeSelectPresenter.CALENDAR, mCalendar == null ? Calendar.getInstance() : mCalendar);
            bundle.putString(DateTimeSelectPresenter.FORMAT, "yyyy-MM-dd");
            CommonUtil.startActivtiyForResult(mContext, DateTimeSelectPresenter.class, Constants.REQUEST_CODE2, bundle);
        }, R.id.rl_end_time);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuClick();
        return super.onOptionsItemSelected(item);
    }

    private void MenuClick() {
        if (admin == null) {
            ToastUtils.showError(this, "请选择负责人");
            return;
        }
        String title = viewDelegate.getTitle();
        if (TextUtil.isEmpty(title)) {
            ToastUtils.showError(this, "请填写项目名称");
            return;
        }
        if (mCalendar == null) {
            ToastUtils.showError(this, "请填写截止时间");
            return;
        }
        if (mCalendar.getTimeInMillis() < System.currentTimeMillis()) {
            DialogUtils.getInstance().sureOrCancel(mContext, mContext.getString(R.string.hint), "截止时间小于当前时间,请确认是否创建", "是", "否", viewDelegate.getRootView(), () -> {
                addProject(title);
            });
        } else {
            addProject(title);
        }
    }

    /**
     * 新增项目
     */
    public void addProject(String title) {
        SaveProjectRequestBean bean = new SaveProjectRequestBean();
        bean.setVisualRange(currentIndex);
        bean.setName(title);
        bean.setNote(viewDelegate.getDes());
        bean.setLeader(admin.getId());
        bean.setEndTime(mCalendar.getTimeInMillis());
        if (tempData != null) {
            bean.setTempId(tempData.getId());
        }
        model.addProject(this, bean, new ProgressSubscriber<CommonNewResultBean2>(this) {
            @Override
            public void onNext(CommonNewResultBean2 baseBean) {
                super.onNext(baseBean);
                ToastUtils.showSuccess(mContext, "新建项目成功");
                Bundle bundle = new Bundle();
                bundle.putLong(ProjectConstants.PROJECT_ID, baseBean.getData().getId());
                CommonUtil.startActivtiy(mContext, ProjectDetailActivity.class, bundle);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            //负责人
            ArrayList<Member> list = (ArrayList<Member>) data.getSerializableExtra(Constants.DATA_TAG1);
            if (list != null && list.size() > 0) {
                admin = list.get(0);
            } else {
                admin = null;
            }
            viewDelegate.setAdmin(admin);
        } else if (requestCode == Constants.REQUEST_CODE2) {
            //截止时间
            if (resultCode == RESULT_OK && data != null) {
                Calendar c = (Calendar) data.getSerializableExtra(DateTimeSelectPresenter.CALENDAR);
                if (c != null) {
                    mCalendar = c;
                    viewDelegate.setEndTime(mCalendar);
                }
            } else if (resultCode == Constants.CLEAR_RESULT_CODE) {
                mCalendar = null;
                viewDelegate.clearEndTime();
            }
        } else if (requestCode == Constants.REQUEST_CODE3 && resultCode == RESULT_OK) {
            //模版
            tempData = (TempDataList) data.getSerializableExtra(Constants.DATA_TAG1);
            viewDelegate.setTempName(tempData.getName());
        }
    }
}
