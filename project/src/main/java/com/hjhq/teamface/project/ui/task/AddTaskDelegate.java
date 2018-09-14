package com.hjhq.teamface.project.ui.task;

import android.view.View;
import android.widget.LinearLayout;

import com.hjhq.teamface.basis.bean.CustomBean;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.customcomponent.widget2.BaseView;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.bean.TaskLayoutResultBean;
import com.hjhq.teamface.project.widget.utils.ProjectCustomUtil;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新增任务
 *
 * @author Administrator
 * @date 2018/7/3
 */

public class AddTaskDelegate extends AppDelegate {
    private LinearLayout llContent;
    private View rlTaskCheck;
    public SwitchButton sBtnTaskCheck;
    public SwitchButton sBtnOnlyParticipantVisible;
    private View llTaskCheckOne;
    public MembersView memberView;
    public List<BaseView> listView = new ArrayList<>();

    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_task_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_add_task);
        setRightMenuTexts(mContext.getString(R.string.confirm));

        llContent = get(R.id.ll_custom_layout);
        rlTaskCheck = get(R.id.rl_task_check);
        sBtnTaskCheck = get(R.id.sbtn_task_check);
        llTaskCheckOne = get(R.id.ll_task_check_one);
        memberView = get(R.id.member_view);
        sBtnOnlyParticipantVisible = get(R.id.sbtn_only_participant_visible);
    }

    public void drawLayout(TaskLayoutResultBean.DataBean.EnableLayoutBean enableLayout, HashMap detailMap,int state, boolean personalTask) {
        llContent.removeAllViews();
        listView.clear();
        List<CustomBean> rows = enableLayout.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return;
        }

        for (CustomBean layoutBean : rows) {
            if (detailMap != null) {
                Object o = detailMap.get(layoutBean.getName());
                layoutBean.setValue(o);
            }

            BaseView customView = ProjectCustomUtil.drawLayout(llContent, layoutBean, state, personalTask);
            if (customView != null) {
                listView.add(customView);
            }
        }
    }

    /**
     * 隐藏检测控件
     */
    public void hideCheckView() {
        setVisibility(R.id.ll_task_check_one, false);
        setVisibility(R.id.rl_task_check, false);
    }

    /**
     * 切换校验人
     *
     * @param isChecked
     */
    public void switchTaskCheck(boolean isChecked) {
        setVisibility(R.id.ll_task_check_one, isChecked);
    }

    /**
     * 设置默认检验人
     *
     * @param member
     */
    public void setDefaultCheckOne(Member member) {
        memberView.setMember(member);
    }

    /**
     * 开启任务校验
     */
    public void openTaskStatus() {
        sBtnTaskCheck.setChecked(true);
        switchTaskCheck(true);
    }

    public String getTaskCheckStatus() {
        return sBtnTaskCheck.isChecked() ? "1" : "0";
    }

    public String getOnlyParticipantStatus() {
        return sBtnOnlyParticipantVisible.isChecked() ? "1" : "0";
    }

    public void setOnlyParticipantStatus(boolean isCheck) {
        sBtnOnlyParticipantVisible.setChecked(isCheck);
    }

    /**
     * 获取检验人ID
     *
     * @return
     */
    public String getCheckOneId() {
        String id = null;
        List<Member> members = memberView.getMembers();
        if (!CollectionUtils.isEmpty(members)) {
            id = members.get(0).getId() + "";
        }
        return id;
    }
}
