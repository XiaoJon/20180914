package com.hjhq.teamface.project.ui.task;

import android.widget.EditText;
import android.widget.TextView;

import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.project.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 */

public class AddSubTaskDelegate extends AppDelegate {
    private EditText etSubTitle;
    public MembersView mMemberView;
    private TextView tvDeadline;

    @Override
    public int getRootLayoutId() {
        return R.layout.project_activity_add_sub_task;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.project_sub_task_detail);
        setRightMenuTexts(mContext.getString(R.string.finish));
        etSubTitle = get(R.id.et_sub_task_title);
        tvDeadline = get(R.id.tv_deadline);
        mMemberView = get(R.id.member_view);
    }

    /**
     * 获取子任务标题
     *
     * @return
     */
    public String getSubTitle() {
        return etSubTitle.getText().toString().trim();
    }

    /**
     * 获取截止时间
     *
     * @return
     */
    public String getDeadline() {
        return tvDeadline.getText().toString().trim();
    }

    /**
     * 获取执行人
     *
     * @return
     */
    public String getExecutorId() {
        List<Member> members = mMemberView.getMembers();
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Member member : members) {
            sb.append("," + member.getId());
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }

    /**
     * 设置截止时间
     *
     * @param deadline
     */
    public void setDeadline(String deadline) {
        TextUtil.setText(tvDeadline, deadline);
    }
}