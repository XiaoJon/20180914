package com.hjhq.teamface.attendance.views;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.attendance.R;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;

/**
 * Created by Administrator on 2017/11/9.
 * Describe：
 */

public class AddApprovalDelegate extends AppDelegate {
    TextView tvUnit;
    TextView tvState;
    TextView tvTitle;
    TextView tvStartTime;
    TextView tvStopTime;
    EditText etDuration;


    @Override
    public int getRootLayoutId() {
        return R.layout.attendance_add_approval_activity;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.attendance_add_approval_title);
        // TODO: 2018/6/8 蓝色带返回字样切图 
        getToolbar().setNavigationIcon(R.drawable.icon_blue_back);
        setRightMenuTexts(R.color.app_blue, "保存");
        tvState = get(R.id.state_content);
        tvUnit = get(R.id.unit_content);
        tvTitle = get(R.id.type_content);
        tvStartTime = get(R.id.content4);
        tvStopTime = get(R.id.content5);
        etDuration = get(R.id.content3);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        super.setOnClickListener(listener, ids);
    }

    public void setUnitText(String s) {
        tvUnit.setText(s);
    }

    public void setStateText(String s) {
        tvState.setText(s);
    }

    /**
     * 关联审批模块名/标题
     *
     * @param title
     */
    public void setApproveTitle(String title) {
        TextUtil.setText(tvTitle, title);
    }

    /**
     * 开始时间
     *
     * @param s
     */
    public void setStartTime(String s) {
        TextUtil.setText(tvStartTime, s);

    }

    /**
     * 结束时间
     *
     * @param s
     */
    public void setStopTime(String s) {
        TextUtil.setText(tvStopTime, s);

    }

    /**
     * 获取输入的时长
     *
     * @return
     */
    public String getDurationText() {
        return etDuration.getText().toString();
    }
}
