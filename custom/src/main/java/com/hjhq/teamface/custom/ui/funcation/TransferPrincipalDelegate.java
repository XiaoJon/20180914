package com.hjhq.teamface.custom.ui.funcation;

import android.widget.TextView;

import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.basis.zygote.AppDelegate;
import com.hjhq.teamface.custom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 转移负责人
 *
 * @author Administrator
 * @date 2017/12/20
 */

public class TransferPrincipalDelegate extends AppDelegate {
    private MembersView memberView;
    private TextView tvSelectValue;

    @Override
    public int getRootLayoutId() {
        return R.layout.custom_transfer_principal_layout;
    }

    @Override
    public boolean isToolBar() {
        return true;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        setTitle("转移负责人");

        setRightMenuTexts(R.color.main_green, "确定");

        memberView = get(R.id.member_view);
        tvSelectValue = get(R.id.tv_select_value);
    }

    public void setSelectValue(String value) {
        TextUtil.setText(tvSelectValue, value);
    }

    public List<Member> getMembers() {
        return memberView.getMembers();
    }

    public void setOnAddMemberClickedListener(MembersView.onAddMemberClickedListener listener) {
        memberView.setOnAddMemberClickedListener(listener);
    }

    public void setMembers(ArrayList<Member> members) {
        memberView.setMembers(members);
    }
}
