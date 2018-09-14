package com.hjhq.teamface.custom.ui.funcation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hjhq.teamface.basis.constants.CustomConstants;
import com.hjhq.teamface.basis.database.Member;
import com.hjhq.teamface.basis.util.CollectionUtils;
import com.hjhq.teamface.basis.util.TextUtil;
import com.hjhq.teamface.basis.util.popupwindow.PopUtils;
import com.hjhq.teamface.basis.view.member.MembersView;
import com.hjhq.teamface.custom.R;
import com.hjhq.teamface.custom.bean.AddOrEditShareRequestBean;

import java.util.List;

/**
 * @author Administrator
 * @date 2017/12/21
 */

public class ShareItemView extends FrameLayout {
    public static final String[] AUTH = {"只读", "读／写", "读／写／删"};
    private int state = CustomConstants.DETAIL_STATE;
    private Context mContext;
    private View rootView;
    private View llOption;
    private View tvEdit;
    private View tvDel;
    private TextView tvContent;
    private MembersView memberView;
    private View llSelect;
    private View ivRight;
    private int access_permissions = 0;

    public ShareItemView(@NonNull Context context) {
        super(context);
        init(context);
    }


    public ShareItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShareItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        rootView = View.inflate(mContext, R.layout.custom_share_item, null);
        llOption = rootView.findViewById(R.id.ll_option);
        tvEdit = rootView.findViewById(R.id.tv_edit);
        tvDel = rootView.findViewById(R.id.tv_del);
        tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        memberView = (MembersView) rootView.findViewById(R.id.member_view);
        llSelect = rootView.findViewById(R.id.ll_select);
        ivRight = rootView.findViewById(R.id.iv_right);
        addView(rootView);

    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case CustomConstants.DETAIL_STATE:
                ivRight.setVisibility(View.GONE);
                memberView.setAddMemberIvVisibility(View.GONE);
                break;
            case CustomConstants.ADD_STATE:
            case CustomConstants.EDIT_STATE:
                llOption.setVisibility(View.GONE);
                llSelect.setOnClickListener(v -> {
                    PopUtils.showBottomMenu((Activity) mContext, rootView, "选择权限", AUTH, position -> {
                        TextUtil.setText(tvContent, AUTH[position]);
                        access_permissions = position;
                        return true;
                    });
                });
                break;
            default:
                break;
        }
    }

    public void setOnAddMemberClickedListener(MembersView.onAddMemberClickedListener onAddMemberClickedListener) {
        memberView.setOnAddMemberClickedListener(onAddMemberClickedListener);
    }

    public List<Member> getMembers() {
        return memberView.getMembers();
    }

    public void setMembers(List<Member> members) {
        memberView.setMembers(members);
    }

    public void setContent(int access_permissions) {
        this.access_permissions = access_permissions;
        TextUtil.setText(tvContent, AUTH[access_permissions]);
    }

    public AddOrEditShareRequestBean.BasicsBean getAddOrEditData() {
        List<Member> members = getMembers();
        if (CollectionUtils.isEmpty(members)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Member member : members) {
            sb.append(member.getName() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        AddOrEditShareRequestBean.BasicsBean basicsBean = new AddOrEditShareRequestBean.BasicsBean();
        basicsBean.setAccess_permissions(access_permissions + "");
        basicsBean.setAllot_employee(memberView.getMembers());
        basicsBean.setTarget_lable(sb.toString());
        return basicsBean;
    }
}
