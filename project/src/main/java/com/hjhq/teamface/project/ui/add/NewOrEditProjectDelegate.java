package com.hjhq.teamface.project.ui.add;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.ProjectConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.image.ImageLoader;
import com.hjhq.teamface.basis.util.DateTimeUtil;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;
import com.hjhq.teamface.project.presenter.EditProjectActivity;

import java.util.Calendar;

/**
 * 新增项目/项目设置
 */

public class NewOrEditProjectDelegate extends AppDelegate {
    private TextView scaleText;
    private TextView tagText;
    private TextView adminText;
    private TextView endTimeText;
    private TextView startTimeText;
    private TextView projectStatusText;
    private EditText mEtProjectName;
    private EditText mEtProjectDes;
    private TextView mTvProjectDes;
    private EditText mEtProgress;
    private TextView mTvProgress;
    private TextView mTvProjectName;
    private ImageView adminAvatar;
    private View actionView;
    FrameLayout bottomActionBar;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_add_or_set_activity_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        scaleText = get(R.id.tv_scope_content);
        tagText = get(R.id.tv_template_name);
        adminText = get(R.id.tv_admin_name);
        endTimeText = get(R.id.tv_end_time_content);
        startTimeText = get(R.id.tv_start_time_content);
        adminAvatar = get(R.id.iv_admin_avatar);
        mEtProgress = get(R.id.et_progress);
        mTvProgress = get(R.id.tv_progress_content);
        mEtProjectName = get(R.id.et_name);
        mTvProjectName = get(R.id.tv_name);
        mTvProjectDes = get(R.id.tv_des);
        mEtProjectDes = get(R.id.et_des);
        projectStatusText = get(R.id.tv_status_content);
        bottomActionBar = get(R.id.fl_can);
    }

    /**
     * 设置人员标题名称
     *
     * @param text
     */
    public void setAdminText(String text) {
        TextUtil.setText(adminText, text);
    }

    /**
     * 公开或不公开
     *
     * @param s
     */
    public void setScopeText(String s) {
        TextUtil.setText(scaleText, s);
    }

    /**
     * 设置负责人
     *
     * @param admin
     */
    public void setAdmin(Member admin) {
        if (admin != null) {
            ImageLoader.loadCircleImage(getActivity(), admin.getPicture(), adminAvatar, admin.getName());
        } else {
            ImageLoader.loadCircleImage(getActivity(), R.drawable.project_icon_add_member, adminAvatar);
        }
    }

    public void setTagText(String tag) {
        TextUtil.setText(tagText, "标签");
    }

    /**
     * 结束时间
     *
     * @param c
     */
    public void setEndTime(Calendar c) {
        if (c != null) {
            TextUtil.setText(endTimeText, DateTimeUtil.longToStr(c.getTimeInMillis(), "yyyy-MM-dd"));
        } else {
            TextUtil.setText(endTimeText, "");
        }
    }

    /**
     * 清除结束时间
     */
    public void clearEndTime() {
        TextUtil.setText(endTimeText, "");
    }

    /**
     * 设置开始时间
     *
     * @param c
     */
    public void setStartTime(Calendar c) {
        if (c != null) {
            TextUtil.setText(startTimeText, DateTimeUtil.longToStr(c.getTimeInMillis(), "yyyy-MM-dd"));
        } else {
            TextUtil.setText(startTimeText, "");
        }


    }

    /**
     * 进度
     *
     * @param p 进度 0自动计算 1手动填写
     * @param s
     */
    public void setProgressText(int p, String s) {
        TextUtil.setText(mTvProgress, s);
        setVisibility(R.id.rl_progress, p != 0);
    }

    /**
     * 底部操作按钮
     *
     * @param status
     */
    public void setButtomOptions(String status) {
        bottomActionBar.removeAllViews();
        switch (status) {
            case ProjectConstants.PROJECT_STATUS_RUNNING:
                actionView = getActivity().getLayoutInflater().inflate(R.layout.project_setting_options_layout0, null);
                actionView.findViewById(R.id.ll_action1).setOnClickListener(v -> {
                    ((EditProjectActivity) getActivity()).switchProjectStatus(ProjectConstants.PROJECT_STATUS_FILED);

                });
                actionView.findViewById(R.id.ll_action2).setOnClickListener(v -> {
                    ((EditProjectActivity) getActivity()).switchProjectStatus(ProjectConstants.PROJECT_STATUS_PAUSE);

                });
                actionView.findViewById(R.id.ll_action3).setOnClickListener(v -> {
                    ((EditProjectActivity) getActivity()).switchProjectStatus(ProjectConstants.PROJECT_STATUS_DELETED);

                });
                bottomActionBar.addView(actionView);
                break;
            case ProjectConstants.PROJECT_STATUS_PAUSE:
            case ProjectConstants.PROJECT_STATUS_FILED:
            case ProjectConstants.PROJECT_STATUS_DELETED:
                actionView = getActivity().getLayoutInflater().inflate(R.layout.project_setting_options_layout1, null);
                actionView.findViewById(R.id.ll_action1).setOnClickListener(v -> {
                    ((EditProjectActivity) getActivity()).switchProjectStatus(ProjectConstants.PROJECT_STATUS_RUNNING);
                });
                bottomActionBar.addView(actionView);
                break;
            default:
                break;
        }
    }

    /**
     * 获取标题
     */
    public String getTitle() {
        EditText view = get(R.id.et_name);
        return view.getText().toString().trim();
    }

    /**
     * 获取描述
     */
    public String getDes() {
        EditText view = get(R.id.et_des);
        return view.getText().toString().trim();
    }

    /**
     * 设置项目名字
     *
     * @param name
     */
    public void setProjectName(String name) {
        TextUtil.setText(mEtProjectName, name);
        TextUtil.setText(mTvProjectName, name);
    }

    /**
     * 获取项目名字
     *
     * @return
     */
    public String getProjectName() {
        return mEtProjectName.getText().toString();
    }

    /**
     * 设置项目描述
     *
     * @param name
     */
    public void setProjectDes(String name) {
        TextUtil.setText(mEtProjectDes, name);
        TextUtil.setText(mTvProjectDes, name);
    }

    /**
     * 获取项目描述
     *
     * @return
     */
    public String getProjectDes() {
        return mEtProjectDes.getText().toString();
    }

    /**
     * 设置项目负责人
     *
     * @param url
     * @param name
     */
    public void setProjectPrinciple(String url, String name) {
        ImageLoader.loadCircleImage(getActivity(), url, adminAvatar, name);

    }

    /**
     * 设置项目进度值
     *
     * @param persent
     */
    public void setProjectProgress(String persent) {
        TextUtil.setText(mEtProgress, persent);
    }

    /**
     * 项目状态
     *
     * @param s
     */
    public void setProjectStatusText(String s) {
        TextUtil.setText(projectStatusText, s);
    }

    /**
     * 获取手动输入的进度
     *
     * @return
     */
    public int getProgressValue() {
        int value = TextUtil.parseInt(mEtProgress.getText().toString().trim());
        return value;
    }

    /**
     * 设置模板
     *
     * @param name
     */
    public void setTempName(String name) {
        TextUtil.setText((TextView) get(R.id.tv_end_template_content), name);
    }


    /**
     * 是否可修改项目信息
     *
     * @param b
     */
    public void projectEditable(boolean b) {
        if (b) {
            showMenu(0);
            get(R.id.rl_admin).setClickable(true);
            get(R.id.rl_start_time).setClickable(true);
            get(R.id.rl_end_time).setClickable(true);
            get(R.id.rl_scope).setClickable(true);
            get(R.id.rl_progress_type).setClickable(true);
            get(R.id.tv_des).setVisibility(View.GONE);
            get(R.id.tv_name).setVisibility(View.GONE);
            get(R.id.et_name).setVisibility(View.VISIBLE);
            get(R.id.et_des).setVisibility(View.VISIBLE);
            mEtProgress.setFocusable(true);
            mEtProgress.setFocusableInTouchMode(true);
            mEtProgress.requestFocus();
        } else {
            showMenu();
            get(R.id.rl_admin).setClickable(false);
            get(R.id.rl_start_time).setClickable(false);
            get(R.id.rl_end_time).setClickable(false);
            get(R.id.rl_scope).setClickable(false);
            get(R.id.rl_progress_type).setClickable(false);
            get(R.id.tv_des).setVisibility(View.VISIBLE);
            get(R.id.tv_name).setVisibility(View.VISIBLE);
            get(R.id.et_name).setVisibility(View.GONE);
            get(R.id.et_des).setVisibility(View.GONE);
            mEtProgress.setFocusable(false);
            mEtProgress.setFocusableInTouchMode(false);
        }
    }
}
