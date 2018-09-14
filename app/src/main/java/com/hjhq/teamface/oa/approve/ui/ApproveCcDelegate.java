package com.hjhq.teamface.oa.approve.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hjhq.teamface.R;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.oa.approve.adapter.ApproveCcAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批抄送人
 *
 * @author Administrator
 * @date 2018/5/28
 */

public class ApproveCcDelegate extends AppDelegate {
    private RecyclerView mRecyclerView;
    private TextView tvCc;

    @Override
    public int getRootLayoutId() {
        return R.layout.approve_cc_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle(R.string.approve_cc);

        mRecyclerView = get(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tvCc = get(R.id.tv_cc);
    }

    public void setMemberList(List<Member> memberList) {
        if (memberList == null) {
            memberList = new ArrayList<>(0);
        }
        TextUtil.setText(tvCc, "抄送人 (" + memberList.size() + ")");
        mRecyclerView.setAdapter(new ApproveCcAdapter(memberList));
    }
}
