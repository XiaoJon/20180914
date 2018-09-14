package com.hjhq.teamface.oa.approve.ui;

import com.hjhq.teamface.basis.constants.Constants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.zygote.ActivityPresenter;

import java.util.List;

/**
 * 审批抄送人
 *
 * @author Administrator
 * @date 2018/5/28
 */

public class ApproveCcActivity extends ActivityPresenter<ApproveCcDelegate, ApproveModel> {
    @Override
    public void init() {
        List<Member> memberList = (List<Member>) getIntent().getSerializableExtra(Constants.DATA_TAG1);

        viewDelegate.setMemberList(memberList);
    }
}
